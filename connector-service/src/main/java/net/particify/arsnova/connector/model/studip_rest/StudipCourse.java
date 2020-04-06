package net.particify.arsnova.connector.model.studip_rest;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StudipCourse {
	private String id;
	private String title;
	private List<String> teachers;
	private List<String> tutors;
	private List<String> students;

	public void setName(String name) {
		this.title = name;
	}

	@JsonProperty("course_id")
	public String getId() {
		return id;
	}

	@JsonProperty("course_id")
	public void setId(String id) {
		this.id = id;
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
