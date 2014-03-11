package quizObject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

/**
 * 
 * @author benji
 *
 */
public class TimerFrame extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	TimerModel model;
	
	JProgressBar bar = new JProgressBar();
	JLabel label = new JLabel();
	
	/**
	 * Constructor
	 * @param model - the TimerModel which the GUI is observing
	 */
	public TimerFrame(TimerModel model) {
		this.model = model;
		setDisplay();
		model.countDown();
	}
	
	/**
	 * Getter for TimermModel
	 * @return model (TimerModel)
	 */
	public TimerModel getModel() {
		return model;
	}
	
	/**
	 * Setter for the model
	 * @param model the TimerModel to use
	 */
	public void setModel(TimerModel model) {
		this.model = model;
	}
	
	/**
	 * Sets the display for the TimerFrame
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
		
		if(model.getSeconds() > 3) {
			bar.setForeground(green);
		} else {
			bar.setForeground(red);
		}
		
		label.setText("" + model.getSeconds());
		bar.setValue((model.getSeconds() * 100) / model.getInitialSecs());	
	}
	
	
	
	public void reset() {
		Color green = new Color(10, 100, 20);
		Color red = new Color(120, 10, 10);
		
		if(model.getSeconds() > 3) {
			bar.setForeground(green);
		} else {
			bar.setForeground(red);
		}
		
		label.setText("" + model.getSeconds());
		bar.setValue((model.getSeconds() * 100) / model.getInitialSecs());
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
	
	// Main method tester
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("Log in");
		TimerFrame pane = new TimerFrame(new TimerModel(10));
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(pane);
		frame.pack();
		frame.setVisible(true);
		
		while(true){pane.reset();}
		
	}
	
	
	
}
