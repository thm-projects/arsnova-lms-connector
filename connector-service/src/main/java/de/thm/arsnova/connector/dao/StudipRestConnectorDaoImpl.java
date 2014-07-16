package de.thm.arsnova.connector.dao;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.thm.arsnova.connector.model.Course;
import de.thm.arsnova.connector.model.Membership;
import de.thm.arsnova.connector.model.UserRole;
import de.thm.arsnova.connector.model.studip_rest.StudipCourse;
import de.thm.arsnova.connector.model.studip_rest.StudipCoursesWrapper;
import de.thm.arsnova.connector.model.studip_rest.StudipUser;
import de.thm.arsnova.connector.model.studip_rest.StudipUsersWrapper;

public class StudipRestConnectorDaoImpl implements ConnectorDao {

	private static final String TYPE = "studip";

	private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

	private static final Logger logger = LoggerFactory.getLogger(StudipRestConnectorDaoImpl.class);

	private final RestTemplate restTemplate = new RestTemplate();

	private String userMembershipUri = "/user/{userid}/courses";
	private String userSearchUri = "/users?needle={username}";

	@Value("${lms.http.serverUrl}") private String uriHostPart;
	@Value("${lms.http.username}") private String httpUsername;
	@Value("${lms.http.password}") private String httpPassword;

	@PostConstruct
	public void initialize() {
		logger.info("Initializing {}", StudipRestConnectorDaoImpl.class.getName());
		userMembershipUri = uriHostPart + userMembershipUri;
		userSearchUri = uriHostPart + userSearchUri;
		logger.debug("Using userMembershipUri={}, userSearchUri={}", userMembershipUri, userSearchUri);
	}

	@Override
	public List<String> getCourseUsers(final String courseid) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Membership getMembership(final String username, final String courseid) {
		logger.trace("Retrieve membership status for user {} in course {}", username, courseid);
		Membership membership = new Membership();
		StudipUser user = getUserByName(username);
		if (null == user) {
			return membership;
		}

		try {
			ResponseEntity<StudipCoursesWrapper> courseWrapperEntity = restTemplate.exchange(
				userMembershipUri,
				HttpMethod.GET,
				new HttpEntity<Void>(getAuthorizationHeader()),
				StudipCoursesWrapper.class,
				user.getId()
			);
			List<StudipCourse> courses;

			if (null != (courses = courseWrapperEntity.getBody().getStudy())) {
				for (StudipCourse course : courses) {
					if (courseid.equals(course.getId())) {
						membership.setMember(true);
						membership.setUserrole(UserRole.MEMBER);
						logger.trace("User {} is a member of course {}", username, courseid);

						return membership;
					}
				}
			}
			if (null != (courses = courseWrapperEntity.getBody().getWork())) {
				for (StudipCourse course : courses) {
					if (courseid.equals(course.getId())) {
						membership.setMember(true);
						membership.setUserrole(UserRole.TEACHER);
						logger.trace("User {} is a teacher of course {}", username, courseid);

						return membership;
					}
				}
			}
		} catch (RestClientException | HttpMessageNotReadableException e) {
			/* Stud.IP sends an empty array instead of an object if no courses are found */
		}

		membership.setMember(false);
		logger.trace("User {} is not a member of course {}", username, courseid);

		return membership;
	}

	@Override
	public List<Course> getMembersCourses(final String username) {
		logger.trace("Retrieve courses for {}", username);
		List<Course> courses = new ArrayList<>();
		StudipUser user = getUserByName(username);
		if (null == user) {
			return courses;
		}

		try {
			ResponseEntity<StudipCoursesWrapper> courseWrapperEntity = restTemplate.exchange(
				userMembershipUri,
				HttpMethod.GET,
				new HttpEntity<Void>(getAuthorizationHeader()),
				StudipCoursesWrapper.class,
				user.getId()
			);
			List<StudipCourse> sipCourses;

			if (null != (sipCourses = courseWrapperEntity.getBody().getStudy())) {
				for (StudipCourse sipCourse : sipCourses) {
					courses.add(buildCourse(sipCourse, UserRole.MEMBER));
				}
			}
			if (null != (sipCourses = courseWrapperEntity.getBody().getWork())) {
				for (StudipCourse sipCourse : sipCourses) {
					courses.add(buildCourse(sipCourse, UserRole.TEACHER));
				}
			}
		} catch (RestClientException | HttpMessageNotReadableException e) {
			/* Stud.IP sends an empty array instead of an object if no courses are found */
		}

		return courses;
	}

	private StudipUser getUserByName(String username) {
		logger.trace("Retrieve user {}", username);
		ResponseEntity<StudipUsersWrapper> usersWrapperEntity = restTemplate.exchange(
			userSearchUri,
			HttpMethod.GET,
			new HttpEntity<Void>(getAuthorizationHeader()),
			StudipUsersWrapper.class,
			username
		);

		for (StudipUser user : usersWrapperEntity.getBody().getUsers()) {
			if (username.equals(user.getUsername())) {
				logger.trace("Username {} belongs to user {}", username, user);

				return user;
			}
		}
		logger.trace("No user was found for username {}", username);

		return null;
	}

	private HttpHeaders getAuthorizationHeader() {
		HttpHeaders httpHeaders = new HttpHeaders();
		String authorization = httpUsername + ":" + httpPassword;
		httpHeaders.add(
			"Authorization",
			"Basic " + Base64.encodeBase64String(authorization.getBytes(UTF8_CHARSET))
		);

		return httpHeaders;
	}

	private Course buildCourse(StudipCourse sipCourse, UserRole role) {
		Membership membership = new Membership();
		membership.setMember(true);
		membership.setUserrole(role);

		Course course = new Course();
		course.setId(sipCourse.getId());
		course.setFullname(sipCourse.getTitle());
		course.setShortname(sipCourse.getTitle());
		course.setType(TYPE);
		course.setMembership(membership);

		return course;
	}
}
