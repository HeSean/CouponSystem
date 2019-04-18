package exception;

public class FailedConnectionException extends Exception {
	private static final long serialVersionUID = 1L;
	private String msg;

	public FailedConnectionException() {
		super();
	}

	public FailedConnectionException(String msg) {
		super();
		setMsg(msg);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String name) {
		this.msg = name;
	}

	@Override
	public String toString() {
		if (msg != null) {
			return "FailedConnectionException - " + msg;
		} else 
		return "FailedConnectionException - Failed to connect to database.";
	}

}
