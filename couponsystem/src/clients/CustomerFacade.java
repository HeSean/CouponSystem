package clients;

import java.util.ArrayList;
import java.util.Collection;

import coupon.Coupon;
import coupon.CouponDBDAO;
import customer.Customer;
import customer.CustomerDBDAO;
import exception.AlreadyBoughtException;
import exception.EmptyException;
import exception.ExpiredCouponException;
import exception.OutOfStockException;

public class CustomerFacade {

	CouponDBDAO couponDBDAO = new CouponDBDAO();
	CustomerDBDAO customerDBDAO = new CustomerDBDAO();

	public CustomerFacade() {
		// TODO Auto-generated constructor stub
	}

	public void purchaseCoupon(Customer customer, Coupon coupon)
			throws AlreadyBoughtException, OutOfStockException, ExpiredCouponException {
		try {
			if ((couponDBDAO.canBuy(customer, coupon)) == 1) {
				couponDBDAO.purchaseCoupon(customer, coupon);
			} else if ((couponDBDAO.canBuy(customer, coupon)) == 2) {
				throw new AlreadyBoughtException(coupon, customer);
			} else if ((couponDBDAO.canBuy(customer, coupon)) == 3) {
				throw new OutOfStockException(coupon);
			} else if ((couponDBDAO.canBuy(customer, coupon)) == 4) {
				throw new ExpiredCouponException(coupon, customer);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public Collection<Coupon> getPurchaseHistory(long id) throws Exception {
		ArrayList<Long> couponsID = (ArrayList<Long>) customerDBDAO.getCouponsID(id);
		ArrayList<Coupon> coupons = new ArrayList<Coupon>();

		if (!couponsID.isEmpty()) {
			coupons = (ArrayList<Coupon>) customerDBDAO.getCoupons(couponsID);
		}
			return coupons;
		}
	}

