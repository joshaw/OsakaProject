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

	private String username;
	private String passwordHash;

	public LoginRequest(String username, String password) {
		this.username = username;
		// this.passwordHash = password.hashCode();
		this.passwordHash = password;
		//System.out.println("Login username: " + username + "\npassword: " + passwordHash);
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	@Override
	public String toString() {
		return "Username: " + username + "\nPassHash: " + passwordHash;
	}

}
