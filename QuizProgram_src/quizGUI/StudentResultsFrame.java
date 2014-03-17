package quizGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import quizMain.QuizClient;
import quizObject.Question;
/**
 * Class StudentResultsFrame is the display the student will see after they have answered a question.
 * @author bxc077
 * @version 20140314
 */
public class StudentResultsFrame extends MasterFrame implements Observer {

	private static final long serialVersionUID = 1L;
	
	// Quiz model
	QuizClient model;
	
	// GUI components
	JProgressBar progressBar = new JProgressBar(); //questions answered so far
	private JTextPane question = new JTextPane(); // Text-box displaying the question
	private JTextPane answerA = new JTextPane(); // Text-box displaying possible answers...
	private JTextPane answerB = new JTextPane(); // The correct answer will be highlighted
	private JTextPane answerC = new JTextPane();
	private JTextPane answerD = new JTextPane();
	
	private LeaderBoard leaderBoard;
	
	public StudentResultsFrame(QuizClient model) {
		this.model = model;
		setDisplay(new Question()); // TODO model.getCurrentQuestion
	}
	
	/**
	 * setDisplay
	 * Sets the question display based on a question object
	 *
	 * @param q - the question object to create GUI for
	 */
	public void setDisplay(Question q) {
	
		// Grid layout for the answers
		JPanel answerPane = new JPanel(new GridLayout(4,1,5,5));
		
		leaderBoard = new LeaderBoard(model.getAllScores());
		
		// Sets the labels to the question/answers
		question.setText(q.getQuestion()); question.setEditable(false);
		question.setFont(new Font("SansSerif", Font.BOLD + Font.ITALIC, 16));
		
		// Highlights the correctAnswer
		Color green = new Color(10, 100, 20);
		Color red = Color.RED;

		if(model.getResponseNumber() == 0) answerA.setForeground(red);
		if(model.getResponseNumber() == 1) answerB.setForeground(red);
		if(model.getResponseNumber() == 2) answerC.setForeground(red);
		if(model.getResponseNumber() == 3) answerD.setForeground(red);

		if(q.getCorrectAnswerPos() == 0) answerA.setForeground(green);
		if(q.getCorrectAnswerPos() == 1) answerB.setForeground(green);
		if(q.getCorrectAnswerPos() == 2) answerC.setForeground(green);
		if(q.getCorrectAnswerPos() == 3) answerD.setForeground(green);
		
		answerA.setText("A: " + q.getAnswer(0)); answerA.setEditable(false); answerPane.add(answerA);
		answerB.setText("B: " + q.getAnswer(1)); answerB.setEditable(false); answerPane.add(answerB);
		answerC.setText("C: " + q.getAnswer(2)); answerC.setEditable(false); answerPane.add(answerC);
		answerD.setText("D: " + q.getAnswer(3)); answerD.setEditable(false); answerPane.add(answerD);

		// Adds the questions to the layout
		JPanel questionPane = new JPanel(); questionPane.setLayout(new GridLayout(2,1,5,5));
		questionPane.add(question);
		questionPane.add(answerPane);
		
		// The layout for this panel
		setLayout(new GridBagLayout());
		GridBagConstraints con = new GridBagConstraints();
		

		// adding to questionPane
		questionPane.add(question);
		questionPane.add(answerPane);

		// finally adding to main panel
		con.fill = GridBagConstraints.BOTH;
		con.weightx = 1.0; con.weighty = 1;
		con.gridx = 0; con.gridy = 0; con.insets = new Insets(5,5,0,5);
		add(questionPane, con);
		con.fill = GridBagConstraints.VERTICAL;
		con.weightx = 0; con.weighty = 1;
		con.gridx = 1; con.gridy = 0; con.insets = new Insets(5,5,0,5);
		add(leaderBoard, con);
		setPreferredSize(new Dimension(600, 600));
	}
	
    /**
     * resetDisplay - when the panel is changed to this one, some of its components will need 
     * to be updated to represent the model
     */
	public void resetDisplay() {

		// Sets the labels to the question/answers
		Question q = model.getCurrentQuestion();
		
		question.setText(q.getQuestion());
		question.setFont(new Font("SansSerif", Font.BOLD + Font.ITALIC, 16));
		
		answerA.setText("A: " + q.getAnswer(0));
		answerB.setText("B: " + q.getAnswer(1));
		answerC.setText("C: " + q.getAnswer(2));
		answerD.setText("D: " + q.getAnswer(3));
		
		// Highlights the correctAnswer
		Color green = new Color(10, 100, 20);
		Color red = Color.RED;

		if(model.getResponseNumber() == 0) answerA.setForeground(red);
		if(model.getResponseNumber() == 1) answerB.setForeground(red);
		if(model.getResponseNumber() == 2) answerC.setForeground(red);
		if(model.getResponseNumber() == 3) answerD.setForeground(red);

		if(q.getCorrectAnswerPos() == 0) answerA.setForeground(green);
		if(q.getCorrectAnswerPos() == 1) answerB.setForeground(green);
		if(q.getCorrectAnswerPos() == 2) answerC.setForeground(green);
		if(q.getCorrectAnswerPos() == 3) answerD.setForeground(green);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
	
}
