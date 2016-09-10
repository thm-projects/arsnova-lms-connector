package de.thm.arsnova.connector.model.moodle_rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MoodleCourse {
	
	private String id;
	private String shortname;
	private String fullname;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getShortname() {
		return shortname;
	}


	public void setShortname(String shortname) {
		this.shortname = shortname;
	}


	public String getFullname() {
		return fullname;
	}


	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
}
