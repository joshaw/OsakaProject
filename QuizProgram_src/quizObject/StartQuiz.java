/** Class sent by admin to signal to server that the quiz is to start.
 * Holds information about when the quiz was started.
 *
 * @author jaw097
 * @version 20140303
 */
package quizObject;

import java.io.Serializable;

public class StartQuiz implements Serializable {

	public static final long serialVersionUID = 42L;

	private long quizStartTime;

	public StartQuiz() {
		quizStartTime = System.currentTimeMillis();
	}

	public long getQuizStartTime() {
		return quizStartTime;
	}

}
