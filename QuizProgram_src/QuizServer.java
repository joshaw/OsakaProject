import quizObject.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Time;

/**
 * Server class for the interactive quiz game
 * @author sxf796 - server setup, creation and management of client connections
 * @author rjs305 - liason between the server and the client after the connection has been made to provide quiz functionality
 */
public class QuizServer {

	private static final int PORT = 9000;
	private ServerSocket listener;
	private Socket clientSocket;

	/**
	 * Starts the server
	 */
	public void main(String[] args) throws Exception{

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
				new ClientThread(clientSocket).start();

			}//end of while
			
		} catch(IOException ioe){
			System.out.println(ioe); //when would this be throw? write in when and why here, then include a way of handling it
			// at a later point - decide this with the group 
		} finally{
			clientSocket.close();
		}//end of try/catch/finally block

	}//end of startServer method

	/**
	 * Inner class that is created when a new client connects to the server
	 *
	 */
	private class ClientThread extends Thread{

		private Socket clientSocket;

		//instance variables which allow communication between the Server and Client 
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

				//create the input and output streams for the server to communicate with the client
				objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
				objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
				dataInputStream = new DataInputStream(clientSocket.getInputStream());
				printStream = new PrintStream(clientSocket.getOutputStream());

				//send and handle the login request to client
				boolean userExists = processLoginRequest();

                // This quiz object is a dummy, and will be replaced with the quiz retrieved from the database
                Quiz currentQuiz = new Quiz();

                // Check user exists before proceeding to process quiz
				if(userExists){

                    // Send quiz to client
                    objectOutputStream.writeObject(currentQuiz);

                    // Start quiz
                    objectOutputStream.writeObject(new StartQuiz());

                    // Iterate through each of the quiz questions
                    for(int i = 1; i < 6; i++){
                        // Sending a DisplayQuestion object to initiate each question on the client side
                        objectOutputStream.writeObject(new DisplayQuestion(i));

                        // Record a the point in time that question is released
                        Time questionTime = new Time(System.currentTimeMillis());

                        // While 10 seconds (10000 milliseconds) is greater that the elapsed time, check for responses from client
                        while(10000 > System.currentTimeMillis() - questionTime.getTime()){
                            try{
                                // Read an object from the stream
                                Object object = objectInputStream.readObject();

                                // Check if the object is an AnswerResponse
                                if (object instanceof AnswerResponse) {
                                    // If it is, cast it to the AnswerResponse type, then check if the response if correct
                                    AnswerResponse currentResponse = (AnswerResponse) object;

                                    // Check if response is correct - TO DO: Implement getCorrect in the Question class
                                    if(currentResponse.getResponse() == currentQuiz.getQuestion(i).getCorrect()){

                                        objectOutputStream.writeObject(new Score((1 / currentResponse.getResponse()) * 10));

                                        // TO DO: update database with result

                                    } else { // Otherwise, update score of client with incorrect answer - 0 or negative(?)

                                        objectOutputStream.writeObject(new Score(-2));

                                        // TO DO: update database with result
                                    }

                                }
                            }
                            catch(ClassNotFoundException e){
                                e.printStackTrace();
                            }
                        }
                    }


				} else{
					//send a message to the client asking them to re-enter their login details
				}//end of if/else

				//after the quiz has finished, close the connections
				objectOutputStream.close();
				objectInputStream.close();
				dataInputStream.close();
				printStream.close();
				
			} catch(IOException ioe){
				System.out.println(ioe);
			} //end of try/catch block

		}//end of run method

		/**
		 * Sends and processes a login request to the client.
		 * Returns true if the client is found in the database, false otherwise
		 * @return boolean
		 */
		private boolean processLoginRequest(){

			/*
			 * Send a log in request to the client
			 * TODO this may need to change, for example there could be a LoginRequestServer object, 
			 * which gets sent to the client model, causing the log in screen to be displayed
			 */
			printStream.println("Please enter your login details"); 

			try{

				//obtain the LoginRequest object from the client
				Object newObj = objectInputStream.readObject(); 

				if(newObj instanceof LoginRequest){ 

					//Obtain the user name and hashed password from the LoginRequest Object
					String username = ((LoginRequest) newObj).getUsername();
					int passwordHash = ((LoginRequest) newObj).getPasswordHash();

					/*
					 * These details need to be send to the database to see if they exist.
					 * 
					 * These should be a method/stored procedure in the JDBC class that searches the 
					 * database for a matching username/passwordHash entry, that returns true if the user 
					 * exists and false if it doesn't. I will make a call to this method and return the 
					 * result, e.g.
					 * 
					 * return JDBC.doesUserExist(username, passwordHash);
					 *
                    */

                    /* This is how information can be returned to client as a LoginReply

                     Boolean userExists = RESULT OF QUERY GOES HERE;
                     Boolean isStudent = RESULT OF QUERY GOES HERE;

                     if(userExists){
                     if(isStudent){
                     objectOutputStream.writeObject(new LoginReply(true, true));
                     } else {
                     objectOutputStream.writeObject(new LoginReply(true, false));
                     }
                     } else {
                     objectOutputStream.writeObject(new LoginReply(false, false));
                     }

                    */

					return true; //place holder until the above code can be executed

				}//end of if

			} catch(Exception e){
				System.out.println(e);
			}//end of try/catch block
			
			return false; //returned if the user doesn't exist
			
		}//end of processLoginRequest method


	}//end of ClientTread class




}//end of QuizServer
