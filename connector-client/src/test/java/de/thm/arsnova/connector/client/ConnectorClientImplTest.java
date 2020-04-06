package de.thm.arsnova.connector.client;

import de.thm.arsnova.connector.model.Courses;
import de.thm.arsnova.connector.model.Membership;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static de.thm.arsnova.connector.model.UserRole.TEACHER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(MockitoExtension.class)
public class ConnectorClientImplTest {

	private ResourceLoader resourceLoader = new DefaultResourceLoader();
	private RestTemplate restTemplate = new RestTemplate();
	private MockRestServiceServer mockServer;

	private ConnectorClientImpl connectorClient;

	@BeforeEach
	public void setup() {
		mockServer = MockRestServiceServer.createServer(restTemplate);
		connectorClient = new ConnectorClientImpl(restTemplate);
		connectorClient.setServiceLocation("http://testuri1234.local.site/");
		connectorClient.setUsername("test");
		connectorClient.setPassword("test");
	}

	@Test
	@DisplayName("test should get courses")
	public void testShouldGetCourses() {
		Resource responseResource = resourceLoader.getResource("classpath:response_courses.json");

		mockServer.expect(
				ExpectedCount.once(),
				requestTo(URI.create("http://testuri1234.local.site/ptsr00/courses"))
		)
				.andExpect(method(HttpMethod.GET))
				.andRespond(
						withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(responseResource)
				);

		Courses courses = connectorClient.getCourses("ptsr00");
		mockServer.verify();
		assertThat(courses.getCourse()).hasSize(1);
		assertThat(courses.getCourse().get(0).getFullname()).isEqualTo("Testkurs");
	}

	@Test
	@DisplayName("test should get membership of user in given course using getMembership()")
	public void testShouldGetMembershipUsingGetMembership() {
		Resource responseResource = resourceLoader.getResource("classpath:response_membership.json");

		mockServer.expect(
				ExpectedCount.once(),
				requestTo(URI.create("http://testuri1234.local.site/ptsr00/membership/42"))
		)
				.andExpect(method(HttpMethod.GET))
				.andRespond(
						withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(responseResource)
				);

		Membership membership = connectorClient.getMembership("ptsr00", "42");
		mockServer.verify();
		assertThat(membership.isMember()).isTrue();
		assertThat(membership.getUserrole()).isEqualTo(TEACHER);
	}

	@Test
	@DisplayName("test should get membership of user in given course using deprecated isMember()")
	public void testShouldGetMembershipUsingIsMember() {
		Resource responseResource = resourceLoader.getResource("classpath:response_membership.json");

		mockServer.expect(
				ExpectedCount.once(),
				requestTo(URI.create("http://testuri1234.local.site/ptsr00/membership/42"))
		)
				.andExpect(method(HttpMethod.GET))
				.andRespond(
						withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(responseResource)
				);

		Membership membership = connectorClient.isMember("ptsr00", "42");
		mockServer.verify();
		assertThat(membership.isMember()).isTrue();
		assertThat(membership.getUserrole()).isEqualTo(TEACHER);
	}
}
