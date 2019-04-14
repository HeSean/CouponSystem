package exception;

import coupon.Coupon;

public class OutOfStockException extends Exception {


	private static final long serialVersionUID = 1L;
	private Coupon coupon;
	private String msg;
	
	public OutOfStockException(Coupon coupon) {
		this.coupon = coupon;
	}

	public OutOfStockException(String msg) {
		setMsg(msg);
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		if (msg != null) {
			return "OutOfStockException of Coupon - \"" + coupon.getTitle()
			+ "\". There are no more coupons left in stock to delete, Please try again.";
		} else 
		return "OutOfStockException of Coupon - \"" + coupon.getTitle()
				+ "\". There are no more coupons left in stock, Please try again.";
	}

	
	
	
}
