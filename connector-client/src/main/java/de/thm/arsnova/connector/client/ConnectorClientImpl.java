package de.thm.arsnova.connector.client;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import de.thm.arsnova.connector.model.Courses;
import de.thm.arsnova.connector.model.IliasCategoryNode;
import de.thm.arsnova.connector.model.IliasQuestion;
import de.thm.arsnova.connector.model.Membership;

public class ConnectorClientImpl implements ConnectorClient {
	private final RestTemplate restTemplate = new RestTemplate();

	private static final String ISMEMBER_URI = "/{username}/membership/{courseid}";
	private static final String GETCOURSES_URI = "/{username}/courses";
	private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
	private static final String ILIAS_TREEOBJECTS_URI = "/ilias/{refid}";
	private static final String ILIAS_QUESTIONS_URI = "/ilias/question/{refid}";

	private String uriHostPart;
	private String httpUsername;
	private String httpPassword;

	@Autowired
	public void setServiceLocation(String serviceLocation) {
		uriHostPart = serviceLocation;
	}

	@Autowired
	public void setUsername(String username) {
		httpUsername = username;
	}

	@Autowired
	public void setPassword(String password) {
		httpPassword = password;
	}

	@Override
	public Membership isMember(String username, String courseid) {
		return this.getMembership(username, courseid);
	}

	@Override
	public Membership getMembership(String username, String courseid) {
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

	@Override
	public List<IliasCategoryNode> getTreeObjects(int refId) {
		Class<List<IliasCategoryNode>> clazz = null;

		ResponseEntity<List<IliasCategoryNode>> response = restTemplate.exchange(
				buildRequestUri(ILIAS_TREEOBJECTS_URI),
				HttpMethod.GET,
				createNodeListEntity(),
				clazz,
				refId
				);
		return response.getBody();
	}

	@Override
	public List<IliasQuestion> getQuestions(int refId) {
		Class<List<IliasQuestion>> clazz = null;

		ResponseEntity<List<IliasQuestion>> response = restTemplate.exchange(
				buildRequestUri(ILIAS_QUESTIONS_URI),
				HttpMethod.GET,
				createQuestionListEntity(),
				clazz,
				refId
				);
		return response.getBody();
	}

	private HttpEntity<Membership> createMembershipEntity() {
		return new HttpEntity<Membership>(getAuthorizationHeader());
	}

	private HttpEntity<Courses> createCoursesEntity() {
		return new HttpEntity<Courses>(getAuthorizationHeader());
	}

	private HttpEntity<List<IliasCategoryNode>> createNodeListEntity() {
		return new HttpEntity<List<IliasCategoryNode>>(getAuthorizationHeader());
	}

	private HttpEntity<List<IliasQuestion>> createQuestionListEntity() {
		return new HttpEntity<List<IliasQuestion>>(getAuthorizationHeader());
	}

	private HttpHeaders getAuthorizationHeader() {
		HttpHeaders httpHeaders = new HttpHeaders();
		String authorisation = httpUsername + ":" + httpPassword;
		httpHeaders.add(
				"Authorization",
				"Basic " + Base64.encodeBase64String(authorisation.getBytes(UTF8_CHARSET))
				);
		return httpHeaders;
	}

	private String buildRequestUri(String relativeUri) {
		return uriHostPart + relativeUri;
	}
}
