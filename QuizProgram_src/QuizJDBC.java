import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Properties;


public class QuizJDBC {

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
		Quiz q = QuizJDBC.getQuiz(con, 6);
		System.out.println(q);

	}//end of main

	public static LoginReply isUser(Connection con, String userID, String password)  {

		
		String sql = "SELECT * FROM users WHERE user_id=? AND password=?";
		boolean isStudent = true;
		boolean loginSuccessful = false;
		String name = null;
		LoginReply lr = null;		

		try{
			PreparedStatement ps = con.prepareStatement(sql);
			System.out.println("userID is: " + userID + ", password is: " + password);
			ps.setString(1, userID);
			ps.setString(2, password);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()){
				
				if(rs.getString("role").equals("admin")) isStudent = false; 
				name = rs.getString("first_name") + " " + rs.getString("last_name");
				loginSuccessful = true;	
			}//end of if

			lr = new LoginReply(loginSuccessful, isStudent, name);
			System.out.println(lr);
			
		}catch(SQLException e){
			System.out.println(e); 
		}
		
		return lr;
	}//end of isUser


	public static Quiz getQuiz(Connection con, long quizID) {

		String sql = "SELECT question, ans1, ans2, ans3, ans4, correct_ans_id FROM questions, quiz WHERE quiz.quiz_id = ? AND questions.quiz_id = quiz.quiz_id";

		
		Question[] questionArray = new Question[7];
		int i = 0; 
		

		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setLong(1, quizID);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				
				String question = rs.getString("question");
				String ans1 = rs.getString("ans1"); String ans2 = rs.getString("ans2"); 
				String ans3 = rs.getString("ans3"); String ans4 = rs.getString("ans4"); 
				String[] answers = new String[4];
				answers[0] = ans1; answers[1] = ans2; answers[2] = ans3; answers[3] = ans4;
				int correctAnswerPos = rs.getInt("correct_ans_id") - 1;

				questionArray[i] = new Question(question, answers, correctAnswerPos);
				i++;

			}//end of while

			Quiz quiz = new Quiz(questionArray);
			return quiz;			

		} catch(SQLException e) {
			System.out.println(e); return null;
		}//end of try/catch block	

	}//end of getQuiz

}//end of class

