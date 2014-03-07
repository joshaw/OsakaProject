package quizObject;

import java.io.Serializable;

public class DisplayQuestion implements Serializable {

	public static final long serialVersionUID = 42L;

	private int number;

	public DisplayQuestion(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}

}
