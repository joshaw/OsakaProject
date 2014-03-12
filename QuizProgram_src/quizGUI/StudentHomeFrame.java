package quizGUI;

import quizObject.*;
import quizMain.QuizClient;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

/**
 * The home screen view once a student has logged in.
 *
 * @author Benjamin Crispin
 * @version 20140307
 */
public class StudentHomeFrame extends JPanel implements Observer {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	// Model
	QuizClient model;

	// GUI
	private JLabel title;
	private JButton start = new JButton("Start Quiz");
	private JButton viewResults = new JButton("View Results");
	private JButton updateProfile = new JButton("Update Profile");
	private JButton howToPlay = new JButton("How to play");

	/**
	 * Constructor
	 */
	public StudentHomeFrame(QuizClient model) {
		this.model = model;

		setDisplay();

		JFrame frame = new JFrame("Student Home");

		JFrame.setDefaultLookAndFeelDecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(this);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * setDisplay
	 * Sets the display for StudentHomeFrame
	 */
	public void setDisplay() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		JPanel buttons = new JPanel(new GridLayout(4,1,5,10));
		Dimension d = new Dimension(136, 130);

		title = new JLabel("Hi " + model.getUsername() + " welcome to the quiz");
		title.setFont(new Font("SansSerif", Font.BOLD + Font.ITALIC, 16));

		buttons.add(start);
		buttons.add(viewResults);
		buttons.add(updateProfile);
		buttons.add(howToPlay);

		start.addActionListener(new StartListener());
		viewResults.addActionListener(new ViewResultsListener());
		updateProfile.addActionListener(new UpdateProfileListener());
		howToPlay.addActionListener(new HowToPlayListener());

		buttons.setMaximumSize(d);
		buttons.setAlignmentX(CENTER_ALIGNMENT);
		c.gridx = 0; c.gridy = 0; c.insets = new Insets(20,20,0,20);
		add(title, c);
		c.gridx = 0; c.gridy = 1; c.insets = new Insets(10,20,20,20);
		add(buttons, c);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	/**
	 * Action Listener for the start button
	 */
	public class StartListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// INSERT START ACTION HERE!!!
			System.out.println("Start");
		}
	}

	/**
	 * Action Listener for the viewResults button
	 */
	public class ViewResultsListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// INSERT START ACTION HERE!!!
			System.out.println("View Results");
		}
	}

	/**
	 * Action Listener for the updateProfile button
	 */
	public class UpdateProfileListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// INSERT START ACTION HERE!!!
			System.out.println("Update Profile");
		}
	}

	/**
	 * Action Listener for the howToPlay button
	 */
	public class HowToPlayListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// INSERT START ACTION HERE!!!
			System.out.println("How To Play");
		}
	}

	// Main method tester
	public static void main(String[] args) {

		QuizClient model = new QuizClient();
		// model.setUsername("JohnSmith");

		JFrame frame = new JFrame("Student Home");
		JPanel pane = new StudentHomeFrame(model);

		JFrame.setDefaultLookAndFeelDecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(pane);
		frame.pack();
		frame.setVisible(true);

	}

}
