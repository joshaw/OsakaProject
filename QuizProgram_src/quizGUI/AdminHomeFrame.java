package quizGUI;

import quizMain.QuizClient;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;


/**
 * Class AdminHomeFrame is the display that the admin user will see once they have logged in.
 * @author bxc077
 * @version 20140314
 */
public class AdminHomeFrame extends MasterFrame implements Observer {

    private static final long serialVersionUID = 1L;

    // Model
    QuizClient model;

    // GUI
    JLabel label;
    JComboBox<Long> selectQuiz;
    JButton start = new JButton("Start");
    JButton modifyQuiz = new JButton("Create/Delete/Modify quiz");
    JTextArea studentsConnected;

    /**
     * Constructor
     */
    public AdminHomeFrame(QuizClient model) {
        this.model = model;
        setDisplay();
    }

    /**
     * Sets the display for the AdminHomeFrame
     */
    public void setDisplay() {

        label = new JLabel("Hi " + model.getUsername() + " welcome to the quiz");
        label.setFont(new Font("SansSerif", Font.BOLD + Font.ITALIC, 16));

        selectQuiz = new JComboBox<Long>(model.getQuizIDs());
        model.setCurrentQuizID((Long) selectQuiz.getSelectedItem());
        selectQuiz.addActionListener(new SelectQuizListener());

        start.addActionListener(new StartListener());

        studentsConnected = new JTextArea();
        studentsConnected.setText("Students names will appear here when they're connected\n\nJohnSmith\nMarryBones\nJamesFisher");
        studentsConnected.setLineWrap(true);
        studentsConnected.setWrapStyleWord(true);
        studentsConnected.setMargin(new Insets(5,5,5,5));

        JScrollPane scroll = new JScrollPane(studentsConnected);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        setPreferredSize(new Dimension(400,300));

        c.fill = GridBagConstraints.NONE;
        c.gridx = 0; c.gridy = 0; c.weightx = 0; c.weighty = 0.2; c.gridwidth = 2;
        add(label, c);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0; c.gridy = 1; c.weightx = 1; c.weighty = 1; c.gridwidth = 2;
        add(scroll, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 2; c.weightx = 0.5; c.weighty = 0.2; c.gridwidth = 1;
        add(selectQuiz, c);
        c.gridx = 1; c.gridy = 2; c.weightx = 1; c.weighty = 0.2; c.gridwidth = 1;
        add(start, c);
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0; c.gridy = 3; c.weightx = 1; c.weighty = 0.1; c.gridwidth = 2;
        add(modifyQuiz, c);

    }

    public class SelectQuizListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            long q = (Long) selectQuiz.getSelectedItem();
            // model.setCurrentQuizID(q); TODO THIS IS CORRECT
            model.setCurrentQuizID(q);
            System.out.println("you have selected a quiz");
        }
    }

    public class StartListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            model.adminStart(); // The QuizRequest object to be sent to the server...
        }
    }

    @Override
    public void update(Observable arg0, Object arg1) {
        // TODO Auto-generated method stub

    }
    
    /**
     * resetDisplay - when the panel is changed to this one, some of its components will need 
     * to be updated to represent the model
     */
	@Override
	public void resetDisplay() {
		
		// Resets the label info
        label.setText("Hi " + model.getUsername() + " welcome to the quiz");
	
        // TODO Show the students connecting here -> studentsConnected......
	}
    
    

}
