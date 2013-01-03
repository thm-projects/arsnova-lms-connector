package de.thm.arsnova.connector.moodle.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import de.thm.arsnova.connector.moodle.model.MdlCourse;
import de.thm.arsnova.connector.moodle.model.MdlUser;

@Component
public class CourseDaoImpl implements CourseDao {
	
	@PersistenceContext(unitName = "moodle-connector")
	private EntityManager em;

	public List<MdlUser> getCourseUsers(String courseid) {
		MdlCourse course = em.createNamedQuery("getCourse", MdlCourse.class)
				.setParameter("courseid", courseid).getSingleResult();
		if (course == null) {
			return null;
		}
				
		return course.getMdlUsers();
	}
}
