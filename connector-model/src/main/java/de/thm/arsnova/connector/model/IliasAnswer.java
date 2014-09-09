package de.thm.arsnova.connector.model;

public class IliasAnswer {
	private String text;
	private double points;

	public IliasAnswer(String text, double points) {
		this.text = text;
		this.points = points;
	}

	public IliasAnswer() {}

	public String getText() {
		return text;
	}

	public double getPoints() {
		return points;
	}
}
