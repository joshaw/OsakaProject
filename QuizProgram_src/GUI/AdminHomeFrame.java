package quizGUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import quizObject.*;

/**
 * 
 * @author benji
 *
 */
public class AdminHomeFrame extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;
	
	// Model
	QuizModel model;
	
	// GUI
	JLabel label;
	JComboBox<String> selectQuiz;
	JButton start = new JButton("Start");
	JButton modifyQuiz = new JButton("Create/Delete/Modify quiz");
	JTextArea studentsConnected;
	
	/**
	 * Constructor
	 */
	public AdminHomeFrame(QuizModel model) {
		this.model = model;
		setDisplay();
	}
	
	/**
	 * Sets the display for the AdminHomeFrame
	 */
	public void setDisplay() {
		
		label = new JLabel("Hi " + model.getUsername() + " welcome to the quiz");
		label.setFont(new Font("SansSerif", Font.BOLD + Font.ITALIC, 16));
			
		String[] quizNames = new String[model.getQuizArray().length];
		for(int i = 0; i < model.getQuizArray().length; i++) {
			quizNames[i] = model.getQuizArray()[i].getName();
		}
		selectQuiz = new JComboBox<String>(quizNames);
		
		studentsConnected = new JTextArea();
		studentsConnected.setText("Students names will appear here when they're connected\n\nJohnSmith\nMarryBones\nJamesFisher");
		studentsConnected.setLineWrap(true);
		studentsConnected.setWrapStyleWord(true);
		studentsConnected.setMargin(new Insets(5,5,5,5));
		
		JScrollPane scroll = new JScrollPane(studentsConnected);
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
		setPreferredSize(new Dimension(400,300));
		
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0; c.gridy = 0; c.weightx = 0; c.weighty = 0.2; c.gridwidth = 2;
		add(label, c);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0; c.gridy = 1; c.weightx = 1; c.weighty = 1; c.gridwidth = 2;
		add(scroll, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0; c.gridy = 2; c.weightx = 0.5; c.weighty = 0.2; c.gridwidth = 1;
		add(selectQuiz, c);
		c.gridx = 1; c.gridy = 2; c.weightx = 1; c.weighty = 0.2; c.gridwidth = 1;
		add(start, c);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0; c.gridy = 3; c.weightx = 1; c.weighty = 0.1; c.gridwidth = 2;
		add(modifyQuiz, c);
		
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		
		QuizModel model = new QuizModel();
		model.setUsername("JohnSmith");
		Quiz[] q = new Quiz[1];
		q[0] = new Quiz();
		model.setQuizArray(q);
		
		JFrame frame = new JFrame("Admin Home");
		JPanel pane = new AdminHomeFrame(model);
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(pane);
		frame.pack();
		frame.setVisible(true);
	}


}
