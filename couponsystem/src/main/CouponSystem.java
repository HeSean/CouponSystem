package main;

import exception.FailedConnectionException;
import facade.AdminFacade;
import facade.CompanyFacade;
import facade.CouponClientFacade;
import facade.CustomerFacade;
import facade.clientType;

// singleton
public class CouponSystem {

	private static  CouponSystem instance = null;
	private Thread update;
	private DailyCouponExpirationTask runnable;
	private AdminFacade adminFacade;
	private CustomerFacade customerFacade;
	private CompanyFacade companyFacade;

	private CouponSystem() throws FailedConnectionException {
		runnable = new DailyCouponExpirationTask();
		update = new Thread(runnable);
	}

	public static CouponSystem getInstance() throws FailedConnectionException {
		if (instance == null) {
			instance = new CouponSystem();
		}
		return instance;
	}

	public void startCouponsUpdater() {
		System.out.println("Starting to remove expired coupons");
		update.start();
	}

	public void stopTask() {
		System.out.println("Stopping thread in charge of removing expired coupons...");
		if (update != null) {
			runnable.terminate();
			System.out.println("Thread stopped");
		}
	}

	public CouponClientFacade login(String name, String password, clientType type) throws Exception {
		switch (type) {
		case ADMINISTRATOR:
			return adminFacade.login(name, password, type);
		case CUSTOMER:
			return customerFacade.login(name, password, type);
		case COMPANY:
			return companyFacade.login(name, password, type);
		default:
			return null;
		}
	}

}
