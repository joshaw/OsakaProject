package quizMain;

import quizObject.*;
import quizGUI.*;

import javax.swing.JPanel;
import javax.swing.JFrame;

import java.net.Socket;
import java.net.SocketException;
import java.io.*;

import java.util.Observable;

public class QuizClient extends Observable {

    private static final int PORT = 9000;

    private PrintWriter out;
    private InputStream inputStream;
    private Socket socket;
    private ObjectInputStream objectInput;
    private ObjectOutputStream objectOutput;

    private JFrame frame;
    private JPanel[] guiElements = new JPanel[10];

    private boolean connected = true;
    private LoginReply loginReply;
    private Quiz quiz;
    private long currentQuizID;
    private Question currentQuestion;
    private int responseNumber = -1;
    private Long[] quizIDs;
    private String[] quizNames;

    private String username;
    private String passwordHash;
    private long questionReceivedTime;
    private boolean isStudentUser;
    private boolean loginIsSuccessful = false;

    // Print notification when starting.
    public QuizClient() {
        System.out.println("Client running");

        frame = new JFrame("Quiz");
        guiElements[0] = new LoginFrame(this);
        guiElements[1] = new StudentHomeFrame(this);
        guiElements[2] = new AdminHomeFrame(this);
        guiElements[3] = new QuestionFrame(this);

        JFrame.setDefaultLookAndFeelDecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setContentPane(guiElements[0]);

        frame.pack();
        frame.setVisible(true);

        System.out.println("Started GUI");
    }

    /******************************************************************************
     *                                    RUN                                      *
     ******************************************************************************/
    private void run() throws Exception {

        Object object;
        socket = new Socket("localhost", PORT);
        out = new PrintWriter(socket.getOutputStream(), true);

        objectOutput = new ObjectOutputStream(socket.getOutputStream());
        objectInput = new ObjectInputStream(socket.getInputStream());

        System.out.println(loginIsSuccessful);
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
                        //Student has successfully logged in
                        frame.setContentPane(guiElements[1]);
                        System.out.println("User is Student");
                    } else {
                        frame.setContentPane(guiElements[2]);
                        System.out.println("User is Admin");
                    }
                    frame.pack();
                    frame.repaint();
                }
            }
        }

        objectOutput.writeObject(new QuizRequest(3L));
        // TODO check quiz request was successful

        /* Until the end of the quiz, keep listening for objects.*/
        while (loginReply.isSuccessful()) {
            try{

                // Read an object from the stream.
                object = objectInput.readObject();

                // ------------------------------------- Quiz
                if (object instanceof Quiz) {
                    quiz = (Quiz) object;

                    currentQuestion = quiz.getQuestion(1);
                    System.out.println(currentQuestion);

                    // ------------------------------------- StartQuiz
                } else if (object instanceof StartQuiz) {
                    StartQuiz start = (StartQuiz) object;
                    // Start Quiz!

                    // ------------------------------------- DisplayQuestion
                } else if (object instanceof DisplayQuestion) {
                    DisplayQuestion displayQuestion = (DisplayQuestion) object;

                    responseNumber = -1;
                    questionReceivedTime = System.currentTimeMillis();

                    currentQuestion = quiz.getQuestion(
                            displayQuestion.getNumber());

                    System.out.println(displayQuestion.getNumber());

                    objectOutput.writeObject(waitForUserResponse());
                    // Display question

                } else if (object instanceof Score) {
                    Score score = (Score) object;

                    // ------------------------------------- OTHER
                } else {
                    System.out.println("I don't know what to do.");
                }

                // ----------------------------------------- NO OBJECT

            } catch (SocketException e){
            } catch (EOFException e) {
                System.out.println("The connection to the server has been" +
                        " lost. Shutting down.");
                connected = false;
                socket.close();
                break;
            }
        }
    }

    public void setResponseNumber(int responseNumber) {
        this.responseNumber = responseNumber;
    }

    public String getAnswer(int i) {
        return currentQuestion.getAnswer(i);
    }

    public Long[] getQuizIDs() {
        // return quizIDs;
        Long[] temp = {1L, 2L, 3L, 4L, 5L, 6L};
        return temp;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.passwordHash = password;
    }

    public void setCurrentQuizID(long CurrentQuizID) {
        this.currentQuizID = currentQuizID;
    }

    /**
     * requestLogin - action for the loginButton in the LoginFrame
     *
     * Converts the char[] password to string password and hashes.
     * Then creates LoginRequest object based on the user name and hashed password string.
     *
     * @return LoginRequest
     */
    public void requestLogin() {
        try {
            objectOutput.writeObject(new LoginRequest(username, passwordHash));
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void adminStart() {
        QuizRequest quizRequest = new QuizRequest(currentQuizID);
        try {
            objectOutput.writeObject(quizRequest);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private AnswerResponse waitForUserResponse() {
        long responseTime;
        while ((responseTime = System.currentTimeMillis() - questionReceivedTime) < 1000
                && responseNumber == -1) {
            //pause
                }

        if (responseNumber == -1) {
            System.out.println("No user response. Score of -2");
            return new AnswerResponse(-1);
        }

        return new AnswerResponse(responseNumber, responseTime);
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

    /******************************************************************************
     *                                    MAIN                                     *
     ******************************************************************************/
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
