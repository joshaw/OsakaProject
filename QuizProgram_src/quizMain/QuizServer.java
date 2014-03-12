package quizMain;

import quizObject.*;

import java.sql.*;
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
                new ClientThread(clientSocket).start();

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

    /**
     * Inner class that is created when a new client connects to the server
     *
     */
    private class ClientThread extends Thread {

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

                //create a connection to the database
                Connection con = QuizJDBC.getConnection();

                boolean userExists = false;

                //while loop is designed to run until a successful login has been recieved
                while(!userExists) {
                    try{
                        Object newObj = objectInputStream.readObject();
                        if(newObj instanceof LoginRequest){
                            LoginRequest loginRequest = (LoginRequest) newObj;

                            //Obtain the user name and hashed password from the LoginRequest Object
                            String username = loginRequest.getUsername();

                            //stored in database as a string
                            String password = loginRequest.getPasswordHash();

                            //query database for login details, create a login reply object and return it
                            LoginReply lr = QuizJDBC.isUser(con, username, password);
                            // LoginReply lr = new LoginReply(true, true, "Josh");
                            objectOutputStream.writeObject(lr); //send the loginReply to the client

                            //is the user exists exit the while loop and move onto quiz
                            if(lr.isSuccessful()) {
                                userExists = true;
                            } else {
                                objectOutputStream.writeObject(new LoginReply(false));
                            }

                        }//end of if

                    } catch(EOFException e){
                        System.out.println("Client has not logged in.");
                        break;
                    } catch(Exception e){
                        System.out.println(e);
                    }//end of try/catch block
                }//end of while

                // User has logged in, start quiz
                if (userExists) {

                    /*
                     * The following try/catch block recieves a QuizRequest Object from
                     * client (admin) and retrieves the required quiz from the database
                     */
                    Quiz currentQuiz = null;
                    try{
                        Object potentialQuizObject = objectInputStream.readObject();
                        if(potentialQuizObject instanceof QuizRequest){
                            long quizID = ((QuizRequest) potentialQuizObject).getQuizID();

                            //get quiz from database and set to currentQuiz
                            // currentQuiz = QuizJDBC.getQuiz(con, quizID);
                            currentQuiz = new Quiz();
                        }//end of if

                        try {
                            startQuizSession(con, currentQuiz);
                        } catch(Exception e){
                            e.printStackTrace();
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

        public boolean startQuizSession(Connection con, Quiz currentQuiz) throws Exception {
            // Send quiz to client
            objectOutputStream.writeObject(currentQuiz);

            // Start quiz
            objectOutputStream.writeObject(new StartQuiz());

            // Iterate through each of the quiz questions
            for(int i = 1; i < 6; i++){

                // Sending a DisplayQuestion object to initiate each question on the client side
                objectOutputStream.writeObject(new DisplayQuestion(i));

                try{
                    // Read an object from the stream
                    Object object = objectInputStream.readObject();

                    // Check if the object is an AnswerResponse
                    if (object instanceof AnswerResponse) {

                        AnswerResponse currentResponse = (AnswerResponse) object;

                        // Check if response is correct - TO DO: Implement getCorrect in the Question class
                        int score;
                        if(currentResponse.getResponse() == (currentQuiz.getQuestion(i).getCorrectAnswerPos())){

                            score = (int) (1 / currentResponse.getResponseTime()) / 100;
                            objectOutputStream.writeObject(new Score(score));

                        } else { // Otherwise, update score of client with incorrect answer - 0 or negative(?)
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

    }//end of ClientTread class

    public static void main(String[] args) throws Exception {
        QuizServer q = new QuizServer();
        q.ServerMain();
    }

}//end of QuizServer
