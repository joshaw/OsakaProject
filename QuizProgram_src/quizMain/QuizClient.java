package quizMain;

import quizObject.*;
import quizGUI.*;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.JLabel;

import java.net.Socket;
import java.net.SocketException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.awt.Dimension;
import java.io.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;

public class QuizClient extends Observable {

    private static final int PORT = 9010;
    private static final String IPADDRESS = "localhost";

    private static final int LOGIN = 0;
    private static final int STUDENTHOME = 1;
    private static final int ADMINHOME = 2;
    private static final int QUESTION = 3;
    private static final int WAITING = 4;
    private static final int STUDENTRESULTS = 5;
    private static final int ADMINRESULTS = 6;
    private static final int FINALRESULTS = 7;

    private PrintWriter out;
    private InputStream inputStream;
    private Socket socket;
    private ObjectInputStream objectInput;
    private ObjectOutputStream objectOutput;

    private JFrame frame;
    private MasterFrame[] guiElements = new MasterFrame[10];

    private boolean connected = true;
    private LoginReply loginReply;
    private Quiz quiz;
    private long currentQuizID;
    private Question currentQuestion;
    private int responseNumber = -1;
    private Long[] quizIDs;
    private String[] quizNames;
    private ArrayList<Score> allScores; // when received should be sorted (in-order)
    private int questionScore;
    private int currentQuestionNumber = 0;
    
    private String username;
    private String passwordHash;
    private long questionReceivedTime;
    private boolean isStudentUser;
    private boolean loginIsSuccessful = false;
    private boolean continueListening = true;
    private int numberOfUsers = 1;

    // Print notification when starting.
    public QuizClient() {
        try {
            socket = new Socket(IPADDRESS, PORT);
        } catch(IOException e) {
            System.out.println("Can't connect to server. Exiting...");
            System.exit(1);
        }

        System.out.println("Client running.");

        // Changing the LookAndFeel to nimbus
        try {
            UIManager.setLookAndFeel( "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch(Exception e) {

        }

        frame = new JFrame("Quiz");
        guiElements[LOGIN] = new LoginFrame(this);
        guiElements[STUDENTHOME] = new StudentHomeFrame(this);
        guiElements[ADMINHOME] = new AdminHomeFrame(this);
        guiElements[QUESTION] = new QuestionFrame(this);
        ((QuestionFrame) guiElements[QUESTION]).setDisplay();
        guiElements[WAITING] = new WaitingFrame();
        guiElements[STUDENTRESULTS] = new StudentResultsFrame(this);

        JFrame.setDefaultLookAndFeelDecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        changeContentPane(LOGIN);
        frame.setVisible(true);

        System.out.println("Client GUI loaded.");

        allScores = new ArrayList<Score>();
        //allScores.add(new Score("Mary", 60));
    }

    //*************************************************************************
    //                                  RUN                                   *
    //************************************************************************/
    private void run() throws Exception {

        Object object;
        out = new PrintWriter(socket.getOutputStream(), true);

        objectOutput = new ObjectOutputStream(socket.getOutputStream());
        objectInput = new ObjectInputStream(socket.getInputStream());

        while (!loginIsSuccessful) {
            // Here the name and password fetched by the gui is sent to the server
            // objectOutput.writeObject(new LoginRequest(1, 25));
            // System.out.println("login request sent");

            object = objectInput.readObject();
            if (object instanceof LoginReply) {
                loginReply = (LoginReply) object;
                loginIsSuccessful = loginReply.isSuccessful();
                isStudentUser = loginReply.isStudent();

                if (loginIsSuccessful) {
                    if (isStudentUser) {
                    	System.out.println("Client Student user "+getUsername()+" logged in.");
                        changeContentPane(STUDENTHOME);
                        studentSession(object);

                    } else {
                    	System.out.println("Client Admin user "+getUsername()+" logged in.");
                        changeContentPane(ADMINHOME);
                        adminSession(object);
                    }
                }
            }
        }
    }

    public void changeContentPane(int i) {
        frame.setContentPane(guiElements[i]);
        guiElements[i].resetDisplay();
        frame.pack();
        frame.repaint();

        // If changing to the QuestionFrame, then start the CountDownTimer.
        if(i == QUESTION) {
            ((QuestionFrame) guiElements[QUESTION]).startQuestion();
        }
        frame.setPreferredSize(new Dimension(600, 300));
    }

    public int getResponseNumber() {
        return responseNumber;
    }

    public void setResponseNumber(int n) {
        //System.out.println("response number was "+responseNumber);
        this.responseNumber = n;
        //System.out.println("response number changed to "+responseNumber);

    }

    public String getAnswer(int i) {
        return currentQuestion.getAnswer(i);
    }

    public Long[] getQuizIDs() {
        // TODO
        // return quizIDs;
        Long[] temp = {1L, 2L, 3L, 6L, 7L, 8L};
        return temp;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        // TODO
        // hash password
        this.passwordHash = password;
    }

    public void setCurrentQuizID(long currentQuizID) {
        this.currentQuizID = currentQuizID;
    }

    public ArrayList<Score> getAllScores() {
        return allScores;
    }

    public int getQuestionScore() { 
    	return questionScore;
    }
    
    /**
     * @return a question object which is the current question as specified by
     * the server.
     */
    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    /**
     * @return true if the client is still connected to the server.
     */
    public boolean isConnected() {
        return connected;
    }

    public LoginReply getLoginReply() {
        return loginReply;
    }

    /**
     * @return true if the login was successful, username and password were in
     * the database or were added successfully.
     */
    public boolean loginIsSuccessful() {
        return loginIsSuccessful;
    }

    /**
     * @return true if the user is a Student, otherwise must be an Admin
     */

    public boolean isStudent() {
        return isStudentUser;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    /** requestLogin - action for the loginButton in the LoginFrame
     *
     * @return LoginRequest
     */
    public void requestLogin() {
        try {
        	setUsername(username);
            objectOutput.writeObject(new LoginRequest(username, passwordHash));
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * requestWaitingScreen - action for the start button in the StudentHomeFrame
     */
    public void requestWaitingScreen() {
            changeContentPane(WAITING);
    }

    public void adminStart() {
        QuizRequest quizRequest = new QuizRequest(currentQuizID);
        sendObject(quizRequest);
    }

    // Allows GUI to get score for last question
    //public int getScore(){
        //score.get
    //}

    private AnswerResponse waitForUserResponse() {
        long responseTime;

        Outerloop:
            while ((responseTime = System.currentTimeMillis() - questionReceivedTime) < 7000) {
                if(getResponseNumber() != -1) {

                    break Outerloop;
                }
                if(getResponseNumber() == -1) { //if this is taken out it won't work
                    System.out.print("");
                }
            }

        if (responseNumber == -1) {
            //System.out.println("No user response. Score of -2");
            return new AnswerResponse(-1);
        }

        return new AnswerResponse(responseNumber, responseTime);
    }

    private void studentSession(Object object) throws Exception {
        /* Until the end of the quiz, keep listening for objects.*/
    	QuizLoop:
        while (loginReply.isSuccessful()) {
            try{
                try {
                    object = objectInput.readObject();
                } catch (EOFException e){
                	System.out.println("Reading the object causes an EOF.");
                }
            	// Read an object from the stream.

                // ------------------------------------- Quiz
                if (object instanceof Quiz) {
                    quiz = (Quiz) object;

                    currentQuestion = quiz.getQuestion(0);
                    //System.out.println(currentQuestion);

                    // ------------------------------------- StartQuiz
                } else if (object instanceof StartQuiz) {
                    StartQuiz start = (StartQuiz) object;
                    System.out.println("Student user "+getUsername()+" is starting the quiz.");

                    // ------------------------------------- DisplayQuestion
                } else if (object instanceof DisplayQuestion) {
                    DisplayQuestion displayQuestion = (DisplayQuestion) object;

                    setResponseNumber(-1);
                    questionReceivedTime = System.currentTimeMillis();

                    currentQuestionNumber = displayQuestion.getNumber(); 
                    currentQuestion = getQuiz().getQuestion(currentQuestionNumber);
                    System.out.println(username+" is currently answering question "+displayQuestion.getNumber());

                    guiElements[QUESTION].resetDisplay();

                    changeContentPane(QUESTION);

                    AnswerResponse ur = waitForUserResponse();
                    

                    objectOutput.writeObject(ur);
                    
                    System.out.println("WAIT 1");
                    // ------------------------------------- SCORE
                    int updates = 0;
                    boolean continueListening = false;     
                    	
                	// Check results for every user
                	do {
                		System.out.println("Start of listening loop ");  
                    	// Read input stream if data is available
	                    object = objectInput.readObject();
	                    
	                    if (object instanceof ArrayList<?>) {

	                    	int mark = 0;
	                    	for(int j = 0; j < allScores.size(); j++){
	                    		if(allScores.get(j).getUsername().equalsIgnoreCase(username)) {
	                    			mark = allScores.get(j).getMark();
	                    		}
	                    	}
	
	                        allScores = (ArrayList<Score>) object;
	                        Collections.sort(allScores);
	                        
	                    	for(int j = 0; j < allScores.size(); j++){
	                    		if(allScores.get(j).getUsername().equalsIgnoreCase(username)) {
	                    			mark = allScores.get(j).getMark() - mark;
	                    		}
	                    	}
	                    	
	                        questionScore = mark;
	                        
	                        // Reset student results pane when new results are available
	                        changeContentPane(STUDENTRESULTS);
	                        
	                        //System.out.println("Displaying results having got scores.");
	                        
	                        if(currentQuestionNumber >= 9){
	                        	// After quiz is complete, finish quiz 
	                        	break QuizLoop;
	                        }
	                        
	                        continueListening = false;
	                        
	                    	for(int j = 0; j < allScores.size(); j++){
	                    		System.out.println("Allscores check: "+allScores);
	                    		System.out.println("Current Question Number: "+currentQuestionNumber);
	                    		if(allScores.get(j).getCurrentQuestion() < currentQuestionNumber) {
	                    			continueListening = true;
	                    		}
	                    	}
	                    	
	                    	System.out.println("Continue Listening: "+continueListening);
	                    	
	                    }
	                System.out.println("End of listening loop ");    
                    } while (continueListening);
                	System.out.println("After listening loop ");
                	while((System.currentTimeMillis() - questionReceivedTime) < 10000){System.out.print("");}
                }
                    // ------------------------------------- OTHER
                else {
                    System.out.println("I don't know what to do.");
                    System.out.println(object);
                }

                // ----------------------------------------- NO OBJECT

            } catch (SocketException e){
            	e.printStackTrace();
            } catch (EOFException e) {
                System.out.println("The connection to the server has been" +
                " lost. Shutting down.");
                connected = false;
                socket.close();
                break;
            }
        }
    }

    private void adminSession(Object object) {

    }

    public boolean sendObject(Object object) {
        if (object instanceof AnswerResponse) {
            AnswerResponse response = (AnswerResponse) object;
            response.setResponseTime(
                    System.currentTimeMillis() - questionReceivedTime);
        }

        try{
            objectOutput.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**************************************************************************
     *                                  MAIN                                  *
     *************************************************************************/
    /** Main client method. Starts the client running.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        QuizClient client = new QuizClient();
        client.run();

    }
}
