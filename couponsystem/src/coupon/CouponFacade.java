package coupon;

import java.util.Collection;

public class CouponFacade {

	CouponDBDAO couponDBDAO = new CouponDBDAO();

	public void createCoupon(Coupon coupon) throws Exception {
		couponDBDAO.createCoupon(coupon);
	}

	public void removeCoupon(Coupon coupon) throws Exception {
		couponDBDAO.removeCoupon(coupon);
	}

	public void updateCoupon(Coupon coupon) throws Exception {
		couponDBDAO.updateCoupon(coupon);
	}

	public Coupon getCoupon(long id) throws Exception {
		 return couponDBDAO.getCoupon(id);
	}

	public Collection<Coupon> getAllCoupons() throws Exception {
		return couponDBDAO.getAllCoupons();
	}
	
	public Collection<Coupon> getCouponByType (CouponType cType) throws Exception{
		return couponDBDAO.getCouponByType(cType);
	}
}
