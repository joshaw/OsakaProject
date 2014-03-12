package quizGUI;

import quizObject.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

/**
 * 
 * @author benji
 *
 */
public class CountDownTimer extends JPanel {

	private static final long serialVersionUID = 1L;
	
	// Timer
	private int initialSecs;
	private static int countSecs = 1;
	private static int seconds;
	private static Timer timer;	
	
	// GUI
	JProgressBar bar = new JProgressBar();
	JLabel label = new JLabel();
	
	/**
	 * Constructor
	 * @param seconds - how many seconds to count down from
	 */
	public CountDownTimer(int seconds) {
		CountDownTimer.seconds = seconds;
		initialSecs = seconds;
		timer = new Timer();
		setDisplay();
	}
	
	/**
	 * Sets the initial display for the CountDownTimer
	 */
	public void setDisplay() {
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		Dimension d = new Dimension(40,200);

		bar.setOrientation(SwingConstants.VERTICAL);
		bar.setPreferredSize(d);
		bar.setBorderPainted(false);
		
		c.weightx = 0; c.weighty = 1.0; 
		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0; c.gridy = 0; c.insets = new Insets(5,5,0,5);
		add(bar, c);
		c.weightx = 1.0; c.weighty = 0; 
		c.gridx = 0; c.gridy = 1; c.insets = new Insets(10,5,5,5);
		add(label, c);
		
		Color green = new Color(10, 100, 20);
		Color red = new Color(120, 10, 10);
		
		if(seconds > 3) {
			bar.setForeground(green);
		} else {
			bar.setForeground(red);
		}
		
		label.setText("" + seconds);
		bar.setValue(100 - (countSecs * 100) / (initialSecs * 10));	
	}
	
	/**
	 * Used to repaint the display every time the seconds are decremented
	 */
	public void reset() {
		Color green = new Color(10, 100, 20);
		Color red = new Color(120, 10, 10);
		
		if(seconds > 3) {
			bar.setForeground(green);
		} else {
			bar.setForeground(red);
		}
		
		label.setText("" + seconds);
		bar.setValue(100 - (countSecs * 100) / (initialSecs * 10));	
	}
	
	/**
	 * countDown is the method that runs the CountDownTimer. Counts down the seconds to 0.
	 * Uses helper methods - decrementTimer.
	 */
	public void countDown() {

		timer.scheduleAtFixedRate(new TimerTask() {

			public void run() {
				decrementTimer();
				reset();
			}

		}, 1000, 100);

	}

	/**
	 * decrementTimer is a helper method for the countDown method. 
	 * It decrements the seconds by one each time unless seconds=1 in which it will terminate the timer.
	 * @return int seconds
	 */
	private static void decrementTimer() {
		countSecs++;
		
		if (seconds < 1) {
			timer.cancel();
		}
		
		if(countSecs % 10 == 0) {
			seconds--;
		}
		
		
	}
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("Count Down Timer");
		CountDownTimer pane = new CountDownTimer(20);
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(pane);
		frame.pack();
		frame.setVisible(true);
		
		pane.countDown();
		
		
	}
	
	
	
}
