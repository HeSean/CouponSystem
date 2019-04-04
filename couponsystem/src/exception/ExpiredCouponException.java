package exception;

import coupon.Coupon;
import customer.Customer;

public class ExpiredCouponException extends Exception {
	private static final long serialVersionUID = 1L;
	private Coupon coupon;
	private Customer customer;
	
	public ExpiredCouponException(Coupon coupon, Customer customer) {
		this.coupon = coupon;
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "ExpiredCouponException of Coupon - \"" + coupon.getTitle() 
				+ "\". wanted coupon has expired. ";
	}
}
