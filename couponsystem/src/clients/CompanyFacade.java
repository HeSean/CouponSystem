package clients;

import java.util.Collection;

import coupon.Coupon;
import coupon.CouponDBDAO;
import coupon.CouponType;
import exception.NameExistsException;

public class CompanyFacade {
	
	CouponDBDAO couponDBDAO = new CouponDBDAO();
	
	public CompanyFacade() {
		// TODO Auto-generated constructor stub
	}
	
	public void createCoupon(Coupon coupon) throws NameExistsException {
		try {
			if (!couponDBDAO.checkCouponName(coupon)) {
				couponDBDAO.createCoupon(coupon);
			} else
				throw new NameExistsException(coupon.getTitle(), coupon);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void removeCoupon(Coupon coupon) throws Exception {
		couponDBDAO.removeCoupon(coupon);
		//missing removal of coupons purchased by customers
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
