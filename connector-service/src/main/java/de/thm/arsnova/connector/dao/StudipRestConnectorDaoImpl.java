package de.thm.arsnova.connector.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import de.thm.arsnova.connector.model.Course;
import de.thm.arsnova.connector.model.Membership;
import de.thm.arsnova.connector.model.UserRole;

public class StudipRestConnectorDaoImpl implements ConnectorDao {

	private static final String TYPE = "studip";

	private static final String USER_MEMBERSHIP_URI = "/user/{userid}/courses";
	private static final String USER_SERACH_URI = "/users?needle={username}";

	private final RestTemplate restTemplate = new RestTemplate();

	@Override
	public List<String> getCourseUsers(final String courseid) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Membership getMembership(final String username, final String courseid) {
		Membership membership = new Membership();
		ResponseEntity<StudipCoursesContainer> courseContainer = restTemplate.exchange(
			USER_MEMBERSHIP_URI,
			HttpMethod.GET, null,
			StudipCoursesContainer.class,
			getUserByName(username).getUserId()
		);

		for (StudipCourse course : courseContainer.getBody().study) {
			if (courseid.equals(course.getCourseId())) {
				membership.setMember(true);
				membership.setUserrole(UserRole.MEMBER);

				return membership;
			}
		}
		for (StudipCourse course : courseContainer.getBody().work) {
			if (courseid.equals(course.getCourseId())) {
				membership.setMember(true);
				membership.setUserrole(UserRole.TEACHER);

				return membership;
			}
		}
		membership.setMember(false);

		return membership;
	}

	@Override
	public List<Course> getMembersCourses(final String username) {
		List<Course> courses = new ArrayList<>();
		ResponseEntity<StudipCoursesContainer> courseContainer = restTemplate.exchange(
			USER_MEMBERSHIP_URI,
			HttpMethod.GET, null,
			StudipCoursesContainer.class,
			getUserByName(username).getUserId()
		);

		for (StudipCourse sipCourse : courseContainer.getBody().study) {
			courses.add(buildCourse(sipCourse, UserRole.MEMBER));
		}
		for (StudipCourse sipCourse : courseContainer.getBody().work) {
			courses.add(buildCourse(sipCourse, UserRole.TEACHER));
		}

		return courses;
	}

	private StudipUser getUserByName(String username) {
		ResponseEntity<StudipUserContainer> userContainer = restTemplate.exchange(
			USER_SERACH_URI,
			HttpMethod.GET, null,
			StudipUserContainer.class,
			username
		);

		return userContainer.getBody().getUser();
	}

	private Course buildCourse(StudipCourse sipCourse, UserRole role) {
		Membership membership = new Membership();
		membership.setMember(true);
		membership.setUserrole(role);

		Course course = new Course();
		course.setId(sipCourse.getCourseId());
		course.setFullname(sipCourse.getTitle());
		course.setShortname(sipCourse.getTitle());
		course.setType(TYPE);
		course.setMembership(membership);

		return course;
	}

	class StudipCoursesContainer {
		private List<StudipCourse> study;
		private List<StudipCourse> work;

		public List<StudipCourse> getStudy() {
			return study;
		}

		public void setStudy(List<StudipCourse> study) {
			this.study = study;
		}

		public List<StudipCourse> getWork() {
			return work;
		}

		public void setWork(List<StudipCourse> work) {
			this.work = work;
		}
	}

	class StudipCourse {
		private String courseId;
		private String title;
		private List<String> teachers;
		private List<String> tutors;
		private List<String> students;

		public void setName(String name) {
			this.title = name;
		}

		public String getCourseId() {
			return courseId;
		}

		public void setCourseId(String courseId) {
			this.courseId = courseId;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public List<String> getTeachers() {
			return teachers;
		}

		public void setTeachers(List<String> teachers) {
			this.teachers = teachers;
		}

		public List<String> getTutors() {
			return tutors;
		}

		public void setTutors(List<String> tutors) {
			this.tutors = tutors;
		}

		public List<String> getStudents() {
			return students;
		}

		public void setStudents(List<String> students) {
			this.students = students;
		}
	}

	class StudipUserContainer {
		private StudipUser user;

		public StudipUser getUser() {
			return user;
		}

		public void setUser(StudipUser user) {
			this.user = user;
		}
	}

	class StudipUser {
		private String userId;
		private String username;

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}
	}
}
