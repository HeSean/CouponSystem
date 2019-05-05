package facade;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;

import dao.CompanyDBDAO;
import dao.CouponDBDAO;
import exception.EmptyException;
import exception.FailedConnectionException;
import exception.NameExistsException;
import javabeans.Company;
import javabeans.Coupon;
import javabeans.CouponType;

public class CompanyFacade implements CouponClientFacade {

	private Company company;
	private CouponDBDAO couponDBDAO = new CouponDBDAO();
	private CompanyDBDAO companyDBDAO = new CompanyDBDAO();

	public CompanyFacade() {
	}

	// Method for creating a new coupon
	public void createCoupon(Coupon coupon, long compID) {
		try {
			if (!couponDBDAO.checkCouponName(coupon)) {
				couponDBDAO.createCoupon(coupon);
				Company company = companyDBDAO.getCompany(compID);
				couponDBDAO.insertCouponToCompanysCouponJoinTable(coupon.getId(), compID);
				company.addCoupon(coupon);
			} else
				throw new NameExistsException(coupon.getTitle(), coupon);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Method for removing coupon
	public void removeCoupon(Coupon coupon) {
		try {
			couponDBDAO.removeCoupon(coupon);
		} catch (EmptyException e) {
			e.printStackTrace();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
	}

	// Method for updating coupon details - end date or price
	public void updateCoupon(Coupon coupon) throws Exception {
		couponDBDAO.updateCoupon(coupon);
	}

	public Coupon getCoupon(long id) throws Exception {
		return couponDBDAO.getCoupon(id);
	}

	public Collection<Coupon> getAllCoupons() throws Exception {
		LinkedHashSet<Long> couponsID = companyDBDAO.getCouponsID(company.getId());
		return companyDBDAO.getCoupons(couponsID);
	}

	public Collection<Coupon> getCouponByType(CouponType cType) throws Exception {
		System.out.println("Coupons from type " + cType + " are:");
		LinkedHashSet<Long> couponsID = companyDBDAO.getCouponsID(company.getId());
		LinkedHashSet<Coupon> coupons = (LinkedHashSet<Coupon>) companyDBDAO.getCoupons(couponsID);
		LinkedHashSet<Coupon> wantedCoupons = new LinkedHashSet<>();
		for (Coupon coupon : coupons) {
			if (coupon.getType().equals(cType)) {
				wantedCoupons.add(coupon);
			}
		}
		if (!wantedCoupons.isEmpty()) {
			return wantedCoupons;
		} else
			System.out.println("No coupons were found from type - " + cType);
		return null;
	}

	public Collection<Coupon> getCouponByPrice(double price) throws Exception {
		System.out.println("Coupons under " + price + "$ price are:");
		LinkedHashSet<Long> couponsID = companyDBDAO.getCouponsID(company.getId());
		LinkedHashSet<Coupon> coupons = (LinkedHashSet<Coupon>) companyDBDAO.getCoupons(couponsID);
		LinkedHashSet<Coupon> wantedCoupons = new LinkedHashSet<>();
		for (Coupon coupon : coupons) {
			if (coupon.getPrice() < price) {
				wantedCoupons.add(coupon);
			}
		}
		if (!wantedCoupons.isEmpty()) {
			return wantedCoupons;
		} else
			System.out.println("No coupons were found under that price - " + price);
		return null;
	}

	public Collection<Coupon> getCouponByDate(LocalDate localDate) throws Exception {
		System.out.println("Available coupons by given date - " + localDate + " are:");
		LinkedHashSet<Long> couponsID = companyDBDAO.getCouponsID(company.getId());
		LinkedHashSet<Coupon> coupons = (LinkedHashSet<Coupon>) companyDBDAO.getCoupons(couponsID);
		LinkedHashSet<Coupon> wantedCoupons = new LinkedHashSet<>();
		for (Coupon coupon : coupons) {
			if (coupon.getEndDate().compareTo(localDate) > 0) {
				wantedCoupons.add(coupon);
			}
		}
		if (!coupons.isEmpty()) {
			return coupons;
		} else
			System.out.println("No available coupons were found in the system to expire before  - " + localDate);
		return null;
	}

	public Company getCompany(long id) throws Exception {
		return companyDBDAO.getCompany(id);
	}

	public void getAllCompanies() throws Exception {
		companyDBDAO.getAllCompanys();
	}

	@Override
	public CouponClientFacade login(String name, String password, clientType c) throws Exception {
		if (companyDBDAO.login(name, password)) {
			System.out.println("LOGIN of " + name);
			this.company = companyDBDAO.getCompany(name);
			return this;
		} else
			return null;
	}

}
