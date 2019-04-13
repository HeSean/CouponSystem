package main;

import clients.AdminFacade;
import clients.CompanyFacade;
import clients.CouponClientFacade;
import clients.CustomerFacade;
import coupon.Coupon;
import coupon.CouponDBDAO;
import exception.EmptyException;

// singleton
public class CouponSystem {

	private  CouponDBDAO couponDBDAO;
	private AdminFacade adminFacade;
	private CustomerFacade customerFacade;
	private CompanyFacade companyFacade;
	private static CouponSystem cSystem = new CouponSystem();
	Thread update = new Thread(new DailyCouponExpirationTask());
	DailyCouponExpirationTask runnable = new DailyCouponExpirationTask();

	private CouponSystem() {
		try {
			if (!couponDBDAO.getAllCoupons().isEmpty()) {
				startCouponsUpdater();
			} else throw new EmptyException();
		} catch (EmptyException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static CouponSystem getInstance() {
		return cSystem;
	}

	
	public void startCouponsUpdater() {
		System.out.println("Starting to remove expired coupons");
		update.run();
	}
	
	public void stopTask() {
		System.out.println("Stopping thread in charge of removing expired coupons...");
		if (update != null) {
			runnable.terminate();
			System.out.println("Thread stopped");
		}
	}
	
	public void removeExpiredCoupon (Coupon coupon) throws Exception {
		couponDBDAO.removeCoupon(coupon);
	}
	
	public CouponClientFacade login(String name, String password, clientType type) throws Exception {
			switch(type) {
			case ADMINISTRATOR:
			   return adminFacade.login(name, password, type);
			case CUSTOMER:
			   return  customerFacade.login(name, password, type);
			case COMPANY:
				return companyFacade.login(name, password, type);
			}
			return adminFacade;
	}


}
