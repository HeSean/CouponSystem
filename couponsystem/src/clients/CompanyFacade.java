package clients;

import java.util.Collection;

import company.Company;
import company.CompanyDBDAO;
import coupon.Coupon;
import coupon.CouponDBDAO;
import coupon.CouponType;
import exception.NameExistsException;

public class CompanyFacade implements CouponClientFacade {

	CouponDBDAO couponDBDAO = new CouponDBDAO();
	CompanyDBDAO companyDBDAO = new CompanyDBDAO();

	public CompanyFacade() {
		// TODO Auto-generated constructor stub
	}

	// Method for creating a new coupon
	public void createCoupon(Coupon coupon, long compID) throws NameExistsException {
		try {
			if (!couponDBDAO.checkCouponName(coupon)) {
				couponDBDAO.createCoupon(coupon);
				Company company = companyDBDAO.getCompany(compID);
				couponDBDAO.insertCouponToJoinTable(coupon.getId(), compID);
				company.addCoupon(coupon);
			} else
				throw new NameExistsException(coupon.getTitle(), coupon);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// Method for removing coupon
	public void removeCoupon(Coupon coupon) throws Exception {
		couponDBDAO.removeCoupon(coupon);
	}

	// Method for updating coupon details - end date or price
	public void updateCoupon(Coupon coupon) throws Exception {
		couponDBDAO.updateCoupon(coupon);
	}

	public Coupon getCoupon(long id) throws Exception {
		return couponDBDAO.getCoupon(id);
	}

	 public Collection<Coupon> getAllCoupons() throws Exception {
		 
	 }

	public Collection<Coupon> getCouponByType(CouponType cType) throws Exception {
		Collection<Coupon> coupons = couponDBDAO.getCouponByType(cType);
		if (!coupons.isEmpty()) {
			return coupons;
		} else
			System.out.println("No coupons were found under that price - " + cType);
		return null;
	}

	public Collection<Coupon> getCouponByPrice(double price) throws Exception {
		Collection<Coupon> coupons = couponDBDAO.getCouponByPrice(price);
		if (!coupons.isEmpty()) {
			return coupons;
		} else
			System.out.println("No coupons were found under that price - " + price);
		return null;
	}

	public Company getCompany(long id) throws Exception {
		return companyDBDAO.getCompany(id);
	}

	public void getAllCompanies() throws Exception {
		companyDBDAO.getAllCompanys();
	}

	@Override
	public CompanyFacade login(String name, String password, clientType c) throws Exception {
		CompanyFacade companyFacade = new CompanyFacade();
		if (companyDBDAO.login(name, password)) {
			return companyFacade;
		} else
			return null;
	}

}
