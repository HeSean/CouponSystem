package exception;

public class NotExistsException extends Exception {
	private static final long serialVersionUID = 1L;
	private String msg;

	public NotExistsException() {
	}

	public NotExistsException(String msg) {
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
			return "NotExistsException - " + msg;
		} else 
		return "NotExistsException - No objects were found in list";
	}

}
