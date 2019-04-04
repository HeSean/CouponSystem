package exception;

import coupon.Coupon;
import customer.Customer;

public class AlreadyBoughtException extends Exception {
	private static final long serialVersionUID = 1L;
	private Coupon coupon;
	private Customer customer;
	
	public AlreadyBoughtException(Coupon coupon, Customer customer) {
		this.coupon = coupon;
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "AlreadyBoughtException of Customer - \"" + customer.getCustName()
				+ "\". Customer already bought this coupon (" + coupon.getTitle() +") in the past. ";
	}
	
}
