package quizGUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;


/**
 * Class WaitingFrame is the display the student will see when they have clicked "Start Quiz"
 * and are waiting for the admin to start the quiz.
 * @author bxc077
 * @version 20140314
 */
public class WaitingFrame extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;
	
	// GUI components
	JProgressBar waitingBar = new JProgressBar();
	JLabel label = new JLabel("Please wait for admin to start quiz");
	
	/**
	 * Constructor
	 */
	public WaitingFrame() {
		setDisplay();
	}
	
	/**
	 * Sets the display for the WaitingFrame
	 */
	public void setDisplay() {
		
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        UIManager.put("nimbusOrange", new Color(10, 100, 20));
        
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
		waitingBar.setIndeterminate(true);
		
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 0;
		add(label, c);
		c.insets = new Insets(10,0,0,0);
		c.gridx = 0; c.gridy = 1;
		add(waitingBar, c);

	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
}