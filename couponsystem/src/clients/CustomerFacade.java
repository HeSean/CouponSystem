package clients;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import coupon.Coupon;
import coupon.CouponDBDAO;
import coupon.CouponType;
import customer.Customer;
import customer.CustomerDBDAO;
import exception.WrongInfoInsertedException;
import exception.ExpiredCouponException;
import exception.OutOfStockException;

public class CustomerFacade implements CouponClientFacade {

	private Customer customer;
	private CouponDBDAO couponDBDAO = new CouponDBDAO();
	private CustomerDBDAO customerDBDAO = new CustomerDBDAO();

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

	public Customer getCustomer(long id) throws Exception {
		return customerDBDAO.getCustomer(id);
	}

	public Collection<Customer> getAllCustomers() throws Exception {
		return customerDBDAO.getAllCustomers();
	}

	// customer purchase coupon
	public void purchaseCoupon(Customer customer, Coupon coupon) throws OutOfStockException, ExpiredCouponException {
		try {
			if ((couponDBDAO.canBuy(customer, coupon)) == 1) {
				couponDBDAO.purchaseCoupon(customer, coupon);
			} else if ((couponDBDAO.canBuy(customer, coupon)) == 2) {
				throw new OutOfStockException(coupon);
			} else if ((couponDBDAO.canBuy(customer, coupon)) == 3) {
				throw new ExpiredCouponException(coupon, customer);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public Collection<Coupon> getCouponsPurchaseHistory() throws Exception {
		ArrayList<Long> couponsID = customerDBDAO.getCouponsID(customer.getId());
		return customerDBDAO.getCoupons(couponsID);
	}
	
	
	// get customers entire purchase history
	public Collection<Coupon> getAllPurchasedHistory(long id) throws Exception {
		ArrayList<Long> couponsID = (ArrayList<Long>) customerDBDAO.getCouponsID(id);
		ArrayList<Coupon> coupons = new ArrayList<Coupon>();
		if (!couponsID.isEmpty()) {
			coupons = (ArrayList<Coupon>) customerDBDAO.getCoupons(couponsID);
		}
		return coupons;
	}
	
	// get customers purchase history By Type
	public Collection<Coupon> getAllPurchasedCouponsByType(CouponType couponType) throws Exception {
		System.out.println( "Previously purchased coupons of type " + couponType + " by customer " + customer.getCustName() + " are -");
		
		// through db
		ArrayList<Long> couponsID = (ArrayList<Long>) customerDBDAO.getCouponsID(customer.getId());
		ArrayList<Coupon> coupons = new ArrayList<Coupon>();
		if (!couponsID.isEmpty()) {
			coupons = (ArrayList<Coupon>) customerDBDAO.getCoupons(couponsID);
		}
		Iterator<Coupon> iterator = coupons.iterator();
		while (iterator.hasNext()) {
			Coupon coupon = (Coupon) iterator.next();
			if (!couponType.equals(coupon.getType())) {
				iterator.remove();
			}
		}
		//return coupons;
		
		// through java beans
		ArrayList<Coupon> couponsz = new ArrayList<Coupon>();
		couponsz = customer.getCoupons();
		if (!couponsz.isEmpty()) {
		Iterator<Coupon> iteratorz = couponsz.iterator();
		while (iteratorz.hasNext()) {
			Coupon coupon = (Coupon) iteratorz.next();
			if (!couponType.equals(coupon.getType())) {
				iteratorz.remove();
			}
		}
		return couponsz;
		} else throw new WrongInfoInsertedException("No coupons found for this customer - " + customer.getCustName());
	} 
	
	
	// get customers purchase history By Price
	public Collection<Coupon> getAllPurchasedCouponsByPrice(double price) throws Exception {
		System.out.println("Customer " + customer.getCustName() + " previously purchased coupons under " + price + "$ are -");
		ArrayList<Long> couponsID = (ArrayList<Long>) customerDBDAO.getCouponsID(customer.getId());
		ArrayList<Coupon> coupons = new ArrayList<Coupon>();
		if (!couponsID.isEmpty()) {
			coupons = (ArrayList<Coupon>) customerDBDAO.getCoupons(couponsID);
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
