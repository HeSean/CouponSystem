package exception;

public class WrongInfoInsertedException extends Exception {
	private static final long serialVersionUID = 1L;
	private String msg;

	public WrongInfoInsertedException() {
	}

	public WrongInfoInsertedException(String msg) {
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
			return "WrongInfoInsertedException - " + msg;
		} else 
		return "WrongInfoInsertedException - No objects were found.";
	}


}
