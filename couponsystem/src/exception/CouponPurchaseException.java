package exception;

import javabeans.Coupon;

public class CouponPurchaseException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private String message;
	private Coupon coupon;
	private int error;

	public CouponPurchaseException(String message) {
		super();
		setMessage(message);
	}

	public CouponPurchaseException(Coupon coupon, int error) {
		super();
		setCoupon(coupon);
		setError(error);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	@Override
	public String toString() {
		if (coupon != null) {
			if (error == 2) {
				return "CouponPurchaseException of coupon - \"" + coupon.getTitle()
						+ "\". There are no more coupons left in stock, Please try again.";
			} else {
				return "CouponPurchaseException of coupon - \"" + coupon.getTitle()
						+ "\".Wanted coupon has expired. Please try again.";
			}
		} else
			return "CouponPurchaseException - " + message;
	}

}
