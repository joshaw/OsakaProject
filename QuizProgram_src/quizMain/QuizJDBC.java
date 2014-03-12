package quizMain;

import quizObject.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Properties;

/**
  * Class which contains static methods for developing a connection with the Osaka
  * Quiz database (dbteach2/osakagp), and querying LoginRequest and QuizRequest objects
  */
public class QuizJDBC {

	/**
          * Static method that returns a Connection to the dbteac2/osakagp database
	  * @return Connection
	  */
	public static Connection getConnection() {

		Connection con = null;
		String url = "jdbc:postgresql://dbteach2/osakagp";

		try{
			Class.forName("org.postgresql.Driver").newInstance();
			con = DriverManager.getConnection(url, "sxf796", "dawkins");
		}catch(SQLException e){
			System.out.println(e);
		} catch(Exception e){
			System.out.println(e);
		}

		return con;

	}//end of getConnection

	/*
	 * Main method, used to run some manual tests, can be deleted
	 */
	public static void main(String[] args) {
		Connection con = QuizJDBC.getConnection();
		Quiz q = QuizJDBC.getQuiz(con, 3);
		System.out.println(q);

	}//end of main

	/**
          * Static method that takes a user ID and password of a user as arguments, queries
	  * the database for the existence of the user, and returns a LoginReply object containing the
          * query results
          * @param Connection
 	  * @param String userId
          * @param String password
	  * @return LoginReply
	  */
	public static LoginReply isUser(Connection con, String username, String password)  {

		String sql = "SELECT * FROM users WHERE first_name=? AND password=?";

		//instatiate LoginReply and its instance variables
		boolean isStudent = true;
		boolean loginSuccessful = false;
		String name = "";
		LoginReply lr = null;

		try{
			PreparedStatement ps = con.prepareStatement(sql);
		//	System.out.println("userID is: " + userID + ", password is: " + password); //included for testing
			ps.setString(1, username);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();

			if(rs.next()){ //if the user is found in the database

				if(rs.getString("role").equals("admin")) isStudent = false;

				name = rs.getString("first_name") + " " + rs.getString("last_name");
				loginSuccessful = true;
			}//end of if

			lr = new LoginReply(loginSuccessful, isStudent, name);

		}catch(SQLException e){
			e.printStackTrace();
		}

		return lr;
	}//end of isUser

	/**
 	  * Static method that takes a Connection and quiz id, and returns a Quiz object containing Question objects
	  * @param Connection
	  * @param long quizID
          * @return Quiz object that matches quizID
	  */
	public static Quiz getQuiz(Connection con, long quizID) {

		String sql = "SELECT question_id, question, ans1, ans2, ans3, ans4, correct_ans_id FROM questions, quiz WHERE quiz.quiz_id = ? AND questions.quiz_id = quiz.quiz_id";

		Question[] questionArray = new Question[7]; //array to store the questions
		Quiz quiz = null;
		int i = 0;

		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setLong(1, quizID);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) { //obtain the Question instance variables, store in Question object and add to Question array

				int questionID = rs.getInt("question_id");
				String question = rs.getString("question");
				String ans1 = rs.getString("ans1"); String ans2 = rs.getString("ans2");
				String ans3 = rs.getString("ans3"); String ans4 = rs.getString("ans4");
				String[] answers = new String[4];
				answers[0] = ans1; answers[1] = ans2; answers[2] = ans3; answers[3] = ans4;
				int correctAnswerPos = rs.getInt("correct_ans_id") - 1;

				questionArray[i] = new Question(questionID, question, answers, correctAnswerPos);
				i++;

			}//end of while

			quiz = new Quiz(quizID, questionArray); //create Quiz object containing the questionArray

		} catch(SQLException e) {
			System.out.println(e); return null;
		}//end of try/catch block

		return quiz;

	}//end of getQuiz

}//end of class
