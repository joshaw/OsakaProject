package quizGUI;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * @author benji
 *
 */
public class TimerModel {

	private static  int seconds;
	private int initialSecs;
	private static Timer timer;
	
	/**
	 * Constructor
	 * @param int seconds, length of countdown timer
	 */
	public TimerModel(int seconds) {
		this.initialSecs = seconds; 
		this.seconds = seconds;
		timer = new Timer();
	}
	
	/**
	 * Getter for seconds
	 * @return int seconds
	 */
	public int getSeconds() {
		return seconds;
	}
	
	/**
	 * Setter for seconds
	 * @param int seconds, length of countdown timer
	 */
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	
	public int getInitialSecs() {
		return initialSecs;
	}
	
	/**
	 * Getter for timer
	 * @return timer
	 */
	public static Timer getTimer() {
		return timer;
	}
	
	/**
	 * countDown is the method that runs the TimerModel object by counting down the seconds to 0
	 * Uses helper methods - decrementTimer.
	 */
	public void countDown() {

		timer.scheduleAtFixedRate(new TimerTask() {

			public void run() {
				decrementTimer();
			}

		}, 1000, 1000);

	}

	/**
	 * decrementTimer is a helper method for the countDown method. 
	 * It decrements the seconds by one each time unless seconds=1 in which it will terminate the timer.
	 * @return int seconds
	 */
	private static final int decrementTimer() {
		if (seconds == 1) {
			timer.cancel();
		}
		return seconds--;
	}
	
	// main method tester.
	public static void main(String[] args) {
		TimerModel t = new TimerModel(10);
		t.countDown();
	}
}
