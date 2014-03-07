/** Returns the status of a login attempt back to the client.
 *
 * @author jaw097
 * @version 20140307
 */
package quizObject;

import java.io.Serializable;

public class LoginReply implements Serializable {

	private static final long serialVersionUID = 42L;

	private boolean loginSuccessful = false;
	private boolean isStudent = true;

	public LoginReply(boolean loginSuccessful, boolean isStudent) {
		this.loginSuccessful = loginSuccessful;
		this.isStudent = isStudent;
	}

	public boolean isSuccessful() {
		return loginSuccessful;
	}

	public boolean isStudent() {
		return isStudent;
	}

}
