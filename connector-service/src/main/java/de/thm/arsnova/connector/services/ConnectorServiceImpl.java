package de.thm.arsnova.connector.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thm.arsnova.connector.dao.ConnectorDao;
import de.thm.arsnova.connector.model.Course;
import de.thm.arsnova.connector.model.Courses;
import de.thm.arsnova.connector.model.Membership;

@Service("courseMemberService")
public class ConnectorServiceImpl implements ConnectorService {

	@Autowired
	private ConnectorDao connectorDao;

	@GET
	@Path("/{username}/membership/{courseid}")
	@Produces("application/xml")
	public Membership getMembership(@PathParam("username") String username, @PathParam("courseid") String courseid) {
		return connectorDao.getMembership(username, courseid);
	}

	@GET
	@Path("/{username}/courses")
	@Produces("application/xml")
	public Courses getCourses(@PathParam("username") String username) {
		Courses courses = new Courses();

		for (Course c : connectorDao.getMembersCourses(username)) {
			courses.getCourse().add(c);
		}

		return courses;
	}
}
