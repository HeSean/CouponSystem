package main;

import java.util.Date;

import clients.AdminFacade;
import company.Company;
import company.CompanyFacade;
import coupon.Coupon;
import coupon.CouponFacade;
import coupon.CouponType;
import customer.Customer;
import customer.CustomerFacade;

@SuppressWarnings("unused")
public class Test {

	@SuppressWarnings({ "unused", "deprecation" })
	public static void main(String[] args) throws Exception, IllegalAccessException, ClassNotFoundException {
		// TODO Auto-generated method stub
		System.out.println("Start");

		// CustomerFacade Methods

		// CustomerFacade customerFacade = new CustomerFacade();
		// Customer c3 = new Customer("Michaelz", "123");
		// Customer c4 = new Customer("Tomerz", "12345");
		// System.out.println(c2 + ", " + c3+ ", " + c4 );
		// c2.setCustName("Sean Hed");
		// c2.setPassword("4443");
		// customerFacade.getAllCustomers();
		// customerFacade.getCustomer(1);
		// customerFacade.updateCustomer(c2);
		// customerFacade.updateCustomer(customer);

		// CompanyFacade Methods

		// CompanyFacade companyFacade = new CompanyFacade();
		// Company company1 = new Company("zzimz", "1234", "zzimz@gmail.com");
		// Company company2 = new Company("zzimz hapoalim", "444",
		// "hapoalimz@gmail.com");
		// Company company3 = new Company("alskdjalksdj", "333", "sdlfkj@gmail.com");
		// System.out.println(company1 + "\n" + company2 + "\n" + company3);
		// company1.setCompName("HED ENTERPRISEz");
		// companyFacade.updateCompany(company1);
		// companyFacade.removeCompany(company3);
		// companyFacade.getCompany(1);
		// companyFacade.getAllCompanies();

		// CouponFacade Methods

		// CouponFacade couponFacade = new CouponFacade();
		// Coupon coupon = new Coupon("food", new java.sql.Date(1220227200L * 1000), new
		// java.sql.Date(1220227200L * 1000),
		// 1, CouponType.FOOD, "this coupoun is for food", 100);
		// Coupon coupon2 = new Coupon("sports", new java.sql.Date(1220227200L * 1000),
		// new java.sql.Date(1220227200L * 1000), 1, CouponType.SPORTS, "this coupon is
		// for sporting goodsz", 150);
		// Coupon coupon3 = new Coupon("health", new java.sql.Date(1220227200L * 1000),
		// new java.sql.Date(1220227200L * 1000), 1, CouponType.HEALTH, "this coupon is
		// for your health madafaka",
		// 200);
		// Coupon coupon4 = new Coupon("health", new java.sql.Date(1220227200L * 1000),
		// new java.sql.Date(1220227200L * 1000), 1, CouponType.HEALTH, "this coupon is
		// for your health madafaka",
		// 200);
		// System.out.println(coupon + "\n" + coupon2 + "\n" + coupon3);
		// couponFacade.createCoupon(coupon);
		// couponFacade.createCoupon(coupon2);
		// couponFacade.createCoupon(coupon3);
		// couponFacade.removeCoupon(coupon3);
		// coupon.setAmount(6);
		// couponFacade.updateCoupon(coupon);
		// couponFacade.createCoupon(coupon4);
		// couponFacade.createCoupon(coupon3);
		// couponFacade.getCoupon(1);
		//couponFacade.getAllCoupons();

		AdminFacade adminFacade = new AdminFacade();
		//Company companyCheck = new Company("SEAn", "123", "sean@hotmail.com");
		//adminFacade.createCompany(companyCheck);
		//Customer customerCheck = new Customer("Sean", "12345");
		//adminFacade.createCustomer(customerCheck);
		// adminFacade.updateCompany(1, "1234", "sean@gmail.com");
		// adminFacade.updateCustomer(1, "12347785");
		
		
		clients.CompanyFacade companyFacade = new clients.CompanyFacade();
		Coupon coupon = new Coupon	(4L,"Free drink with meal", new java.sql.Date(119, 3, 31), new java.sql.Date(119, 10, 1), 4, CouponType.FOOD, "free drink", 10);
		//companyFacade.createCoupon(coupon);
		//companyFacade.getCouponByType(CouponType.FOOD);
		//companyFacade.getAllCoupons();
		//companyFacade.getCouponByPrice(10);
		
		
		clients.CustomerFacade customerFacade = new clients.CustomerFacade();
		Customer c2 = new Customer("Sean Hed", "1234");
		customerFacade.purchaseCoupon(c2, coupon);
		

		System.out.println("End");
	}
}
