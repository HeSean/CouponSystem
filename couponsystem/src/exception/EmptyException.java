package exception;

public class EmptyException extends Exception {
	private static final long serialVersionUID = 1L;

	
	public EmptyException() {

	}

	@Override
	public String toString() {
		return "EmptyException - No objects were found in list";
	}
}
