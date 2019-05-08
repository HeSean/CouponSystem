package main;

import java.time.LocalDate;

import facade.AdminFacade;
import facade.CompanyFacade;
import facade.CustomerFacade;
import facade.clientType;
import javabeans.Company;
import javabeans.Coupon;
import javabeans.CouponType;
import javabeans.Customer;

@SuppressWarnings("unused")
public class ConfigUtils {

	private Coupon coupon1 = new Coupon("Seventh Popcorn Free", LocalDate.now(), LocalDate.of(2019, 10, 1), 5,
			CouponType.FOOD, "By YesPlanet", 15, "");
	private Coupon coupon2 = new Coupon("Free Popcorn with movie", LocalDate.now(), LocalDate.of(2019, 10, 1), 5,
			CouponType.FOOD, "By YesPlanet", 15, "");
	private Coupon coupon3 = new Coupon("Free Tent with Lederman swiss knife", LocalDate.now(),
			LocalDate.of(2019, 10, 1), 5, CouponType.CAMPING, "By Hagor", 15, "");
	private Coupon coupon4 = new Coupon("Bonus ChickenWing with takeout order", LocalDate.now(),
			LocalDate.of(2019, 10, 1), 5, CouponType.FOOD, "By Japanika", 15, "");
	private Coupon coupon5 = new Coupon("Free Flashlight with buy", LocalDate.now(), LocalDate.of(2019, 10, 1), 5,
			CouponType.CAMPING, "By Hagor", 15, "");
	private Coupon coupon6 = new Coupon("30% off on all items", LocalDate.now(), LocalDate.of(2019, 10, 1), 5,
			CouponType.CAMPING, "By Hagor", 30, "");
	private Customer customer1 = new Customer("Sean", "1234");
	private Customer customer2 = new Customer("Michael", "1234");
	private Customer customer3 = new Customer("Tomer", "1234");
	private Customer customer4 = new Customer("Kobi", "1234");
	private Customer customer5 = new Customer("Maya", "1234");
	private Customer customer6 = new Customer("Aurora", "1234");
	private Company company1 = new Company("Yesplanet", "1234", "Yesplanet@gmail.com");
	private Company company2 = new Company("Hagor", "1234", "Hagor@gmail.com");
	private Company company3 = new Company("Japanika", "1234", "Japanika@gmail.com");

	// Tests for running all wanted functions
	public void DropAll() {
		System.out.println("Start.");
		Database database = new Database();
		database.dropCompanyCouponTable();
		database.dropCustomerCouponTable();
		database.dropCompanysTable();
		database.dropCustomersTable();
		database.dropCouponsTable();
		System.out.println("End..");
	}

	public void Initialization() throws Exception {
		System.out.println("Start.");
		Database database = new Database();
		database.createCompanysTable();
		database.createCouponsTable();
		database.createCustomersTable();
		database.createCompanysCouponTable();
		database.createCustomerCouponTable();

		AdminFacade adminFacade = (AdminFacade) CouponSystem.getInstance().login("admin", "1234",
				clientType.ADMINISTRATOR);
		adminFacade.createCompany(company1);
		adminFacade.createCompany(company2);
		adminFacade.createCompany(company3);
		adminFacade.createCustomer(customer1);
		adminFacade.createCustomer(customer2);
		adminFacade.createCustomer(customer3);
		adminFacade.createCustomer(customer4);
		adminFacade.createCustomer(customer5);
		adminFacade.createCustomer(customer6);
		
		CompanyFacade companyFacade = (CompanyFacade) CouponSystem.getInstance().login("YesPlanet", "1234",
				clientType.COMPANY);
		companyFacade.createCoupon(coupon1, company1.getId());
		companyFacade.createCoupon(coupon2, company1.getId());
		companyFacade.createCoupon(coupon3, company2.getId());
		companyFacade.createCoupon(coupon4, company3.getId());
		
		CustomerFacade customerFacade = (CustomerFacade) CouponSystem.getInstance().login("Sean", "1234",
				clientType.CUSTOMER);
		customerFacade.purchaseCoupon(customer1, coupon1);
		
		System.out.println("End.");
	}

	public void Login() throws Exception {
		System.out.println("Start.");
		System.out.println("Admin Login - ");
		AdminFacade adminFacadeFailed = (AdminFacade) CouponSystem.getInstance().login("admin", "12345", clientType.ADMINISTRATOR);
		AdminFacade adminFacade =  (AdminFacade) CouponSystem.getInstance().login("admin", "1234", clientType.ADMINISTRATOR);

		// Credentials Authentication
		System.out.println("Company Login - ");
		CompanyFacade companyFacadeFailed = 	(CompanyFacade) CouponSystem.getInstance().login("Yesplanet", "12345", clientType.COMPANY);
		CompanyFacade companyFacade = (CompanyFacade) CouponSystem.getInstance().login("Yesplanet", "1234", clientType.COMPANY);

		System.out.println("Customer Login - ");
		CustomerFacade customerFacadeFailed = (CustomerFacade) CouponSystem.getInstance().login("Sean", "12345", clientType.CUSTOMER);
		CustomerFacade customerFacade = (CustomerFacade) CouponSystem.getInstance().login("Sean", "1234", clientType.CUSTOMER);

		System.out.println("End.");
	}

	public void AdminActions() throws Exception {
		System.out.println("Start.");

		AdminFacade adminFacade = (AdminFacade) CouponSystem.getInstance().login("admin", "1234",
				clientType.ADMINISTRATOR);

		// Company Methods
		System.out.println("Company Methods ");
		System.out.println("------------------------------------------------------");
		System.out.println("Get company with ID 1 --->");
		System.out.println(adminFacade.getCompany(company1.getId()));
		System.out.println("Get all companies --->");
		System.out.println(adminFacade.getAllCompanies());
		System.out.println("Update company email --->");
		company1.setEmail("Yesplanet@yahoo.com");
		adminFacade.updateCompany(company1);
		System.out.println("Remove company --->");
		//adminFacade.removeCompany(company3);
		System.out.println("------------------------------------------------------");

		// Customer Methods
		System.out.println("Customer Methods");
		System.out.println("------------------------------------------------------");
		System.out.println("Get customer with ID 1 --->");
		System.out.println(adminFacade.getCustomer(customer1.getId()));
		System.out.println("Get all customers --->");
		System.out.println(adminFacade.getAllCustomers());
		System.out.println("Update customer password --->");
		customer1.setPassword("123456");
		adminFacade.updateCustomer(customer1);
		System.out.println("Remove customer --->");
		//adminFacade.removeCustomer(customer3);
		System.out.println("------------------------------------------------------");
		System.out.println("End.");
	}

	public void CompanyActions() throws Exception {
		System.out.println("Start.");
		CompanyFacade companyFacade = (CompanyFacade) CouponSystem.getInstance().login("Hagor", "1234",
				clientType.COMPANY);

		System.out.println("Creating coupon --->");
		companyFacade.createCoupon(coupon5, company2.getId());
		companyFacade.createCoupon(coupon6, company2.getId());
		System.out.println("Removing coupon --->");
		companyFacade.removeCoupon(coupon5);
		System.out.println("Updating coupon --->");
		coupon6.setAmount(10);
		companyFacade.updateCoupon(coupon6);
		System.out.println("Getting coupon --->");
		System.out.println(companyFacade.getCoupon(coupon6.getId()));

		System.out.println("Getting all coupons of logged company ---> ");
		companyFacade.login("Hagor", "1234", clientType.COMPANY);
		System.out.println(companyFacade.getAllCoupons());
		System.out.println("Getting coupons by TYPE --->");
		System.out.println(companyFacade.getCouponByType(CouponType.FOOD));
		System.out.println(companyFacade.getCouponByType(CouponType.CAMPING));
		System.out.println("Getting coupons by PRICE --->");
		System.out.println(companyFacade.getCouponByPrice(100));
		System.out.println("Getting coupons by DATE --->");
		System.out.println(companyFacade.getCouponByDate(LocalDate.now()));
		System.out.println("End.");
	}

	public void CustomerActions() throws Exception {
		System.out.println("Start.");
		CustomerFacade customerFacade = (CustomerFacade) CouponSystem.getInstance().login("Michael", "1234",
				clientType.CUSTOMER);
		System.out.println("Purchasing coupon --->");
		customerFacade.purchaseCoupon(customer2, coupon2);

		System.out.println("Getting customer's purchase history --->");
		System.out.println(customerFacade.getCouponsPurchaseHistory());
		System.out.println("Getting customer's purchase history BY TYPE --->");
		System.out.println(customerFacade.getAllPurchasedCouponsByType(CouponType.FOOD));
		System.out.println("Getting customer's purchase history BY PRICE --->");
		System.out.println(customerFacade.getAllPurchasedCouponsByPrice(100));
		System.out.println("End.");

	}

}
