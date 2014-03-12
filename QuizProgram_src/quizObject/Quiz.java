/** Quiz object holds an array of question objects, collected from the
 * database, ready to send to the client.
 *
 * @author jaw097
 * @version 20140311
 */
package quizObject;

import java.io.Serializable;

public class Quiz implements Serializable {

	public static final long serialVersionUID = 42L;

	private long quizID;
	private Question[] questions = new Question[7];
	private String name;

	public Quiz() {

		/* Create example quiz object with several question objects. This
		 * information should be retreived from the database. */
		for (int i = 1; i < 6; i++) {
			questions[i] = new Question(true);
		}
		name = "Test";
	}

	public Quiz(long quizID, String name, Question[] questions) {
		this.quizID = quizID;
		this.questions = questions;
		this.name = name;
	}

	public void setQuestions(Question[] questions) {
		this.questions = questions;
	}

	public Question[] getQuestions() {
		return questions;
	}
	
	public String getName() {
		return name;
	}

	public Question getQuestion(int i) {
		try {
			return questions[i];
		} catch(ArrayIndexOutOfBoundsException e){
			System.err.println("Question index out of bounds.");
		}
		return new Question();
	}

	@Override
	public String toString(){

		String returnString = "";
		for(int i=0; i<7; i++) {
			if(questions[i] != null){
			returnString += questions[i].toString() + "\n";
		} else returnString += "";
		}
		return returnString;
	}//end of toString

	@Override
	public boolean equals(Object o){

		if(o instanceof Quiz){
			if(this.quizID == ((Quiz) o).quizID) return true;
		}

		return false;

	}//end of equals method

}
