package de.thm.arsnova.connector.core;

public class IliasAnswer {
	String text;
	int points;
	
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
