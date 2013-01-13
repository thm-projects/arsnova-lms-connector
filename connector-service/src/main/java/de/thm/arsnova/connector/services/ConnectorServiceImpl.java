package de.thm.arsnova.connector.services;

import java.util.List;

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
	public Membership ismember(@PathParam("username") String username, @PathParam("courseid") String courseid) {
		List<String> users = connectorDao.getCourseUsers(courseid);
		Membership membership = new Membership();
		membership.setCourseid(courseid);

		if (users != null) {
			for (String user : users) {
				if (username.equals(user)) {
					membership.setIsmember(true);
					return membership;
				}
			}
		}

		membership.setIsmember(false);
		return membership;
	}

	@GET
	@Path("/{username}/courses")
	@Produces("application/xml")
	public Courses getCourses(@PathParam("username") String username) {
		Courses courses = new Courses();

		for (Course c : connectorDao.getTeachersCourses(username)) {
			courses.getCourse().add(c);
		}

		return courses;
	}
}
