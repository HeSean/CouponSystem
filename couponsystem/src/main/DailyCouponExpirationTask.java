package main;

import dao.CouponDBDAO;
import exception.EmptyException;
import exception.FailedConnectionException;

public class DailyCouponExpirationTask implements Runnable {

	private static boolean running = true;
	private CouponDBDAO couponDBDAO;

	public DailyCouponExpirationTask() throws FailedConnectionException {
		couponDBDAO = new CouponDBDAO();
	}

	public void terminate() {
		running = false;
	}

	@Override
	public void run() {
		// get all coupons --> for each ----> if end date expired remove coupon
		while (running) {
			try {
				if (!couponDBDAO.getAllCoupons().isEmpty()) {
					couponDBDAO.findExpiredCoupons();
				} else {
					running = false;
					throw new EmptyException("no coupons to run thread on..");
				}
			} catch (EmptyException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				System.out.println("Thread going to sleep");
				//Thread.sleep(100 * 60 * 60 * 24);
				 Thread.sleep(100 * 10 * 10);
			} catch (InterruptedException e) {
				running = false;
				e.printStackTrace();
			}
		}
	}

}
