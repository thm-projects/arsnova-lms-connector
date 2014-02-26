package de.thm.arsnova.connector.model;

import java.util.List;

public class IliasQuestion {
	private int id;
	private int type;
	private int points;
	private String title;
	private String description;
	private String text;
	private int timestamp;
	private List<IliasAnswer> answers;
	private List<IliasFeedback> feedback;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public int getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	public List<IliasAnswer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<IliasAnswer> answers) {
		this.answers = answers;
	}

	public List<IliasFeedback> getFeedback() {
		return feedback;
	}
	public void setFeedback(List<IliasFeedback> feedback) {
		this.feedback = feedback;
	}
}
