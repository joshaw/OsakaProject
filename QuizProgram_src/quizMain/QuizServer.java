package quizMain;

import quizObject.*;

import java.sql.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Time;
import java.util.ArrayList;

/**
 * Server class for the interactive quiz game
 * @author sxf796 - server setup, creation and management of client connections
 * @author rjs305 - liason between the server and the client after the
 * connection has been made to provide quiz functionality
 */
public class QuizServer {

	private static final int PORT = 9000;
	private ServerSocket listener;
	private Socket clientSocket;
	private static boolean quizReady = false;
	private static Quiz quiz;
	private ArrayList<ClientThread> clientArrayList
		= new ArrayList<ClientThread>();

	/**
	 * Starts the server
	 */
	public void ServerMain() throws Exception{

		System.out.println("Server is running");

		//initialise the ServerSocket with PORT
		try {
			listener = new ServerSocket(PORT);
		}catch (IOException ioe){
			System.out.println(ioe);
		}//end of try/catch block

		try{

			while(true){

				//wait for clients to connect, accept and start new ClientThread
				clientSocket = listener.accept();
				clientArrayList.add(new ClientThread(clientSocket));
				System.out.println("Size of arrayList = " + clientArrayList.size());
				clientArrayList.get(clientArrayList.size()-1).start();

			}//end of while

		} catch(IOException ioe){
			/* when would this be throw? write in when and why here, then
			 * include a way of handling it at a later point - decide this with
			 * the group */
			System.out.println(ioe);
		} finally{
			clientSocket.close();
		}//end of try/catch/finally block

	}//end of startServer method

	/*##########################################################################
#                                 INNER CLASS                                 #
##############################################################################*/
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

				//create a connection to the database
				Connection con = QuizJDBC.getConnection();

				boolean userExists = false;

				/*while loop is designed to run until a successful login has
				 *been recieved */
				try{
					while(!userExists) {
						Object newObj = objectInputStream.readObject();
						if(newObj instanceof LoginRequest){
							LoginRequest loginRequest = (LoginRequest) newObj;

							//Obtain the user name and hashed password from the
							//LoginRequest Object
							String username = loginRequest.getUsername();

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

						if(!isStudent) {
							Object object;
							object = objectInputStream.readObject();
							if(object instanceof QuizRequest){
								QuizRequest qr = (QuizRequest) object;
								//get quiz from database and set to currentQuiz
								setQuiz(QuizJDBC.getQuiz(con, qr.getQuizID()));
								setQuizReady(true);
							}
						}//end of if
						System.out.println("BEFORE QUIZ READY LOOP: is Student: "+isStudent+" quizReady: "+quizReady);
						while(!getQuizReady()){
							sleep(0);
						}

						System.out.println("STARTING QUIZ isStudent: "+isStudent+" quizReady: "+quizReady);
						if(isStudent){
							startQuizSession(con, quiz);
						}
					}catch(Exception e){
						System.out.println(e);
					}//end of try/catch block

				}

				// TODO sent final scores to client

				//after the quiz has finished, close the connections
				objectOutputStream.close();
				objectInputStream.close();
				dataInputStream.close();
				printStream.close();

			} catch(IOException ioe){
				System.out.println(ioe);
			} //end of try/catch block

		}//end of run method

		public boolean startQuizSession(Connection con, Quiz currentQuiz)
			throws Exception {

			// Send quiz to client
			objectOutputStream.writeObject(quiz);

			// Start quiz
			objectOutputStream.writeObject(new StartQuiz());

			// Iterate through each of the quiz questions
			for(int i = 1; i < 6; i++){

				// Initiate each question on the client side
				objectOutputStream.writeObject(new DisplayQuestion(i));

				try{
					// Read an object from the stream
					Object object = objectInputStream.readObject();

					// Check if the object is an AnswerResponse
					if (object instanceof AnswerResponse) {

						AnswerResponse currentResponse = (AnswerResponse)object;

						// Check if response is correct
						// TO DO: Implement getCorrect in the Question class
						int score;
						if(currentResponse.getResponse() == (currentQuiz.getQuestion(i).getCorrectAnswerPos())){

							score = (int) (1 / currentResponse.getResponseTime()) / 100;
							objectOutputStream.writeObject(new Score(score));

						// Otherwise, update client with incorrect answer - 0
						} else {
							score = -2;
							objectOutputStream.writeObject(new Score(score));
						}

						// TO DO: update database with result
					}

				} catch(ClassNotFoundException e){
					e.printStackTrace();
				} catch(EOFException e){
					System.out.println("Client has disconnected.");
					return false;
				}
			}
			return true;

		}

		public void sendObject(Object o) {
			try {
				objectOutputStream.writeObject(o);
			} catch(Exception e){
				e.printStackTrace();
			}
		}

	}//end of ClientTread class
	/*##########################################################################
#                             END OF INNER CLASS                              #
##############################################################################*/

	public void sendObjectToAll(Object o) {
		for(ClientThread c: clientArrayList) {
			try {
				c.sendObject(o);
			} catch(Exception e){ }
		}
	}

	public boolean getQuizReady() {
		// System.out.println("getQuizReady");
		return quizReady;
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuizReady(boolean b) {
		System.out.println("setQuizReady");
		quizReady = b;
	}

	public void setQuiz(Quiz q) {
		quiz = q;
	}

	public static void main(String[] args) throws Exception {
		QuizServer q = new QuizServer();
		q.ServerMain();
	}

}//end of QuizServer
