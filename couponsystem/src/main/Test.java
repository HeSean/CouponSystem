package main;

import facade.AdminFacade;
import javabeans.Customer;

public class Test {

	@SuppressWarnings("unused")
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
		// Company company1 = new Company("Nestle", "1234", "nestle@gmail.com");
		// company1.getCoupons();
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
		// couponFacade.getAllCoupons();

		AdminFacade adminFacade = new AdminFacade();
		//Coupon coupon2 = new Coupon(5L, "Seventh Free", LocalDate.now(), LocalDate.of(2019, 10, 1), 5, CouponType.RESTAURANTS, "By Nestle", 15);
		// Company companyCheck = new Company("SEAn", "123", "sean@hotmail.com");
		// adminFacade.createCompany(companyCheck);
		// Customer customerCheck = new Customer("Sean", "12345");
		// adminFacade.createCustomer(customerCheck);
		// adminFacade.updateCompany(1, "1234", "sean@gmail.com");
		// adminFacade.updateCustomer(1, "12347785");


		facade.CompanyFacade companyFacade = new facade.CompanyFacade();
		//Company company4 = new Company(4L,"Laser Center", "1234", "lasercenter@gmail.com");
		//company4.addCoupon(coupon2);
		
		//System.out.println(company4.getCoupons());
		//adminFacade.createCompany(company4);
		//company4.getCoupons();
		//companyFacade.createCoupon(coupon2, company4.getId());
		//companyFacade.getAllCoupons();

		

		//companyFacade.createCoupon(coupon2, 2);

		// companyFacade.getCouponByType(CouponType.FOOD);
		// companyFacade.getAllCoupons();
		// companyFacade.getCouponByPrice(10);

		facade.CustomerFacade customerFacade = new facade.CustomerFacade();
		Customer c2 = new Customer("Sean Hed", "1234");
		// customerFacade.purchaseCoupon(c2, coupon);
		// System.out.println(customerFacade.getPurchaseHistory(2));
		// System.out.println(c2.getCoupons());

		//CouponSystem.getInstance().login("admin", "1234", clientType.ADMINISTRATOR);
		//CouponSystem.getInstance().login("Sean Hed", "1234", clientType.CUSTOMER);
		
		

		System.out.println("End");
	}
}
