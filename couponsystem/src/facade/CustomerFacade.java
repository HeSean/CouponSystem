package facade;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import dao.CouponDBDAO;
import dao.CustomerDBDAO;
import javabeans.Coupon;
import javabeans.CouponType;
import javabeans.Customer;
import exception.CouponPurchaseException;

public class CustomerFacade implements CouponClientFacade {

	private Customer customer;
	private CouponDBDAO couponDBDAO = new CouponDBDAO();
	private CustomerDBDAO customerDBDAO = new CustomerDBDAO();

	public CustomerFacade() {

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

	public Customer getCustomer(long id) throws Exception {
		return customerDBDAO.getCustomer(id);
	}

	public Collection<Customer> getAllCustomers() throws Exception {
		return customerDBDAO.getAllCustomers();
	}

	// customer purchase coupon
	public void purchaseCoupon(Customer customer, Coupon coupon) {
		try {
			if ((couponDBDAO.canBuy(customer, coupon)) == 1) {
				couponDBDAO.purchaseCoupon(customer, coupon);
			} else if ((couponDBDAO.canBuy(customer, coupon)) == 2) {
				throw new CouponPurchaseException(coupon, 2);
			} else if ((couponDBDAO.canBuy(customer, coupon)) == 3) {
				throw new CouponPurchaseException(coupon, 3);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Collection<Coupon> getCouponsPurchaseHistory() throws Exception {
		Collection<Long> couponsID = customerDBDAO.getCouponsID(customer.getId());
		return customerDBDAO.getCoupons(couponsID);
	}
	
	
	// get customers entire purchase history
	public Collection<Coupon> getPurchasedHistoryWithID(long id) throws Exception {
		LinkedHashSet<Long> couponsID = (LinkedHashSet<Long>) customerDBDAO.getCouponsID(id);
		LinkedHashSet<Coupon> coupons = new LinkedHashSet<Coupon>();
		if (!couponsID.isEmpty()) {
			coupons = (LinkedHashSet<Coupon>) customerDBDAO.getCoupons(couponsID);
		}
		return coupons;
	}
	
	// get customers purchase history By Type
	public Collection<Coupon> getAllPurchasedCouponsByType(CouponType couponType) throws Exception {
		System.out.println( "Previously purchased coupons of type " + couponType + " by customer " + customer.getCustName() + " are -");
		LinkedHashSet<Long> couponsID =  (LinkedHashSet<Long>) customerDBDAO.getCouponsID(customer.getId());
		LinkedHashSet<Coupon> coupons = new LinkedHashSet<Coupon>();
		if (!couponsID.isEmpty()) {
			coupons =  (LinkedHashSet<Coupon>) customerDBDAO.getCoupons(couponsID);
		}
		Iterator<Coupon> iterator = coupons.iterator();
		while (iterator.hasNext()) {
			Coupon coupon = (Coupon) iterator.next();
			if (!couponType.equals(coupon.getType())) {
				iterator.remove();
			}
		}
		return coupons;
	} 
	
	
	// get customers purchase history By Price
	public Collection<Coupon> getAllPurchasedCouponsByPrice(double price) throws Exception {
		System.out.println("Previously purchased coupons under " + price + "$ by customer "+ customer.getCustName() + " are -");
		LinkedHashSet<Long> couponsID = (LinkedHashSet<Long>) customerDBDAO.getCouponsID(customer.getId());
		LinkedHashSet<Coupon> coupons = new LinkedHashSet<Coupon>();
		if (!couponsID.isEmpty()) {
			coupons = (LinkedHashSet<Coupon>) customerDBDAO.getCoupons(couponsID);
		}
		Iterator<Coupon> iterator = coupons.iterator();
		while (iterator.hasNext()) {
			Coupon coupon = (Coupon) iterator.next();
			if (price <= coupon.getPrice()) {
				iterator.remove();
			}
		}
		return coupons;
	}

	@Override
	public CouponClientFacade login(String name, String password, clientType c) throws Exception {
		if (customerDBDAO.login(name, password)) {
			System.out.println("LOGIN of " + name);
			this.customer = customerDBDAO.getCustomer(name);
			return this;
		} else
			return null;
	}

}
