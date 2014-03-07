package quizObject;

import java.awt.Font;
import java.awt.GridLayout;
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
 * Question frame
 */
public class QuestionFrame extends JPanel implements Observer {


	private static final long serialVersionUID = 1L;
	
	JTextPane question = new JTextPane();
	JTextPane answerA = new JTextPane();
	JTextPane answerB = new JTextPane();
	JTextPane answerC = new JTextPane();	
	JTextPane answerD = new JTextPane();
	
	JButton a = new JButton("A");
	JButton b = new JButton("B");
	JButton c = new JButton("C");
	JButton d = new JButton("D");
	
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
	 * @param q - the question object to create GUI for
	 */
	public QuestionFrame(Question q) {
		super();
		
		// Sets the labels to the question/answers
		question.setText(q.getQuestion()); question.setEditable(false); 
		question.setFont(new Font("SansSerif", Font.BOLD + Font.ITALIC, 16));
		answerA.setText(q.getAnswer(0)); answerA.setEditable(false);
		answerB.setText(q.getAnswer(1)); answerB.setEditable(false);
		answerC.setText(q.getAnswer(2)); answerC.setEditable(false);
		answerD.setText(q.getAnswer(3)); answerD.setEditable(false);
		
		// Grid layout for the answer buttons and boxes
		JPanel questionPane = new JPanel(new GridLayout(4,1,5,5));
		
		// Adds the questions to the layout
		JPanel panelA = new JPanel(); panelA.setLayout(new BoxLayout(panelA, BoxLayout.LINE_AXIS));
		JPanel panelB = new JPanel(); panelB.setLayout(new BoxLayout(panelB, BoxLayout.LINE_AXIS));
		JPanel panelC = new JPanel(); panelC.setLayout(new BoxLayout(panelC, BoxLayout.LINE_AXIS));
		JPanel panelD = new JPanel(); panelD.setLayout(new BoxLayout(panelD, BoxLayout.LINE_AXIS));
		
		// Applies all to main QuestionFrame
		setLayout(new GridLayout(2,1,5,5));
		
		add(question);
		add(questionPane);
		panelA.add(a); panelA.add(answerA); a.addActionListener(new AnswerAListener()); questionPane.add(panelA);
		panelB.add(b); panelB.add(answerB); b.addActionListener(new AnswerBListener()); questionPane.add(panelB);
		panelC.add(c); panelC.add(answerC); c.addActionListener(new AnswerCListener()); questionPane.add(panelC);
		panelD.add(d); panelD.add(answerD); d.addActionListener(new AnswerDListener()); questionPane.add(panelD);
		
	}
	// Main method tester
	public static void main(String[] args) {
		
		Question q1 = new Question("Question 1");
		
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
