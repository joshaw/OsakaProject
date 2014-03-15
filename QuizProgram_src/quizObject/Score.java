package quizObject;

/**
 * Class: Score
 * Purpose: allows result of an AnswerResponse to be packaged and sent back to the client
 */
import java.io.Serializable;

public class Score implements Serializable, Comparable<Score> {

	public static final long serialVersionUID = 42L;

	private String username;
	private int mark;
	
	public Score(String username, int mark){
		this.username = username;
		this.mark = mark;
	}

	public int getMark(){
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

	/**
	 * compareTo compares two score objects.
	 * Natural ordering is higher marks before lower marks (opposite to int ordering)
	 * If the first mark is equal to the second, will return 0.
	 * If the first mark is greater than the second, will return -1.
	 * If the first mark is less than the second, will return -1.
	 */
	@Override
	public int compareTo(Score score) {
		
		if(mark == score.getMark()) {
			return 0;
		} else if(mark > score.getMark()) {
			return -1;
		} else {
			return 1;
		}
	}
	
}
