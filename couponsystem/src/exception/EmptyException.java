package exception;

public class EmptyException extends Exception {
	private static final long serialVersionUID = 1L;
	private String name;

	public EmptyException() {
	}

	public EmptyException(String name) {
		setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		if (name != null) {
			return "EmptyException -  " + name ;
		} else 
		return "EmptyException - No objects were found in list";
	}

}
