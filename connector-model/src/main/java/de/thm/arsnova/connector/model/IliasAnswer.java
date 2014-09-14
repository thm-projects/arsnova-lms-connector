package de.thm.arsnova.connector.model;

public class IliasAnswer {
	private String text;
	private double points;
	private double pointsUnchecked;

	public IliasAnswer(String text, double points, double pointsUnchecked) {
		this.text = text;
		this.points = points;
		this.pointsUnchecked = pointsUnchecked;
	}

	public String getText() {
		return text;
	}

	public double getPoints() {
		return points;
	}
	
	public double getPointsUnchecked() {
		return pointsUnchecked;
	}
}
