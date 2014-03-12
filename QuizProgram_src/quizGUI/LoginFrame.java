package quizGUI;

import quizObject.*;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import quizObject.*;

/**
 * Login GUI is the initial screen the user will see.
 * User enters login details and then pushes login button.
 * When login button is pushed, the details will be made into a 
 * LoginRequest object by the model. This is then sent to the server
 * via the client.
 * 
 * @author Benjamin Crispin
 * @version 20140306
 */
public class LoginFrame extends JPanel implements Observer {
	
	private static final long serialVersionUID = 1L;
	
	// Login model
	private QuizModel model;
	
	// GUI Components
	private JLabel titleLabel = new JLabel("Log in to quiz");
	private JLabel usernameLabel = new JLabel("User Name:"); 
	private JLabel passwordLabel = new JLabel("Password:");
	private JTextField usernameField = new JTextField(10);
	private JPasswordField passwordField = new JPasswordField(10);
	private JButton loginButton = new JButton("Log in");
	
	/**
	 * Constructor
	 */
	public LoginFrame(QuizModel model) {
		this.model = model;
		setDisplay();	
	}
	
	/**
	 * setDisplay
	 * Sets the display for login
	 */
	public void setDisplay() {
		
		JPanel grid = new JPanel(new GridLayout(2,2,10,5));
		titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
		
		setLayout(new GridLayout(3,1,5,10));
		
		grid.add(usernameLabel);
		grid.add(usernameField);
		grid.add(passwordLabel);
		grid.add(passwordField);
		
		loginButton.addActionListener(new LoginListener());
		
		add(titleLabel);
		add(grid);
		add(loginButton);

	}
	
	/**
	 * Action Listener for the login button
	 */
	public class LoginListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			model.setUsername(usernameField.getText());
			model.setPassword(passwordField.getPassword());
			// LoginRequest log1 = model.requestLogin(); // the LoginRequest object to be sent to the server	
			model.requestLogin();
			System.out.println(log1.getUsername() + "\n" + log1.getPasswordHash());
		}
	}
	
	
	@Override // <----- ##### NEED TO DEAL WITH!!
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
	
	// Main method tester
	public static void main(String[] args) {
		QuizModel model = new QuizModel();
		
		JFrame frame = new JFrame("Log in");
		JPanel pane = new LoginFrame(model);
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(pane);
		frame.pack();
		frame.setVisible(true);

	}
	
}
