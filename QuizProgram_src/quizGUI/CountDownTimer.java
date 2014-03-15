package quizGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 * Class CountDownTimer is used as a visual count down in the QuestionFrame class
 * @author bxc077
 * @version 20140314
 */
public class CountDownTimer extends MasterFrame {

	private static final long serialVersionUID = 1L;
	
	// Timer
	private int initialSecs;
	private static int countSecs = 0;
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
		Dimension d = new Dimension(40,20);

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
		
		Color green = new Color(10, 120, 20);
		Color red = new Color(120, 10, 10);
		
		if(seconds > 3) {
			UIManager.put("nimbusOrange", green);
			bar.setForeground(green);
		} else {
			UIManager.put("nimbusOrange", red);
			bar.setForeground(red);
		}
		
		label.setText("" + seconds);
		bar.setValue(100 - (countSecs * 100) / (initialSecs * 10));	
	}
	
	/**
	 * Used to repaint the display every time the seconds are decremented
	 */
	public void reset() {
		Color green = new Color(10, 120, 20);
		Color red = new Color(120, 10, 10);
	
		if(seconds > 3) {
			UIManager.put("nimbusOrange", green);
			bar.setForeground(green);
		} else {
			UIManager.put("nimbusOrange", red);
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

		}, 400, 100);

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
			timer.purge();
		}
		
		if(countSecs % 10 == 0) {
			seconds--;
		}
		
		
	}	
	
	/**
	 * Resets the timer back to its original value.
	 */
	@Override
	public void resetDisplay() {
		countSecs = 0;
		seconds = initialSecs;
		timer.cancel();
		CountDownTimer.timer = new Timer();
		setDisplay();
	}


}
