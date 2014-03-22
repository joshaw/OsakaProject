/** Class sent by admin to signal to server that the quiz is to start.
 *
 * @author jaw097
 * @version 20140303
 */
package quizObject;

import java.io.Serializable;

public class StartQuiz implements Serializable {

	public static final long serialVersionUID = 42L;
	
	private int numberOfParticipants;

	public StartQuiz(int n) {
		numberOfParticipants = n;
	}
	
	public int getNumberOfParticipants(){
		return numberOfParticipants;
	}
	
}
