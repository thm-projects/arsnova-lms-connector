package de.thm.arsnova.connector.moodle.services;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thm.arsnova.connector.moodle.dao.CourseDao;
import de.thm.arsnova.connector.moodle.model.MdlUser;

@Service("courseMemberService")
public class CourseMemberServiceImpl implements CourseMemberService {

	@Autowired
	private CourseDao courseDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.thm.arsnova.connector.moodle.CourseMemberService#ismember(java.lang
	 * .String, int)
	 */
	@GET
	@Path("/{username}/ismemberof/{courseid}")
	@Produces("application/json")
	public boolean ismember(@PathParam("username") String username, @PathParam("courseid") String courseid) {
		List<MdlUser> users = courseDao.getCourseUsers(courseid);

		if (users != null) {
			for (MdlUser user : users) {
				if (username.equals(user.getUsername())) {
					return true;
				}
			}
		}

		return false;
	}

}
