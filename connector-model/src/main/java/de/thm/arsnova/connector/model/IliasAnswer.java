package de.thm.arsnova.connector.model;

public class IliasAnswer {
	private String text;
	private double points;
	private double points_unchecked;

	public IliasAnswer(String text, double points, double points_unchecked) {
		this.text = text;
		this.points = points;
		this.points_unchecked = points_unchecked;
	}

	public String getText() {
		return text;
	}

	public double getPoints() {
		return points;
	}
	
	public double getPointsUnchecked() {
		return points_unchecked;
	}
}
