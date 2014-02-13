package de.thm.arsnova.connector.core;

public class IliasFeedback {

	private final boolean correctness;
	private final String feedback;

	public IliasFeedback(String feedback, boolean correctness) {
		this.correctness = correctness;
		this.feedback = feedback;
	}

	public String getFeedback() {
		return feedback;
	}

	public boolean isCorrect() {
		return correctness;
	}
}
