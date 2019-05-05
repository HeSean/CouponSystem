package exception;

public class EmptyException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	//private String name;

	// public EmptyException() {
	// super();
	// }

	public EmptyException(String msg) {
		super(msg);
		// setName(name);
	}

	// public void setName(String name) {
	// this.name = name;
	// }

//	@Override
//	public String toString() {
//		 if (name != null) {
//		 return "EmptyException - " + name ;
//		 } else
//		 return "EmptyException - No objects were found in list";
//		 }
	}

