import quizObject.*;

import java.net.Socket;
import java.net.SocketException;
import java.io.*;

public class QuizClient {

	private static final int PORT = 9000;

	private PrintWriter out;
	private InputStream inputStream;
	private Socket socket;
	private ObjectInputStream objectInput;
	private ObjectOutputStream objectOutput;

	private boolean connected = true;
	private LoginReply loginReply;
	private Quiz quiz;
	private Question currentQuestion;

	private long questionReceivedTime;
	private boolean isStudentUser;
	private boolean loginIsSuccessful;

	// Print notification when starting.
	public QuizClient() {
		System.out.println("Client running");
	}

/******************************************************************************
*                                    RUN                                      *
******************************************************************************/
	private void run() throws Exception {

		socket = new Socket("localhost", PORT);
		out = new PrintWriter(socket.getOutputStream(), true);

		objectOutput = new ObjectOutputStream(socket.getOutputStream());
		objectInput = new ObjectInputStream(socket.getInputStream());

		// Here the name and password fetched by the gui is sent to the server
		objectOutput.writeObject(new LoginRequest("josh", 123456789));
		System.out.flush();

		/* Until the end of the quiz, keep listening for objects.*/
		Object object;
		while (true) {
			try{

				// Read an object from the stream.
				object = objectInput.readObject();

				// ------------------------------------- Quiz
				if (object instanceof Quiz) {
					quiz = (Quiz) object;

					currentQuestion = quiz.getQuestion(1);
					System.out.println(currentQuestion);

					// ------------------------------------- LoginReply
				} else if (object instanceof LoginReply) {
					loginReply = (LoginReply) object;
					loginIsSuccessful = loginReply.isSuccessful();
					isStudentUser = loginReply.isStudent();

					// ------------------------------------- StartQuiz
				} else if (object instanceof StartQuiz) {
					StartQuiz start = (StartQuiz) object;
					System.out.println("Start Quiz");
					// Start Quiz!

					// ------------------------------------- DisplayQuestion
				} else if (object instanceof DisplayQuestion) {
					DisplayQuestion displayQuestion = (DisplayQuestion) object;
					questionReceivedTime = System.currentTimeMillis();

					currentQuestion = quiz.getQuestion(
							displayQuestion.getNumber());

					System.out.println(displayQuestion.getNumber());
					// Display question

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
	public boolean isStudentUser() {
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
