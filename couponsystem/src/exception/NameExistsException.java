package exception;

import javabeans.Company;
import javabeans.Coupon;
import javabeans.Customer;

public class NameExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String name;
	private Object object;

	public NameExistsException(String name, Object object) {
		super();
		setName(name);
		this.object = object;
	}

	public NameExistsException(String name) {
		super();
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
			return " \nNameExistsException of Comapny Name - \"" + name
					+ "\". There is already a company with that name, Please try again with a different name.";
		} else if (object instanceof Customer) {
			return " \nNameExistsException of Customer Name - \"" + name
					+ "\". There is already a customer with that name, Please try again with a different name.";
		} else if (object instanceof Coupon) {
			return " \nNameExistsException of Coupon Title - \"" + name
					+ "\". There is already a coupon with that name, Please try again with a different name.";
		} else
			return " \nNameExistsException of " + name + ". " + name
					+ " already exists.";

	}
}
