package quizObject;

import java.io.Serializable;

public class AnswerResponse implements Serializable {

	public static final long serialVersionUID = 42L;

	private int response;
	private long responseTime;

	public AnswerResponse(int response) {
		this.response = response;
		this.responseTime = System.currentTimeMillis();
	}

	/**
	 * @param response integer from 0 to 3, user choice for answer.
	 */
	public void setResponse(int response) {
		this.response = response;
	}

	/**
	 * @return a number from 0 to 3 specifying which of the options the user
	 * answered with.
	 */
	public int getResponse() {
		return response;
	}

	/**
	 * @param responseTime set the time for the user to answer a question
	 */
	public void setResponseTime(long responseTime) {
		this.responseTime = responseTime;
	}

	/**
	 * @return time taken from receiving the question to the user clicking on
	 * the answer.
	 */
	public long getResponseTime() {
		return responseTime;
	}

}
