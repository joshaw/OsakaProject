package quizObject;

import java.util.ArrayList;
import java.io.Serializable;

public class Question implements Serializable {

	public static final long serialVersionUID = 42L;

	private String question;
	private String[] answers = new String[4];

	//the position in the array of answers that is the correct one
	private int correctAnswerPos;
	private int questionID;
	private int timeLimit = 10;

	public Question() {}

	public Question(String question) {
		this.question = question;
	}

	public Question(int questionID, String question,
							String[] answers, int correctPos) {
		this.question = question;
		this.answers = answers;
		this.correctAnswerPos = correctPos;
		this.questionID = questionID;
	}

	public Question(int questionID, String question,
			String[] answers, int correctPos, int timeLimit) {
		this.question = question;
		this.answers = answers;
		this.correctAnswerPos = correctPos;
		this.questionID = questionID;
		this.timeLimit = timeLimit;
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

	public int getCorrectAnswerPos() {
		return correctAnswerPos;
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
		return "Q: " + question + "\n" + answerString +
			"\nCorrectAns: " + answers[correctAnswerPos] +"\n";
	}

	public boolean equals(Object o){

		if(o instanceof Question){

			if(this.questionID == (((Question) o).questionID)) return true;

		}

		return false;
	}//end of equals

}
