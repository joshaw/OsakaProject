/** LoginRequest object is sent from client to server with username and
 * password. If the details exsist in the database, the user is logged into the
 * system.
 *
 * @author jaw097
 * @version 20140228
 */
package quizObject;

import java.io.Serializable;

public class LoginRequest implements Serializable {

	private static final long serialVersionUID = 40L;

	private int username;
	private int passwordHash;

	public LoginRequest(int username, int password) {
		this.username = username;
		// this.passwordHash = password.hashCode();
		this.passwordHash = password;
		System.out.println("Login username: " + username + "\npassword: " + passwordHash);
	}

	public void setUsername(int username) {
		this.username = username;
	}

	public int getUsername() {
		return username;
	}

	public int getPasswordHash() {
		return passwordHash;
	}

	@Override
	public String toString() {
		return "Username: " + username + "\nPassHash: " + passwordHash;
	}

}
