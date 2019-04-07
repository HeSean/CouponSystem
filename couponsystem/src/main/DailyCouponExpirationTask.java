package main;

import java.util.Collection;
import java.util.Date;

import clients.CouponSystem;
import coupon.Coupon;

public class DailyCouponExpirationTask implements Runnable {

	private Collection<Coupon> coupons;
	private volatile boolean running = true;

	public void terminate() {
		running = false;
	}

	public DailyCouponExpirationTask() {

	}

	public DailyCouponExpirationTask(Collection<Coupon> coupons) {
		setCoupons(coupons);
	}

	public Collection<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(Collection<Coupon> coupons) {
		this.coupons = coupons;
	}

	@Override
	public void run() {
		// get all coupons --> for each ----> if end date expired remove coupon
		while (running) {
			if (!coupons.isEmpty()) {
				for (Coupon coupon : coupons) {
					if (coupon.getEndDate().after(new Date())) {
						try {
							CouponSystem.getInstance().removeExpiredCoupon(coupon);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							Thread.sleep(100 * 60 * 60 * 24);
						} catch (InterruptedException e) {
							e.printStackTrace();
							running = false;
						}
					}
				}
			}

		}
	}
}
