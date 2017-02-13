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

import com.fasterxml.jackson.databind.ObjectMapper;

import de.thm.arsnova.connector.model.Course;
import de.thm.arsnova.connector.model.Membership;
import de.thm.arsnova.connector.model.UserRole;
import de.thm.arsnova.connector.model.moodle_rest.MoodleCourse;

public class MoodleRestConnectorDaoImpl implements ConnectorDao{

	private static final String TYPE = "moodle";
	private static final int MOODLE_COURSE_EDITINGTEACHER = 3;
	private static final int MOODLE_COURSE_TEACHER = 4;
	private static final int MOODLE_COURSE_MEMBER = 5;
	
	private static String ENCODING="UTF-8";

	@Value("${lms.http.token}") private String token ;
	@Value("${lms.http.serverUrl}") private String domainName;

	private static String getCourseUserURL;
	private static String getMembersCoursesURL;
	private static String getMembershipURL;

	private final RestTemplate restTemplate = new RestTemplate();
	
	@PostConstruct
	public void initialize() {
		getCourseUserURL = domainName + "/webservice/rest/server.php?wstoken=" + token + "&wsfunction=local_arsnova_connector_get_course_users&moodlewsrestformat=json";
		getMembersCoursesURL = domainName + "/webservice/rest/server.php?wstoken=" + token + "&wsfunction=local_arsnova_connector_get_users_courses&moodlewsrestformat=json";
		getMembershipURL = domainName + "/webservice/rest/server.php?wstoken=" + token + "&wsfunction=local_arsnova_connector_get_user_role_in_course&moodlewsrestformat=json";
	}

	/**
	 * It seems that this function is never used anywhere, therefore it isn't tested
	 */
	@Override
	public List<String> getCourseUsers(String courseid) {
		final List<String> result = new ArrayList<String>();
		ResponseEntity<String> userWrapper=null;
		try {
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("courseid", URLEncoder.encode(courseid, ENCODING));
			userWrapper=restTemplate.postForEntity(getCourseUserURL, map, String.class);
			String[] users= new ObjectMapper().readValue(userWrapper.getBody(), String[].class);
			for(String s : users)
			{
				result.add(s);
			}
		} catch (Exception e) {
			System.out.println(userWrapper.getBody());
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public List<Course> getMembersCourses(String username) {
		List<Course> result = new ArrayList<Course>();
		ResponseEntity<String> userWrapper=null;
		try {
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("username", URLEncoder.encode(username, ENCODING));
			userWrapper=restTemplate.postForEntity(getMembersCoursesURL, map, String.class);
			MoodleCourse[] courses=new ObjectMapper().readValue(userWrapper.getBody(), MoodleCourse[].class);
			for(MoodleCourse mc : courses)
			{
				result.add(buildCourse(mc, getMembership(username, mc.getId())));
			}
		} catch (Exception e) {
			System.out.println(userWrapper.getBody());
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public Membership getMembership(String username, String courseid) {
		Membership ms=new Membership();
		ResponseEntity<String> userWrapper=null;
		try {
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("username", URLEncoder.encode(username, ENCODING));
			map.add("courseid", URLEncoder.encode(courseid, ENCODING));
			userWrapper=restTemplate.postForEntity(getMembershipURL, map, String.class);
			ms.setUserrole(getMembershipRole(Integer.parseInt(userWrapper.getBody())));
			if(ms.getUserrole()!=UserRole.OTHER)
				ms.setMember(true);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(userWrapper.getBody());
			ms.setUserrole(UserRole.OTHER);
		}
		return ms;
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