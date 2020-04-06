package de.thm.arsnova.connector.client;

import de.thm.arsnova.connector.model.Courses;
import de.thm.arsnova.connector.model.Membership;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

public class ConnectorClientImpl implements ConnectorClient {
	private final RestTemplate restTemplate = new RestTemplate();

	private static final String ISMEMBER_URI = "/{username}/membership/{courseid}";
	private static final String GETCOURSES_URI = "/{username}/courses";

	private String uriHostPart;
	private String httpUsername;
	private String httpPassword;

	public void setServiceLocation(final String serviceLocation) {
		uriHostPart = serviceLocation;
	}

	public void setUsername(final String username) {
		httpUsername = username;
	}

	public void setPassword(final String password) {
		httpPassword = password;
	}

	@Override
	public Membership isMember(final String username, final String courseid) {
		return getMembership(username, courseid);
	}

	@Override
	public Membership getMembership(final String username, final String courseid) {
		final ResponseEntity<Membership> response = restTemplate.exchange(
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
	public Courses getCourses(final String username) {
		final ResponseEntity<Courses> response = restTemplate.exchange(
				buildRequestUri(GETCOURSES_URI),
				HttpMethod.GET,
				createCoursesEntity(),
				Courses.class,
				username
				);
		return response.getBody();
	}

	private HttpEntity<Membership> createMembershipEntity() {
		return new HttpEntity<>(getAuthorizationHeader());
	}

	private HttpEntity<Courses> createCoursesEntity() {
		return new HttpEntity<>(getAuthorizationHeader());
	}

	private HttpHeaders getAuthorizationHeader() {
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setBasicAuth(httpUsername, httpPassword, StandardCharsets.UTF_8);
		return httpHeaders;
	}

	private String buildRequestUri(final String relativeUri) {
		return uriHostPart + relativeUri;
	}
}
