package quizObject;

import java.util.ArrayList;
import java.io.Serializable;

public class AllScores implements Serializable {

	public static final long serialVersionUID = 42L;

	private ArrayList<Score> allScores;

	public AllScores(ArrayList<Score> allScores) {
		this.allScores = allScores;
	}

	public void setAllScores(ArrayList<Score> allScores) {
		this.allScores = allScores;
	}

	public ArrayList<Score> getAllScores() {
		return allScores;
	}

}
