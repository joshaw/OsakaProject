package quizGUI;

import java.util.Observable;

import quizObject.*;

/**
 * QuizModel
 * Model for Quiz GUI
 * 
 * @author Benjamin Crispin
 * @version 20140306
 */
public class QuizModel extends Observable {

	private String username;
	private char[] password;
	boolean isStudent;
	
	Quiz[] quizArray;
	long currentQuizID;
	int currentQuestionNumber;
	
	
	/**
	 * Constructor (empty - the model is filled up as each GUI screen is presented)
	 */
	public QuizModel() {
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
	 * Getter for quizArray
	 * @return quizArray as Quiz[]
	 */
	public Quiz[] getQuizArray() {
		return quizArray;
	}
	
	/**
	 * Setter for quizArray
	 * @param quizArray as Quiz[]
	 */
	public void setQuizArray(Quiz[] quizArray) {
		this.quizArray = quizArray;
	}

	/**
	 * Getter for currentQuizID
	 * @return currentQuizID as int
	 */
	public long getCurrentQuizID() {
		return currentQuizID;
	}

	/**
	 * Setter for currentQuizID
	 * @param currentQuizID as int
	 */
	public void setCurrentQuizID(long currentQuizID) {
		this.currentQuizID = currentQuizID;
	}

	/**
	 * Getter for currentQuestionNumber
	 * @return currentQuestionNumber as int
	 */
	public int getCurrentQuestionNumber() {
		return currentQuestionNumber;
	}

	/**
	 * Setter for currentQuestionNumber
	 * @param currentQuestionNumber as int
	 */
	public void setCurrentQuestionNumber(int currentQuestionNumber) {
		this.currentQuestionNumber = currentQuestionNumber;
	}
	
	/**
	 * Getter for isStudent
	 * @return isStudent as boolean
	 */
	public boolean isStudent() {
		return isStudent;
	}

	/**
	 * Setter for isStudent
	 * @param isStudent as boolean
	 */
	public void setStudent(boolean isStudent) {
		this.isStudent = isStudent;
	}

	/**
	 * requestLogin - action for the loginButton in the LoginFrame
	 * 
	 * Converts the char[] password to string password and hashes.
	 * Then creates LoginRequest object based on the user name and hashed password string.
	 * 
	 * @return LoginRequest
	 */
	public LoginRequest requestLogin() {
		
		String passwordString = "";
		int passwordHash;
		
		// iterates through the password char[] and creates a password string
		for (int i = 0; i < password.length; i++) {
			passwordString = passwordString + password[i];
		}
		
		passwordHash = passwordString.hashCode();
		return new LoginRequest(username, passwordHash);
		
	}
	 
	/**
	 * login
	 * 
	 * @param reply - the LoginReply from the server.
	 */
	public void login(LoginReply reply) {
		
		if(reply.isSuccessful()) {
			setStudent(reply.isStudent());
			setPassword(new char[0]); // stop storing the password
			setQuizArray(reply.getQuizes());
			// Move to the next screen (admin/student)
		} else {
			// reset the login info for a re-attempt
			setUsername("");
			setPassword(new char[0]);
		}
		
	}
	
	/**
	 * adminStart - action for the start button in the admin frame.
	 * creates and returns a StartQuiz object
	 * @return StartQuiz
	 */
	public StartQuiz adminStart() {
		StartQuiz quiz = new StartQuiz(currentQuizID);
		return quiz; // The start quiz object to be sent to the server...
	}
	
	
	
}
