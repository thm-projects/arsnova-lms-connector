package de.thm.arsnova.connector.model;

public class IliasFeedback {

	private boolean correctness;
	private String feedback;

	public IliasFeedback(String feedback, boolean correctness) {
		this.correctness = correctness;
		this.feedback = feedback;
	}

	public IliasFeedback() {
		this.correctness = false;
		this.feedback = null;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public boolean isCorrect() {
		return correctness;
	}

	public void setCorrect(boolean isCorrect) {
		this.correctness = isCorrect;
	}
}
