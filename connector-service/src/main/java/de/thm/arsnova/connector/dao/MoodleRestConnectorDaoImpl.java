package de.thm.arsnova.connector.dao;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import de.thm.arsnova.connector.dao.model.moodle_rest.MoodleCourse;
import de.thm.arsnova.connector.dao.model.moodle_rest.MoodleUser;
import de.thm.arsnova.connector.model.Course;
import de.thm.arsnova.connector.model.Membership;
import de.thm.arsnova.connector.model.UserRole;

public class MoodleRestConnectorDaoImpl implements ConnectorDao {

	private static final String TYPE = "moodle";
	private static final int MOODLE_COURSE_EDITINGTEACHER = 3;
	private static final int MOODLE_COURSE_TEACHER = 4;
	private static final int MOODLE_COURSE_MEMBER = 5;

	@Value("${lms.http.token}") private String token ;
	@Value("${lms.http.serverUrl}") private String domainName;
	
	private static String enrolledUserInCourseURL;
	private static String userInfoByFieldURL;
	private static String usersCoursesURL;

	private final RestTemplate restTemplate = new RestTemplate();

	@PostConstruct
	public void initialize() {
		enrolledUserInCourseURL = domainName + "/webservice/rest/server.php" + "?wstoken=" + token + "&wsfunction=core_enrol_get_enrolled_users&moodlewsrestformat=json";
		userInfoByFieldURL = domainName + "/webservice/rest/server.php" + "?wstoken=" + token + "&wsfunction=core_user_get_users_by_field&moodlewsrestformat=json";
		usersCoursesURL = domainName + "/webservice/rest/server.php" + "?wstoken=" + token + "&wsfunction=core_enrol_get_users_courses&moodlewsrestformat=json";
	}
	
	@Override
	public List<String> getCourseUsers(String courseid) {
		final List<String> result = new ArrayList<String>();
		for(MoodleUser mu : getUsersInCourse(courseid))
		{
			result.add(mu.getUsername());
		}
		return result;
	}

	@Override
	public List<Course> getMembersCourses(String username) {
		final List<Course> result = new ArrayList<Course>();
		int userId=getIdByUsername(username);
		if(userId==-1)
			return result;
		
		try {
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("userid", URLEncoder.encode(""+userId, "UTF-8"));
			ResponseEntity<MoodleCourse[]> userWrapper=restTemplate.postForEntity(usersCoursesURL, map, MoodleCourse[].class);
			for(MoodleCourse mc : userWrapper.getBody())
			{
				result.add(buildCourse(mc, getMembership(username, mc.getId())));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	//TODO: Maybe find a better way to do this as this might produce quite some traffic in courses with many users
	@Override
	public Membership getMembership(String username, String courseid) {
		for(MoodleUser mu:getUsersInCourse(courseid))
		{
			if(mu.getUsername().equals(username))
			{
				Membership ms=new Membership();
				ms.setMember(true);
				ms.setUserrole(getMembershipRole(mu.getFirstMoodleRoleId()));
				return ms;
			}
		}
		return new Membership();
	}
	
	private int getIdByUsername(String username)
	{
		try {
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("field", URLEncoder.encode("username", "UTF-8"));
			map.add("values[0]", URLEncoder.encode(username, "UTF-8"));
			ResponseEntity<MoodleUser[]> userWrapper=restTemplate.postForEntity(userInfoByFieldURL, map, MoodleUser[].class);
			for(MoodleUser mu : userWrapper.getBody())
			{
				if(mu.getUsername().equals(username))
				{
					return mu.getId();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	private MoodleUser[] getUsersInCourse(String courseId)
	{
		try {
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("courseid", URLEncoder.encode(courseId, "UTF-8"));
			ResponseEntity<MoodleUser[]> userWrapper=restTemplate.postForEntity(enrolledUserInCourseURL, map, MoodleUser[].class);
			return userWrapper.getBody();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new MoodleUser[0];
	}
	
	//Copied from MoodleConnectorDao
	private UserRole getMembershipRole(final int moodleRoleId) {
		if (moodleRoleId == MOODLE_COURSE_EDITINGTEACHER || moodleRoleId == MOODLE_COURSE_TEACHER) {
			return UserRole.TEACHER;
		} else if (moodleRoleId == MOODLE_COURSE_MEMBER) {
			return UserRole.MEMBER;
		}

		// User is course guest
		return UserRole.OTHER;
	}
	
	private Course buildCourse(MoodleCourse mCourse, Membership membership) {
		Course course = new Course();
		course.setId(mCourse.getId());
		course.setFullname(mCourse.getFullname());
		course.setShortname(mCourse.getShortname());
		course.setType(TYPE);
		course.setMembership(membership);

		return course;
	}

}