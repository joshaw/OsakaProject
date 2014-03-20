package quizGUI;

import quizObject.*;
import quizMain.QuizClient;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;

/**
 * Question frame is the screen that will display the questions in the quiz
 * Users have a time limit to click on one of the possible multiple choice answers.
 * @author bxc077
 * @version 20140314
 */
public class QuestionFrame extends MasterFrame implements Observer {

	private static final long serialVersionUID = 1L;
	QuizClient model;

	private JTextPane question = new JTextPane(); // Text-box displaying the question
//	private JTextPane answerA = new JTextPane(); // Text-box displaying possible answers...
//	private JTextPane answerB = new JTextPane();
//	private JTextPane answerC = new JTextPane();
//	private JTextPane answerD = new JTextPane();

	private JButton answerA = new JButton("A"); // Buttons for the possible answers
	private JButton answerB = new JButton("B");
	private JButton answerC = new JButton("C");
	private JButton answerD = new JButton("D");
	
	private CountDownTimer timer; // Visual timer

	/**
	 * Constructor
	 * Calls the setDiplay method to set up the question panel
	 * @param q - the question object to create GUI for
	 */
	public QuestionFrame(QuizClient model) {
		this.model = model;
	}

	/**
	 * setDisplay
	 * Sets the question display based on a question object
	 *
	 * @param q - the question object to create GUI for
	 */
	public void setDisplay() {

		// Sets the labels to the question/answers
		//question.setText(q.getQuestion()); question.setEditable(false);
		//question.setFont(new Font("SansSerif", Font.BOLD + Font.ITALIC, 16));
		//answerA.setText(q.getAnswer(0)); answerA.setEditable(false);
		//answerB.setText(q.getAnswer(1)); answerB.setEditable(false);
		//answerC.setText(q.getAnswer(2)); answerC.setEditable(false);
		//answerD.setText(q.getAnswer(3)); answerD.setEditable(false);

		// Sets dummy labels
		question.setText(""); question.setEditable(false);
		question.setFont(new Font("SansSerif", Font.BOLD + Font.ITALIC, 16));
		answerA.setText(""); //answerA.setEditable(false);
		answerB.setText(""); //answerB.setEditable(false);
		answerC.setText(""); //answerC.setEditable(false);
		answerD.setText(""); //answerD.setEditable(false);
		
		// Grid layout for the answer buttons and boxes
		//JPanel answerPane = new JPanel(new GridLayout(4,1,5,5));
		
		JPanel answerPane = new JPanel(new GridLayout(4,0));
		
		// Adds the questions to the layout
		JPanel panelA = new JPanel(); panelA.setLayout(new BoxLayout(panelA, BoxLayout.LINE_AXIS));
		JPanel panelB = new JPanel(); panelB.setLayout(new BoxLayout(panelB, BoxLayout.LINE_AXIS));
		JPanel panelC = new JPanel(); panelC.setLayout(new BoxLayout(panelC, BoxLayout.LINE_AXIS));
		JPanel panelD = new JPanel(); panelD.setLayout(new BoxLayout(panelD, BoxLayout.LINE_AXIS));
		JPanel questionPane = new JPanel(); questionPane.setLayout(new GridLayout(2,1,5,5));

		// The layout for this panel
		setLayout(new GridBagLayout());
		GridBagConstraints con = new GridBagConstraints();
		con.fill = GridBagConstraints.BOTH;

		// Adding the components to the answerPane
		 answerPane.add(answerA); answerA.addActionListener(new AnswerAListener()); //answerPane.add(panelA);
		 answerPane.add(answerB); answerB.addActionListener(new AnswerBListener()); //answerPane.add(panelB);
		 answerPane.add(answerC); answerC.addActionListener(new AnswerCListener()); //answerPane.add(panelC);
		 answerPane.add(answerD); answerD.addActionListener(new AnswerDListener()); //answerPane.add(panelD);

		// adding to questionPane
		questionPane.add(question);
		questionPane.add(answerPane);

		// finally adding to main panel with timer
		con.weightx = 1.0; con.weighty = 1.0;
		con.gridx = 0; con.gridy = 0; con.insets = new Insets(5,5,0,5);
		add(questionPane, con);
		
		con.weightx = 0; con.weighty = 1.0;
		con.gridx = 1; con.gridy = 0; con.insets = new Insets(5,5,0,5);
		// CountDownTimer timer = new CountDownTimer(q.getTimeLimit());
		this.timer = new CountDownTimer(10);
		add(timer, con);
		timer.setMinimumSize(new Dimension(50, 100));
		//System.out.println(timer.getPreferredSize());
		
	}
	/**
	 * startQuestion is the method that is called to reset and start the CountDownTimer.
	 */
	public void startQuestion() {
		timer.resetDisplay();
		timer.countDown();
	}
	
	/**
	 * ActionListener for the A button
	 */
	public class AnswerAListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			model.setResponseNumber(0);
			//System.out.println("A");
		}
	}

	/**
	 * ActionListener for the B button
	 */
	public class AnswerBListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			model.setResponseNumber(1);
			//System.out.println("B");
		}
	}
	
	/**
	 * ActionListener for the C button
	 */
	public class AnswerCListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			model.setResponseNumber(2);
			//System.out.println("C");
		}
	}

	/**
	 * ActionListener for the D button
	 */
	public class AnswerDListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			model.setResponseNumber(3);
			//System.out.println("D");
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

    /**
     * resetDisplay - when the panel is changed to this one, some of its components will need 
     * to be updated to represent the model
     */
	@Override
	public void resetDisplay() {
		
		// Sets the labels to the question/answers
		Question q = model.getCurrentQuestion();
		
		question.setText(q.getQuestion()); question.setEditable(false);
		question.setFont(new Font("SansSerif", Font.BOLD + Font.ITALIC, 16));
		answerA.setText(q.getAnswer(0)); //answerA.setEditable(false);
		answerB.setText(q.getAnswer(1)); //answerB.setEditable(false);
		answerC.setText(q.getAnswer(2)); //answerC.setEditable(false);
		answerD.setText(q.getAnswer(3)); //answerD.setEditable(false);
		
	}

}
