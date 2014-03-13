package quizGUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;


/**
 * 
 * @author bxc077
 *
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
        
		waitingBar.setIndeterminate(true);
		
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 0;
		add(label, c);
		c.gridx = 0; c.gridy = 1;
		add(waitingBar, c);

	}
	
	
	// Main method test
	public static void main(String[] args) {
		JFrame frame = new JFrame("Waiting");
		WaitingFrame pane = new WaitingFrame();
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(pane);
		frame.pack();
		frame.setVisible(true);
		
		
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}