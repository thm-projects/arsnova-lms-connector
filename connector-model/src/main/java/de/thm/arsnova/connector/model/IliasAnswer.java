package de.thm.arsnova.connector.model;

public class IliasAnswer {
	private String text;
	private int points;

	public IliasAnswer(String text, int points) {
		this.text = text;
		this.points = points;
	}

	public IliasAnswer() {}

	public String getText() {
		return text;
	}

	public int getPoints() {
		return points;
	}
}
