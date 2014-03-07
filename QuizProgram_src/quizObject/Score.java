package quizObject;

/**
 * Class: Score
 * Purpose: allows result of an AnswerResponse to be packaged and sent back to the client
 */
import java.io.Serializable;

public class Score implements Serializable {

	public static final long serialVersionUID = 42L;

	private int mark;

	public Score(int mark){
		this.mark = mark;
	}

	int getMark(){
		return mark;
	}
}
