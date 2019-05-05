package exception;

public class IncorrectCredentialsException extends Exception {
	private static final long serialVersionUID = 1L;
	String msg;

	public IncorrectCredentialsException() {
	}

	public IncorrectCredentialsException(String msg) {
		super(msg);
		//this.msg = msg;
	}

//	@Override
//	public String toString() {
//		if (msg != null) {
//			return "IncorrectCredentialsException - " + msg;
//		} else
//			return "IncorrectCredentialsException - No objects were found.";
//	}

}
