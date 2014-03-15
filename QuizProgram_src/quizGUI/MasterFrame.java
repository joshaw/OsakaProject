package quizGUI;

import javax.swing.JPanel;

/**
 * MasterFrame is an abstract class which extends JPanel.
 * All GUI components in the quizGUI extend the MasterFrame as each component needs a resetDisplay method.
 * @author bxc077
 * @version 20140315
 */
public abstract class MasterFrame extends JPanel {

	private static final long serialVersionUID = 1L;

	// Every GUI screen will have a resetDisplay method
	public abstract void resetDisplay();
	
}
