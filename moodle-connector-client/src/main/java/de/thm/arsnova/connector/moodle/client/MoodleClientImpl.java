package de.thm.arsnova.connector.moodle.client;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import de.thm.arsnova.connector.moodle.model.Courses;
import de.thm.arsnova.connector.moodle.model.Membership;

public class MoodleClientImpl implements MoodleClient {
	private final RestTemplate restTemplate = new RestTemplate();

	private static final String ISMEMBER_URI = "/{username}/membership/{courseid}";
	private static final String GETCOURSES_URI = "/{username}/courses";

	private String uriHostPart;
	private String username;
	private String password;

	@Autowired
	public void setServiceLocation(String serviceLocation) {
		uriHostPart = serviceLocation;
	}

	@Autowired
	public void setUsername(String uname) {
		username = uname;
	}

	@Autowired
	public void setPassword(String passwd) {
		password = passwd;
	}

	private HttpEntity<Membership> createMembershipEntity() {
		HttpHeaders httpHeaders = new HttpHeaders();
		String authorisation = username + ":" + password;
		System.out.println(authorisation);
		byte[] encodedAuthorisation = Base64.encodeBase64(authorisation.getBytes());
		httpHeaders.add("Authorization", "Basic " + new String(encodedAuthorisation));
		return new HttpEntity<Membership>(httpHeaders);
	}
	
	private HttpEntity<Courses> createCoursesEntity() {
		HttpHeaders httpHeaders = new HttpHeaders();
		String authorisation = username + ":" + password;
		byte[] encodedAuthorisation = Base64.encodeBase64(authorisation.getBytes());
		httpHeaders.add("Authorization", "Basic " + new String(encodedAuthorisation));
		return new HttpEntity<Courses>(httpHeaders);
	}

	private String buildRequestUri(String relativeUri) {
		return uriHostPart + relativeUri;
	}

	@Override
	public Membership isMember(String username, String courseid) {
		ResponseEntity<Membership> response = restTemplate.exchange(
				buildRequestUri(ISMEMBER_URI),
				HttpMethod.GET,
				createMembershipEntity(),
				Membership.class,
				username,
				courseid
		);
		
		return response.getBody();
	}

	@Override
	public Courses getCourses(String username) {
		ResponseEntity<Courses> response = restTemplate.exchange(
				buildRequestUri(GETCOURSES_URI),
				HttpMethod.GET,
				createCoursesEntity(),
				Courses.class,
				username
		);
		
		return response.getBody();
	}
}
