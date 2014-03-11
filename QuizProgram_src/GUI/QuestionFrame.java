package quizObject;

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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;


/**
 * Question frame is the screen that will display the questions in the quiz
 * Users have a time limit to click on one of the possible multiple choice answers.
 * 
 */
public class QuestionFrame extends JPanel implements Observer {


	private static final long serialVersionUID = 1L;
	
	private JTextPane question = new JTextPane(); // Text-box displaying the question
	private JTextPane answerA = new JTextPane(); // Text-box displaying possible answers...
	private JTextPane answerB = new JTextPane();
	private JTextPane answerC = new JTextPane();
	private JTextPane answerD = new JTextPane();
	
	private JButton a = new JButton("A");
	private JButton b = new JButton("B");
	private JButton c = new JButton("C");
	private JButton d = new JButton("D");
	
	public class AnswerAListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// INSERT A ACTION HERE!!!
			System.out.println("A");
		}
	}
	
	public class AnswerBListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// INSERT B ACTION HERE!!!
			System.out.println("B");
		}
	}
	
	public class AnswerCListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// INSERT C ACTION HERE!!!
			System.out.println("C");
		}
	}
	
	public class AnswerDListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// INSERT D ACTION HERE!!!
			System.out.println("D");
		}
	}
	
	/**
	 * Constructor
	 * Calls the setDiplay method to set up the question panel
	 * @param q - the question object to create GUI for
	 */
	public QuestionFrame(Question q) {
		setDisplay(q);
	}
	
	/**
	 * setDisplay
	 * Sets the question display based on a question object
	 * 
	 * @param q - the question object to create GUI for
	 */
	public void setDisplay(Question q) {
		
		// Sets the labels to the question/answers
		question.setText(q.getQuestion()); question.setEditable(false); 
		question.setFont(new Font("SansSerif", Font.BOLD + Font.ITALIC, 16));
		answerA.setText(q.getAnswer(0)); answerA.setEditable(false);
		answerB.setText(q.getAnswer(1)); answerB.setEditable(false);
		answerC.setText(q.getAnswer(2)); answerC.setEditable(false);
		answerD.setText(q.getAnswer(3)); answerD.setEditable(false);
		
		// Grid layout for the answer buttons and boxes
		JPanel answerPane = new JPanel(new GridLayout(4,1,5,5));
		
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
		panelA.add(a); panelA.add(answerA); a.addActionListener(new AnswerAListener()); answerPane.add(panelA);
		panelB.add(b); panelB.add(answerB); b.addActionListener(new AnswerBListener()); answerPane.add(panelB);
		panelC.add(c); panelC.add(answerC); c.addActionListener(new AnswerCListener()); answerPane.add(panelC);
		panelD.add(d); panelD.add(answerD); d.addActionListener(new AnswerDListener()); answerPane.add(panelD);
		
		// adding to questionPane
		questionPane.add(question);
		questionPane.add(answerPane);
		
		// finally adding to main panel with timer
		con.weightx = 1.0; con.weighty = 1.0;
		con.gridx = 0; con.gridy = 0; con.insets = new Insets(5,5,0,5);
		add(questionPane, con);
		con.weightx = 0; con.weighty = 1.0;
		con.gridx = 1; con.gridy = 0; con.insets = new Insets(5,5,0,5);
		
		CountDownTimer timer = new CountDownTimer(10);
		add(timer, con);
		timer.countDown();
	}
	

	// Main method tester
	public static void main(String[] args) {
		
		Question q1 = new Question(true);

		JFrame frame = new JFrame("Question");
		JPanel pane = new QuestionFrame(q1);
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(pane);
		frame.pack();
		frame.setVisible(true);
		
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
	
	
}
