package de.thm.arsnova.connector.client;

import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import de.thm.arsnova.connector.model.Courses;
import de.thm.arsnova.connector.model.Membership;

public class ConnectorClientImpl implements ConnectorClient {
	private final RestTemplate restTemplate = new RestTemplate();

	private static final String ISMEMBER_URI = "/{username}/membership/{courseid}";
	private static final String GETCOURSES_URI = "/{username}/courses";

	private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

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
		return new HttpEntity<Membership>(getAuthorizationHeader());
	}

	private HttpEntity<Courses> createCoursesEntity() {
		return new HttpEntity<Courses>(getAuthorizationHeader());
	}

	private HttpHeaders getAuthorizationHeader() {
		final HttpHeaders httpHeaders = new HttpHeaders();
		final String authorisation = httpUsername + ":" + httpPassword;
		httpHeaders.add(
				"Authorization",
				"Basic " + Base64.encodeBase64String(authorisation.getBytes(UTF8_CHARSET))
				);
		return httpHeaders;
	}

	private String buildRequestUri(final String relativeUri) {
		return uriHostPart + relativeUri;
	}
}
