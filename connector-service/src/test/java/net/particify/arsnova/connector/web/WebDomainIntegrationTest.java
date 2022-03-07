package net.particify.arsnova.connector.web;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import net.particify.arsnova.connector.config.DummyTestConfig;
import net.particify.arsnova.connector.config.RepositoryTestConfig;
import net.particify.arsnova.connector.config.SecurityTestConfig;
import net.particify.arsnova.connector.config.WebConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {
		DummyTestConfig.class,
		SecurityTestConfig.class,
		WebConfig.class,
		RepositoryTestConfig.class
})
public class WebDomainIntegrationTest {

	private MockMvc mockMvc;

	@Autowired
	WebApplicationContext webApplicationContext;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testShouldReturnCoursesWithoutCredentials() throws Exception {
		try {
			mockMvc.perform(get("/test/courses").accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
		} catch (final NestedServletException e) {
			assertTrue( e.getCause() instanceof AuthenticationCredentialsNotFoundException );
			return;
		}

		fail("AccessDeniedException expected");
	}

	@Test
	public void testShouldReturnMembership() throws Exception {
		final List<GrantedAuthority> ga = new ArrayList<GrantedAuthority>();
		ga.add(new SimpleGrantedAuthority("ADMIN"));
		final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("admin", "secret", ga);
		SecurityContextHolder.getContext().setAuthentication(token);
		mockMvc.perform(get("/test/membership/42").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	public void testShouldNotReturnMembershipOnInvalidCredentials() throws Exception {
		final List<GrantedAuthority> ga = new ArrayList<GrantedAuthority>();
		final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("tester", "notsecret", ga);
		SecurityContextHolder.getContext().setAuthentication(token);

		try {
			mockMvc.perform(get("/test/membership/42").accept(MediaType.APPLICATION_JSON))
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
		} catch (final NestedServletException e) {
			assertTrue( e.getCause() instanceof AccessDeniedException );
			return;
		}

		fail("AccessDeniedException expected");
	}

	@Test
	public void testShouldReturnCourses() throws Exception {
		final List<GrantedAuthority> ga = new ArrayList<GrantedAuthority>();
		ga.add(new SimpleGrantedAuthority("ADMIN"));
		final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("admin", "secret", ga);
		SecurityContextHolder.getContext().setAuthentication(token);
		mockMvc.perform(get("/test/courses").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	public void testShouldNotReturnCoursesOnInvalidCredentials() throws Exception {
		final List<GrantedAuthority> ga = new ArrayList<GrantedAuthority>();
		final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("tester", "notsecret", ga);
		SecurityContextHolder.getContext().setAuthentication(token);

		try {
			mockMvc.perform(get("/test/courses").accept(MediaType.APPLICATION_JSON))
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
		} catch (final NestedServletException e) {
			assertTrue( e.getCause() instanceof AccessDeniedException );
			return;
		}

		fail("AccessDeniedException expected");
	}

	@Test
	public void testShouldRejectRequestNotAcceptingJson() throws Exception {
		mockMvc.perform(get("/test/membership/42").accept(MediaType.TEXT_PLAIN))
				.andExpect(status().isNotAcceptable());
	}
}
