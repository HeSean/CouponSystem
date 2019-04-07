package clients;

import java.util.Collection;

import coupon.Coupon;
import coupon.CouponDBDAO;
import exception.EmptyException;
import main.DailyCouponExpirationTask;

// singleton
public class CouponSystem {

	private static CouponDBDAO couponDBDAO;
	private Collection<Coupon> coupons;
	private static CouponSystem instance = new CouponSystem();
	Thread update = new Thread(new DailyCouponExpirationTask(coupons));
	DailyCouponExpirationTask runnable = new DailyCouponExpirationTask();

	private CouponSystem() {
		try {
			if (!couponDBDAO.getAllCoupons().isEmpty()) {
				coupons = couponDBDAO.getAllCoupons();
				startCouponsUpdater();
			} else throw new EmptyException();
		} catch (EmptyException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static CouponSystem getInstance() {
		return instance;
	}
	
	public Collection<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(Collection<Coupon> coupons) {
		this.coupons = coupons;
	}
	
	public void startCouponsUpdater() {
		System.out.println("Starting to remove expired coupons");
		update.run();
	}
	
	public void stopTask() {
		System.out.println("Stopping thread in charge of removing expired coupons");
		if (update != null) {
			runnable.terminate();
			System.out.println("Thread stopped");
		}
	}
	
	public void removeExpiredCoupon (Coupon coupon) throws Exception {
		couponDBDAO.removeCoupon(coupon);
	}


}
