package quizGUI;

import quizObject.*;
import quizMain.QuizClient;

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
    private QuizClient model;

    // GUI Components
    private JLabel titleLabel = new JLabel("Log in to quiz");
    private JLabel usernameLabel = new JLabel("User Name:");
    private JLabel passwordLabel = new JLabel("Password:");
    private JTextField usernameField = new JTextField(10);
    private JPasswordField passwordField = new JPasswordField(10);
    private JButton loginButton = new JButton("Log in");
	private JFrame frame;

    /**
     * Constructor
     */
    public LoginFrame(QuizClient model) {
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

	public JFrame getFrame() {
		return frame;
	}

    /**
     * Action Listener for the login button
     */
    public class LoginListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            model.setUsername(String.valueOf(usernameField.getText()));
            model.setPassword(String.valueOf(passwordField.getPassword()));
            model.requestLogin();
        }
    }

    @Override // <----- ##### NEED TO DEAL WITH!!
    public void update(Observable arg0, Object arg1) {
        // TODO Auto-generated method stub

    }

}
