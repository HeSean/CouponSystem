package exception;

import coupon.Coupon;

public class OutOfStockException extends Exception {


	private static final long serialVersionUID = 1L;
	private Coupon coupon;
	
	public OutOfStockException(Coupon coupon) {
		this.coupon = coupon;
	}

	@Override
	public String toString() {
		return "OutOfStockException of Coupon - \"" + coupon.getTitle()
				+ "\". There are no more coupons left in stock, Please try again.";
	}
	
	
}
