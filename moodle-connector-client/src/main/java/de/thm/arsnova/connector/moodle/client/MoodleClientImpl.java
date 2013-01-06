package de.thm.arsnova.connector.moodle.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import de.thm.arsnova.connector.moodle.model.Courses;
import de.thm.arsnova.connector.moodle.model.Membership;

public class MoodleClientImpl implements MoodleClient {
	private final RestTemplate restTemplate = new RestTemplate();
	
	private final String IS_MEMBER_URI = "/{username}/membership/{courseid}";
	private final String GET_COURSES_URI = "/{username}/courses";
	
	private String uriHostPart;
	
	@Autowired
	public void setServiceLocation(String serviceLocation) {
		uriHostPart = serviceLocation;
	}
	
	@Override
	public Membership isMember(String username, String courseid) {
		return restTemplate.getForObject(uriHostPart + IS_MEMBER_URI, Membership.class, username, courseid);
	}
	
	@Override
	public Courses getCourses(String username) {
		return restTemplate.getForObject(uriHostPart + GET_COURSES_URI, Courses.class, username);
	}
}
