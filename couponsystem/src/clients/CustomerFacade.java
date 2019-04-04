package clients;

import coupon.Coupon;
import coupon.CouponDBDAO;
import customer.Customer;
import exception.AlreadyBoughtException;
import exception.ExpiredCouponException;
import exception.OutOfStockException;

public class CustomerFacade {

	CouponDBDAO couponDBDAO = new CouponDBDAO();

	public CustomerFacade() {
		// TODO Auto-generated constructor stub
	}

	public void purchaseCoupon(Customer customer,Coupon coupon) throws  AlreadyBoughtException,OutOfStockException,ExpiredCouponException {
		try {
			if ((couponDBDAO.canBuy(customer ,coupon)) == 1) {
				couponDBDAO.purchaseCoupon(customer, coupon);
			} else if ((couponDBDAO.canBuy(customer ,coupon)) == 2) {
				throw new AlreadyBoughtException(coupon, customer);
			} else if ((couponDBDAO.canBuy(customer ,coupon)) == 3) {
				throw new OutOfStockException(coupon);
			} else if ((couponDBDAO.canBuy(customer ,coupon)) == 4) {
				throw new ExpiredCouponException(coupon, customer);
			}
			} catch (Exception e) {
			System.out.println(e);
		}
	}
}
