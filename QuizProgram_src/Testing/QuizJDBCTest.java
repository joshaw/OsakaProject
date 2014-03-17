package Testing;

import static org.junit.Assert.*;
import java.sql.Connection;
import org.junit.Test;

import quizObject.*;
import quizMain.*;


/**
 * Contains test cases for the QuizJDBC class
 * @author sxf796
 *
 */
public class QuizJDBCTest {
	
	Connection con = QuizJDBC.getConnection();
	
	/*
	 * Test for a student successfully logging in, user_id = 1, password = 25.
	 * A successful request will result in a LoginReply object being created
	 */
	@Test
	public void test1(){
		
		LoginReply lr = QuizJDBC.isUser(con, "1", "25");
		
		LoginReply expected = new LoginReply(true, true, "Mary Ande");
		
		assertEquals("failure - returned object not as expected", lr, expected);
		
	}//end of test1
	
	/*
	 * Test for an administrator successfully logging in
	 */
	@Test
	public void test2(){
		
		LoginReply lr = QuizJDBC.isUser(con, "11", "11");
		
		LoginReply expected = new LoginReply(true, false, "George Kiff");
		
		assertEquals("failure - returned object not as expected", lr, expected);		
	}//end of test2
	
	/*
	 * Test for unsuccessful login - user name doesn't exist
	 */
	@Test
	public void test3(){
		
		LoginReply lr = QuizJDBC.isUser(con, "41", "1");
		
		LoginReply expected = new LoginReply(false, true, "");
		
		assertEquals("failure - returned object not as expected", lr, expected);		
	}//end of test3
	
	/*
	 * Test for unsuccessful login - incorrect password
	 */
	@Test
	public void test4(){
		
		LoginReply lr = QuizJDBC.isUser(con, "1", "11"); //user 1 has "25" as a password
		
		LoginReply expected = new LoginReply(false, true, "");
		
		assertEquals("failure - returned object not as expected", lr, expected);
		
	}//end of test4
	
	/*
	 * Test for creating a Quiz object through database
	 */
	@Test
	public void test5(){
		
		Quiz q = QuizJDBC.getQuiz(con, 3);
		
		//create the questions that make up Quiz number 3
		String[] answers1 = {"Chris Froome", "Lizzie Armitstead", "Matt Crampton", "Kyle Evans"};
		Question q1 = new Question(3, "Which British cyclist won the 100th edition of the Tour de France", answers1, 0);
		
		String[] answers2 = {"11", "14", "5", "6"};
		Question q2 = new Question(4, "How many players are there in a basketball team", answers2, 5);
		
		Question[] questions = {q1, q2};
		Quiz expected = new Quiz(3, questions);
		
		assertEquals("failure - quizzes don't match", q, expected);
	}//end of test5
	
	
}//end of QuizJDBCTest
