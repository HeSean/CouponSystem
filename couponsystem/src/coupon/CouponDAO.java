package coupon;

import java.util.Collection;

public interface CouponDAO {
		public void createCoupon (Coupon coupon) throws Exception;
		
		public void removeCoupon (Coupon coupon) throws Exception;
		
		public void updateCoupon (Coupon coupon) throws Exception;
		
		public Coupon getCoupon (long id) throws Exception;
		
		public Collection<Coupon> getAllCoupons() throws Exception;
		
		public Collection<Coupon> getCouponByType (CouponType cType) throws Exception;
}
