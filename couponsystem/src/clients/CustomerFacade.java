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

public class CustomerFacade implements CouponClientFacade {

	CouponDBDAO couponDBDAO = new CouponDBDAO();
	CustomerDBDAO customerDBDAO = new CustomerDBDAO();

	public CustomerFacade() {
		// TODO Auto-generated constructor stub
	}

	public void createCustomer(Customer customer) throws Exception {
		customerDBDAO.createCustomer(customer);
	}

	public void removeCustomer(Customer customer) throws Exception {
		customerDBDAO.removeCustomer(customer);
	}

	public void updateCustomer(Customer customer) throws Exception {
		customerDBDAO.updateCustomer(customer);
	}

	public void getCustomer(long id) throws Exception {
		customerDBDAO.getCustomer(id);
	}

	public void getAllCustomers() throws Exception {
		customerDBDAO.getAllCustomers();
	}

	// customer purcahse coupon
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

	// get customers entire purchase history
	public Collection<Coupon> getPurchaseHistory(long id) throws Exception {
		ArrayList<Long> couponsID = (ArrayList<Long>) customerDBDAO.getCouponsID(id);
		ArrayList<Coupon> coupons = new ArrayList<Coupon>();

		if (!couponsID.isEmpty()) {
			coupons = (ArrayList<Coupon>) customerDBDAO.getCoupons(couponsID);
		}
		return coupons;
	}

	@Override
	public CouponClientFacade login(String name, String password, clientType c) throws Exception {
		CustomerFacade customerFacade = new CustomerFacade();
		if (customerDBDAO.login(name, password)) {
			return customerFacade;
		} else
			return null;
	}

}
