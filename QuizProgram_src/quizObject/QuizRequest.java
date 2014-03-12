package quizObject;

import java.io.Serializable;

public class QuizRequest implements Serializable {

	public static final long serialVersionUID = 42L;

	private long quizID;

	public QuizRequest(long quizID){
		this.quizID = quizID;

	}//end of constructor

	public long getQuizID(){
		return quizID;
	}

}//end of class
