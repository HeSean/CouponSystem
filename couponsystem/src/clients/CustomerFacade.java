package clients;

import coupon.Coupon;
import coupon.CouponDBDAO;
import exception.OutOfStockException;

public class CustomerFacade {

	CouponDBDAO couponDBDAO = new CouponDBDAO();

	public CustomerFacade() {
		// TODO Auto-generated constructor stub
	}

	public void purchaseCoupon(Coupon coupon) throws OutOfStockException {
		try {
			if (couponDBDAO.canBuy(coupon)) {
				// couponDBDAO.purchase(coupon);
			} else
				throw new OutOfStockException(coupon);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
