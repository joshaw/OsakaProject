package quizGUI;

import quizMain.QuizClient;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 * Login GUI is the initial screen the user will see.
 * User enters login details and then pushes login button.
 * When login button is pushed, the details will be made into a
 * LoginRequest object by the model. This is then sent to the server
 * via the client.
 *
 * @author bxc077
 * @version 20140306
 */
public class LoginFrame extends MasterFrame implements Observer {

    private static final long serialVersionUID = 1L;

    // Login model
    private QuizClient model;

    // GUI Components
    private JLabel titleLabel = new JLabel("Log in to Edify!");
    private JLabel usernameLabel = new JLabel("User Name:");
    private JLabel passwordLabel = new JLabel("Password:");
    private JTextField usernameField = new JTextField(15);
    private JPasswordField passwordField = new JPasswordField(15);
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
        UIManager.put("control", new Color(230,204,255));

        JPanel view = new JPanel();

        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        usernameLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        loginButton.addActionListener(new LoginListener());

        view.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        view.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        c.fill = GridBagConstraints.NONE;
        c.gridx = 0; c.gridy = 0; c.weightx = 0; c.weighty = 0.1; c.gridwidth = 2;
        view.add(titleLabel, c);
        c.insets = new Insets(10,0,0,0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 1; c.weightx = 0; c.weighty = 0; c.gridwidth = 1;
        view.add(usernameLabel, c);
        c.gridx = 1; c.gridy = 1; c.weightx = 0; c.weighty = 0; c.gridwidth = 1;
        view.add(usernameField, c);
        c.insets = new Insets(0,0,0,0);
        c.gridx = 0; c.gridy = 2; c.weightx = 0; c.weighty = 0; c.gridwidth = 1;
        view.add(passwordLabel, c);
        c.gridx = 1; c.gridy = 2; c.weightx = 0; c.weighty = 0; c.gridwidth = 1;
        view.add(passwordField, c);
        c.insets = new Insets(10,0,0,0);
        c.fill = GridBagConstraints.CENTER;
        c.gridx = 0; c.gridy = 3; c.weightx = 0; c.weighty = 0.1; c.gridwidth = 2;

        view.add(loginButton, c);
        add(view);

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

    /**
     * resetDisplay - when the panel is changed to this one, some of its components will need
     * to be updated to represent the model
     */
    @Override
    public void resetDisplay() {
        usernameField.setText("");
        passwordField.setText("");

    }

}
