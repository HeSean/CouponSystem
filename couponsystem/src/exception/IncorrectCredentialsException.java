package exception;

public class IncorrectCredentialsException extends Exception {
	private static final long serialVersionUID = 1L;
	private String msg;

	public IncorrectCredentialsException() {
		super();
	}

	public IncorrectCredentialsException(String msg) {
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
			return "IncorrectCredentialsException - " + msg;
		} else 
		return "IncorrectCredentialsException - No objects were found.";
	}


}
