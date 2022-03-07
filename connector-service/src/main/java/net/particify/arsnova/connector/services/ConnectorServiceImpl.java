package net.particify.arsnova.connector.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.particify.arsnova.connector.dao.ConnectorDao;
import net.particify.arsnova.connector.model.Course;
import net.particify.arsnova.connector.model.Courses;
import net.particify.arsnova.connector.model.Membership;

@Service("courseMemberService")
public class ConnectorServiceImpl implements ConnectorService {

	@Autowired
	private ConnectorDao connectorDao;

	@Override
	public Membership getMembership(String username, String courseid) {
		return connectorDao.getMembership(username, courseid);
	}

	@Override
	public Courses getCourses(String username) {
		Courses courses = new Courses();

		for (Course c : connectorDao.getMembersCourses(username)) {
			courses.getCourse().add(c);
		}

		return courses;
	}
}
