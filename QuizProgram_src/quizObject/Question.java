package quizObject;

import java.util.ArrayList;
import java.io.Serializable;

public class Question implements Serializable {

	public static final long serialVersionUID = 42L;

	private String question;
	private String[] answers = new String[4];
	private int correctNumber;

	public Question(boolean test) {

		/* Create example quiestion with several example answers. This
		 * information should come from the database. */
		question = "This is the question.";
		for (int i = 0; i < 4; i++) {
			answers[i] = "Answer number " + i;
		}
	}

	public Question() {}

	public Question(String question) {
		this.question = question;
	}

	public Question(String question, String[] answers, int correctNumber) {
		this.question = question;
		this.answers = answers;
		this.correctNumber = correctNumber;
	}

	public void setQuestion(String Question) {
		 this.question = question;
	}

	public void setAnswer(int i, String answer) {
		this.answers[i] = answer;
	}

	public String getQuestion() {
		return question;
	}

	public String getAnswer(int i) {
		return answers[i];
	}

	public String[] getAnswers() {
		return answers;
	}

	public int getCorrect() {
		return correctNumber;
	}

	public String toString() {
		String answerString = "";
		for (int i=0; i<4; i++) {
			answerString += "A" + i + ": " + answers[i] + "\n";
		}
		return "Q: " + question + "\n" + answerString;
	}

}
