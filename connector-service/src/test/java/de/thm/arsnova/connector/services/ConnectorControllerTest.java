package de.thm.arsnova.connector.services;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import de.thm.arsnova.connector.model.Course;
import de.thm.arsnova.connector.model.Courses;
import de.thm.arsnova.connector.model.Membership;
import de.thm.arsnova.connector.model.UserRole;
import de.thm.arsnova.connector.web.ConnectorController;

public class ConnectorControllerTest {

	private MockMvc mockMvc;
	
	@InjectMocks
	ConnectorController controller;
	
	@Mock
	ConnectorService service;
	
	@Before
	public void setup() {
    	MockitoAnnotations.initMocks(this);
    	mockMvc = standaloneSetup(controller).build();
    
    	Membership membership = new Membership();
    	membership.setMember(true);
    	membership.setUserrole(UserRole.TEACHER);
    	
    	Membership fmembership = new Membership();
    	fmembership.setMember(false);
    	
    	when(service.getMembership(anyString(), anyString())).thenReturn(fmembership);
    	when(service.getMembership(anyString(), startsWith("123"))).thenReturn(membership);
    	
    	Courses c1 = new Courses();
    	c1.getCourse().add(new Course());
    	
    	when(service.getCourses(anyString())).thenReturn(new Courses());
    	when(service.getCourses(startsWith("ptsr00"))).thenReturn(c1);
	}
	
	@Test
	public void testShouldReturnEmptyMembership() throws Exception {
		mockMvc.perform(get("/ptsr00/membership/12345678").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void testShouldNotReturnEmptyMembership() throws Exception {
		mockMvc.perform(get("/ptsr00/membership/21435678").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void testShouldReturnEmptyCourses() throws Exception {
		mockMvc.perform(get("/ptsr00/courses").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void testShouldNotReturnEmptyCourses() throws Exception {
		mockMvc.perform(get("/ptsr01/courses").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
}
