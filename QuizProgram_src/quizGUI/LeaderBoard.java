package quizGUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import quizMain.QuizClient;
import quizObject.Score;

/**
 *
 * @author bxc077
 * @version 20140315
 */
public class LeaderBoard extends MasterFrame implements Observer {
   
    private static final long serialVersionUID = 1L;
   
    private ArrayList<Score> allScores;
    private JTable table;
   
    /**
     * Constructor
     * @param model
     */
    public LeaderBoard(ArrayList<Score> allScores) {
        this.allScores = allScores;
        setDisplay();
    }
   
    /**
     * Sets the display for LeaderBoard
     */
    public void setDisplay() {
       
        // The pane layout
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
       
        int columns = 3;
        int rows = allScores.size();

        String[] columnNames = {"Position", "Username", "Score"};
        String[][] data = new String[rows][columns];
       
        // Create 2D array for table from ArrayList<Score> allScores
        for(int i = 0; i < rows; i++) {
            data[i][0] = i + 1 + "";
            data[i][1] = allScores.get(i).getUsername();
            data[i][2] = allScores.get(i).getMark() + "";
        }
       
        // Set up the table
        table = new JTable(data, columnNames);
        table.setFillsViewportHeight(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(70);

        // Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setMinimumSize(new Dimension(260,20));
       // scrollPane.setMaximumSize(new Dimension(260,20));
        scrollPane.setPreferredSize(new Dimension(260,20));
       
        // Add the scroll pane to this panel.
        c.weightx = 0; c.weighty = 1.0;
        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 0; c.gridy = 0;
        add(scrollPane, c);
    }
   
   
   
    public static void main(String[] args) {       
       
        QuizClient client = new QuizClient();
       
        JFrame frame = new JFrame("Quiz");
        LeaderBoard pane = new LeaderBoard(client.getAllScores());
       
       
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame.setContentPane(pane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
       
    }
   
    @Override
    public void update(Observable arg0, Object arg1) {
        // TODO Auto-generated method stub
       
    }

    @Override
    public void resetDisplay() {
        // TODO Auto-generated method stub
       
    }

}