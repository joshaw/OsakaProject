package quizObject;

/**
 * Class: Score
 * Purpose: allows result of an AnswerResponse to be packaged and sent back to the client
 */
import java.io.Serializable;

public class Score implements Serializable {

	public static final long serialVersionUID = 42L;

	private String username;
	private int mark;
	
	public Score(String username, int mark){
		this.username = username;
		this.mark = mark;
	}

	int getMark(){
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	
	
}
