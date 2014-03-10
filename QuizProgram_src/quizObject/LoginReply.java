/** Returns the status of a login attempt back to the client.
 *
 * @author jaw097
 * @version 20140307
 */


import java.io.Serializable;

public class LoginReply implements Serializable {

	private static final long serialVersionUID = 42L;

	private boolean loginSuccessful = false;
	private boolean isStudent = true;
	private String name;

	public LoginReply(boolean loginSuccessful, boolean isStudent, String name) {
		this.loginSuccessful = loginSuccessful;
		this.isStudent = isStudent;
		this.name = name;
	}

	public boolean isSuccessful() {
		return loginSuccessful;
	}

	public boolean isStudent() {
		return isStudent;
	}
	
	public String toString() {
		
		return "isSuuccessful: " + loginSuccessful + ", isStudent: " + isStudent + ", Name: " + name;
		
	}//end of toString

	
	@Override
	public boolean equals(Object o){
		
		if(o instanceof LoginReply){
			if(this.loginSuccessful == ((LoginReply) o).loginSuccessful 
					&& this.isStudent == ((LoginReply) o).isStudent && this.name.equals(((LoginReply) o).name)) {
				return true;
			}
		}
		return false;
	}//end of equals
}
