package quizObject;

import java.util.ArrayList;
import java.io.Serializable;

public class Question implements Serializable {

	public static final long serialVersionUID = 42L;

	private String question;
	private String[] answers = new String[4];
	private int correctAnswerPos; //the position in the array of answers that is the correct one
	private int questionID;
	private int timeLimit = 10;

	public Question(boolean test) {

		/* Create example question with several example answers. This
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

	public Question(int questionID, String question, String[] answers, int correctPos) {
		this.question = question;
		this.answers = answers;
		this.correctAnswerPos = correctPos;
		this.questionID = questionID;
	}

	public void setQuestion(String question) {
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

	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	public String toString() {
		String answerString = "";
		for (int i=0; i<4; i++) {
			answerString += "A" + i + ": " + answers[i] + "\n";
		}
		return "Q: " + question + "\n" + answerString + "\nCorrectAns: " + answers[correctAnswerPos] +"\n";
	}

	public boolean equals(Object o){

		if(o instanceof Question){

			if(this.questionID == (((Question) o).questionID)) return true;

		}

		return false;
	}//end of equals

}
