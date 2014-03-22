package quizMain;

import quizObject.*;

import java.sql.*;
import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Server class for the interactive quiz game
 * @author sxf796 - server setup, creation and management of client connections
 * @author rjs305 - liaison between the server and the client after the
 * connection has been made to provide quiz functionality
 */
public class QuizServer {

	private static final int PORT = 9010;
	private static final int WCPORT = 9009;
	private ServerSocket listener;
	private ServerSocket wcListener;
	private Socket clientSocket;
	private boolean quizReady = false;
	private QuizTime qt;
	private static int displayQuestionNumber = 0;
	private Quiz quiz;
	private ArrayList<ClientThread> clientArrayList = new ArrayList<ClientThread>();
	private ArrayList<ClientThread> studentArrayList = new ArrayList<ClientThread>();
	private static Connection con;
	private ArrayList<Score> allServerScores;
	private static Integer usersAnswered = 0;
	/**
	 * Starts the server
	 */
	public boolean ServerMain() throws Exception {

		//create a connection to the database
		con = QuizJDBC.getConnection();

		// initialise the server scores ArrayList
		allServerScores = new ArrayList<Score>();

		//initialise the ServerSocket with PORT
		try {
			listener = new ServerSocket(PORT);
		} catch (IOException ioe){
			System.out.println("Port unavailable - server may already be on.");
			return false;
		}
		//end of try/catch block

		try{
			
			System.out.println("Server is running.");
			
			while(true){

				//wait for clients to connect, accept and start new ClientThread
				clientSocket = listener.accept();
				clientArrayList.add(new ClientThread(clientSocket));
				System.out.println("Number of connected users is " + clientArrayList.size());
				clientArrayList.get(clientArrayList.size()-1).start();

			}//end of while

		} catch(IOException ioe){
			/* when would this be thrown? write in when and why here, then
			 * include a way of handling it at a later point - decide this with
			 * the group */
			System.out.println(ioe);
		} finally{
			clientSocket.close();
		}//end of try/catch/finally block
		
		System.out.println("Server shutting down.");
		return true;
	}//end of startServer method

	//
	//
	//
	//
	//
	//*************************************************************************
	//                              INNER CLASS                               *
	//************************************************************************/
	/**
	 * Inner class that is created when a new client connects to the server
	 *
	 */
	private class ClientThread extends Thread {

		private Socket clientSocket;
		private boolean isStudent = true;

		//instance variables which allow communication between Server and Client
		private ObjectOutputStream objectOutputStream;
		private ObjectInputStream objectInputStream;
		private DataInputStream dataInputStream;
		private PrintStream printStream;
		private String username;
		private boolean lastThrough = false;

		/**
		 * Constructor for the client thread
		 * @param clientSocket
		 */
		public ClientThread(Socket clientSocket){
			this.clientSocket = clientSocket;
		}//end of constructor

		/**
		 * Handles the client thread
		 */
		public void run(){
			try{
				//input and output streams for server to communicate with client
				objectOutputStream =
					new ObjectOutputStream(clientSocket.getOutputStream());
				objectInputStream =
					new ObjectInputStream(clientSocket.getInputStream());
				dataInputStream =
					new DataInputStream(clientSocket.getInputStream());
				printStream = new PrintStream(clientSocket.getOutputStream());

				boolean userExists = false;

				/*while loop is designed to run until a successful login has
				 *been received */
				try{
					while(!userExists) {
						Object newObj = objectInputStream.readObject();
						if(newObj instanceof LoginRequest){
							LoginRequest loginRequest = (LoginRequest) newObj;

							//Obtain the user name and hashed password from the
							//LoginRequest Object
							username = loginRequest.getUsername();

							//stored in database as a string
							String password = loginRequest.getPasswordHash();

							//query database for login details, create a login
							//reply object and return it
							LoginReply lr =
								QuizJDBC.isUser(con, username, password);

							//update status student vs admin
							isStudent = lr.isStudent();

							//send the loginReply to the client
							objectOutputStream.writeObject(lr);

							//if the user exists exit loop and move onto quiz
							if(lr.isSuccessful()) {
								userExists = true;
							} else {
								objectOutputStream.writeObject(
										new LoginReply(false));
							}

						}//end of if

					}//end of while
				} catch(EOFException e){
					System.out.println("Client has not logged in.");
				} catch(ClassNotFoundException e){
					System.out.println(e);
				}//end of try/catch block

				// User has logged in
				if (userExists) {

					/*
					 * The following try/catch block receives a QuizRequest
					 * Object from client (admin) and retrieves the required
					 * quiz from the database
					 */
					try{
						
//						clientSocket = listener.accept();
//						clientArrayList.add(new ClientThread(clientSocket));
						
						if(!isStudent) {
							Object object;
							object = objectInputStream.readObject();
							if(object instanceof QuizRequest){
								QuizRequest qr = (QuizRequest) object;
								//get quiz from database and set to currentQuiz
								System.out.println("QUIZ ID: "+ qr.getQuizID());
								setQuiz(QuizJDBC.getQuiz(con, qr.getQuizID()));
								//setQuiz(QuizJDBC.getQuiz(con, 3));
								//System.out.println("JDBC QUIZ FIRST QUESTION ON SERVER: "+getQuiz().getQuestion(0));
								qt = new QuizTime();
								setQuizReady(true);
								qt.start();
							}
						} else { // If user is a student put a blank entry into scores HashMap
							synchronized(studentArrayList){
								studentArrayList.add(this);
							}
							synchronized(allServerScores){
								allServerScores.add(new Score(username, 0, -1));
							}
						}
						// System.out.println("BEFORE QUIZ READY LOOP: is Student: "+isStudent+" quizReady: "+quizReady);
						while(!getQuizReady()){
							sleep(0);
						}

						// System.out.println("STARTING QUIZ isStudent: "+isStudent+" quizReady: "+quizReady);
						if(isStudent){
							startQuizSession(con);
						}
					}catch(Exception e){
						System.out.println(e);
					}//end of try/catch block

				}

				// TODO sent final scores to client
				//after the quiz has finished, close the connections
				System.out.println("SERVER THINKS QUIZ HAS FINISHED - CLOSING CLIENT THREAD STREAMS");
				objectOutputStream.close();
				objectInputStream.close();
				dataInputStream.close();
				printStream.close();

			} catch(IOException ioe){
				System.out.println(ioe);
			} //end of try/catch block

		}//end of run method

		public boolean startQuizSession(Connection con)
			throws Exception {

			Quiz currentQuiz = getQuiz();

			//System.out.println(currentQuiz);

			// Send quiz to client
			objectOutputStream.writeObject(currentQuiz);

			// Wait to give quiz time to transfer
			sleep(50);

			// Start quiz
			objectOutputStream.writeObject(new StartQuiz(studentArrayList.size()));

			// Iterate through each of the quiz questions
			
		
			
			while(getDisplayQuestionNumber() < 10){

				// Initiate each question on the client side
				System.out.println("Current question on Server is: "+getDisplayQuestionNumber());
				System.out.println();
				objectOutputStream.writeObject(new DisplayQuestion(getDisplayQuestionNumber()));
				
				try{
					// Read an object from the stream
					Object object = objectInputStream.readObject();
					
					int user = 0;
					
					// Check if the object is an AnswerResponse
					if (object instanceof AnswerResponse) {

						AnswerResponse currentResponse = (AnswerResponse)object;

						// Check if response is correct
						// TO DO: Implement getCorrect in the Question class
						int score;
						if(currentResponse.getResponse() == (currentQuiz.getQuestion(getDisplayQuestionNumber()).getCorrectAnswerPos())){

							score = (int) (10 + (500000 / currentResponse.getResponseTime()));
						// Otherwise, update client with incorrect answer - 0
						} else {
							score = -100;
						}
						// Updates allScores ArrayList and sends to client
						
						
						synchronized(allServerScores){
							ForLoop:
							for (int k = 0; k < allServerScores.size(); k++){
								
								if (allServerScores.get(k).getUsername().equals(username)){
									
									user = k;
									
									//System.out.println("Mark before update: "+ allServerScores.get(k).getMark());
									//System.out.println("Adding Score: "+score);
									
									allServerScores.get(k).addMark(score);
									allServerScores.get(k).setCurrentQuestion(getDisplayQuestionNumber());
									
									//System.out.println("Mark after update: "+ allServerScores.get(k).getMark());
									
									break ForLoop;
								} 
							}
	
							// Clone Arraylist
							ArrayList<Score> allScores = new ArrayList<Score>();
							for(int i = 0; i < allServerScores.size(); i++) {
								allScores.add(allServerScores.get(i).deepClone());
							}
							
							// Sends scores to client
							//objectOutputStream.writeObject(allScores);
							
							// Sends scores to clients
							sendObjectToAll(allScores); 
							
						}
						
						// Update number of users answered
						synchronized(usersAnswered){
							incrementUsersAnswered();
							System.out.println("Users answered updated to: "+ usersAnswered);
						}
							
						// Last user 'through the gate' increments the question number
						synchronized(usersAnswered){
							if(usersAnswered == allServerScores.size()){
								lastThrough = true;
								incrementDisplayQuestionNumber();
								setUsersAnswered(0);
							}
						}
					
						// Wait until all users have answered before sending next display question

						waitForOthers:
						if(!lastThrough){
							boolean wait = true;
							while(wait){	
								synchronized(usersAnswered){
									wait = (getUsersAnswered() < allServerScores.size());
								}
							}	
						}
						if(lastThrough){lastThrough = false;}
					}	
				} catch(ClassNotFoundException e){
					e.printStackTrace();
				} catch(SocketException e){
					e.printStackTrace();
					System.out.println("Client has disconnected.");
				} catch(EOFException e){
					System.out.println("Client has disconnected.");
					return false;
				}
			}
			return true;

		}

		public void sendObject(Object object) {
			try {
				objectOutputStream.writeObject(object);
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
	}//end of ClientThread class
	//*************************************************************************
	//                       END OF INNER CLASS CLIENTTHREAD                  *
	//************************************************************************/

	
	//*************************************************************************
	//                           INNER CLASS QUIZ TIME                        *
	//************************************************************************/
	/**
	 * Inner class that is created when quiz starts to regulate timing
	 *
	 */
	private class QuizTime extends Thread {
		
		long startTime;
		
		public QuizTime(){}
		
		public void run(){
			//System.out.println("QUIZ TIME IS RUNNING");
			
			QuestionLoop:
				
				//for(int i=0; i<10; i++) {
				
			while(getDisplayQuestionNumber() < 10){
				startTime = System.currentTimeMillis();
				
				while(System.currentTimeMillis() - startTime < 10000){ // controls time length of question
					System.out.print("");
				}
				
				// Stops quiz entrants starting after first question
				if(getDisplayQuestionNumber() > 0){
					setQuizReady(false);
				}
				
				synchronized(allServerScores){				
					if(allServerScores.size() == 0){ // All Clients have disconnected during the quiz
						break QuestionLoop;
					}
				}
			}
		
			// Quiz cleanup COMMENTING THIS OUT DOESNT DO ANYTHING??? - (Rowan - this was meant to reset quiz to start state once over)
			//setDisplayQuestionNumber(1);
		
		
		}
	}//end of QuizTime class
	
	//*************************************************************************
	//                           END OF INNER CLASS                           *
	//************************************************************************/
	//
	//
	//
	//
	
	public Integer getUsersAnswered(){
		synchronized(usersAnswered){
			return usersAnswered;
		}
	}
	
	public void setUsersAnswered(Integer i){
		synchronized(usersAnswered){
			usersAnswered = i;
		}
	}
	
	public void incrementUsersAnswered(){
		synchronized(usersAnswered){
			usersAnswered++;
		}
	}
	
	public int getDisplayQuestionNumber(){
		return displayQuestionNumber;
	}
	
	public void setDisplayQuestionNumber(int i){
		displayQuestionNumber = i;
	}
	
	public void incrementDisplayQuestionNumber(){
		displayQuestionNumber++;
	}
	
	public void sendObjectToAll(Object object) {
		synchronized(studentArrayList){
			for(ClientThread thread: studentArrayList) {
				try {
					thread.sendObject(object);
				} catch(Exception e){ }
			}
		}
	}

	public boolean getQuizReady() {
		// System.out.println("getQuizReady");
		return quizReady;
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuizReady(boolean ready) {
		//System.out.println("setQuizReady");
		quizReady = ready;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	public static void main(String[] args) throws Exception {
		QuizServer quizServer = new QuizServer();
		quizServer.ServerMain();
	}

}//end of QuizServer