package de.thm.arsnova.connector.model.studip_rest;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StudipCoursesWrapper {
	private StudipCoursesInnerWrapper courses;

	public StudipCoursesInnerWrapper getCourses() {
		return courses;
	}

	public void setCourses(StudipCoursesInnerWrapper courses) {
		this.courses = courses;
	}

	public List<StudipCourse> getStudy() {
		return courses.study;
	}

	public void setStudy(List<StudipCourse> study) {
		courses.study = study;
	}

	public List<StudipCourse> getWork() {
		return courses.work;
	}

	public void setWork(List<StudipCourse> work) {
		courses.work = work;
	}

	public static class StudipCoursesInnerWrapper {
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
}
