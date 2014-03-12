package quizObject;

import java.util.Observable;

/**
 * LoginModel
 * Model for Login GUI
 * 
 * @author Benjamin Vrispin
 * @version 20140306
 */
public class LoginModel extends Observable {

	private String username;
	private char[] password;
	
	/**
	 * Constructor (empty)
	 */
	public LoginModel() {
		username = "";
		password = new char[0];
	}
	
	/**
	 * Getter for username
	 * @return user name as string
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Setter for username
	 * @param username as string
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Getter for password
	 * @return password as char[]
	 */
	public char[] getPassword() {
		return password;
	}
	
	/**
	 * Setter for password
	 * @param password as char[]
	 */
	public void setPassword(char[] password) {
		this.password = password;
	}

	/**
	 * login
	 * 
	 * Converts the char[] password to string password and hashes.
	 * Then creates LoginRequest object based on the user name and hashed password string.
	 * 
	 * @return LoginRequest
	 */
	public LoginRequest login() {
		
		String passwordString = "";
		int passwordHash;
		
		// iterates through the password char[] and creates a password string
		for (int i = 0; i < password.length; i++) {
			passwordString = passwordString + password[i];
		}
		
		passwordHash = passwordString.hashCode();
		return new LoginRequest(username, passwordHash);
		
	}
	
	
}
