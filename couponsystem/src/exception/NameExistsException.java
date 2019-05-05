package exception;

import javabeans.Company;
import javabeans.Coupon;
import javabeans.Customer;

public class NameExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String name;
	private Object object;

	public NameExistsException(String name, Object object) {
		super(name);
		setName(name);
		this.object = object;
	}

	public NameExistsException(String name) {
		super(name);
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
		if (object instanceof Company) {
			return "NameExistsException of Comapny Name - \"" + name
					+ "\". There is already a company with that name, Please try again with a different name.";
		} else if (object instanceof Customer) {
			return "NameExistsException of Customer Name - \"" + name
					+ "\". There is already a customer with that name, Please try again with a different name.";
		} else if (object instanceof Coupon) {
			return "NameExistsException of Coupon Title - \"" + name
					+ "\". There is already a coupon with that name, Please try again with a different name.";
		} else
			return "NameExistsException of " + name + ". " + name
					+ " already exists.";

	}
}
