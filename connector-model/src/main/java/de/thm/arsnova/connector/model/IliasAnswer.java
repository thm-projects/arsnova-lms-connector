package de.thm.arsnova.connector.model;

public class IliasAnswer {
	private final String text;
	private final int points;

	public IliasAnswer(String text, int points) {
		this.text = text;
		this.points = points;
	}

	public String getText() {
		return text;
	}

	public int getPoints() {
		return points;
	}
}
