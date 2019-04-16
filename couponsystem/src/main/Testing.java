package main;

import java.time.LocalDate;

import clients.AdminFacade;
import clients.CompanyFacade;
import clients.CustomerFacade;
import clients.clientType;
import company.Company;
import coupon.Coupon;
import coupon.CouponType;
import customer.Customer;

public class Testing {

	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		System.out.println("Start.");
		Database database = new Database();
		// database.dropCompanyCouponTable();
		// database.dropCustomerCouponTable();
		// database.dropCompanysTable();
		// database.dropCustomersTable();
		// database.dropCouponsTable();
		// database.createCompanysTable();
		// database.createCouponsTable();
		// database.createCustomersTable();
		// database.createCompanysCouponTable();
		// database.createCustomerCouponTable();

		Coupon coupon1 = new Coupon("Seventh Popcorn Free", LocalDate.now(), LocalDate.of(2019, 10, 1), 5,
				CouponType.FOOD, "By YesPlanet", 15);
		Coupon coupon2 = new Coupon("Free Popcorn with movie", LocalDate.now(), LocalDate.of(2019, 10, 1), 5,
				CouponType.FOOD, "By YesPlanet", 15);
		Coupon coupon3 = new Coupon("Free Tent with BBQ", LocalDate.now(), LocalDate.of(2019, 10, 1), 5,
				CouponType.CAMPING, "By Hagor", 15);
		Coupon coupon4 = new Coupon("Bonus ChickenWing with takeout order", LocalDate.now(), LocalDate.of(2019, 10, 1),
				5, CouponType.FOOD, "By Japanika", 15);

		Customer customer1 = new Customer("Sean", "1234");
		Customer customer2 = new Customer("Michael", "1234");
		Customer customer3 = new Customer("Tomer", "1234");
		Customer customer4 = new Customer("Kobi", "1234");
		Customer customer5 = new Customer("Maya", "1234");
		Customer customer6 = new Customer("Aurora", "1234");

		Company company1 = new Company("Yesplanet", "1234", "Yesplanet@gmail.com");
		Company company2 = new Company("Hagor", "1234", "Hagor@gmail.com");
		Company company3 = new Company("Japanika", "1234", "Japanika@gmail.com");

		AdminFacade adminFacade = new AdminFacade();
		CompanyFacade companyFacade = new CompanyFacade();
		CustomerFacade customerFacade = new CustomerFacade();

		// adminFacade.createCompany(company1);
		// adminFacade.createCompany(company2);
		// adminFacade.createCompany(company3);
		// adminFacade.createCustomer(customer1);
		// adminFacade.createCustomer(customer2);
		// adminFacade.createCustomer(customer3);
		// adminFacade.createCustomer(customer4);
		// adminFacade.createCustomer(customer5);
		// adminFacade.createCustomer(customer6);
		// // //
//		 companyFacade.createCoupon(coupon1, company1.getId());
//		 companyFacade.createCoupon(coupon2, company1.getId());
//		 companyFacade.createCoupon(coupon3, company2.getId());
//		 companyFacade.createCoupon(coupon4, company3.getId());
		// companyFacade.getAllCompanies();
		// companyFacade.getAllCoupon();
		// companyFacade.getCouponByPrice(10);
		// companyFacade.getCouponByType(CouponType.FOOD);
		// companyFacade.getCoupon(2);
		// companyFacade.getAllCoupon();
		// companyFacade.getCouponByDate(LocalDate.now());

		// companyFacade.removeCoupon(coupon1);

		// customerFacade.getAllCustomers();
		// System.out.println(customerFacade.getCustomer(1));
		// System.out.println(customerFacade.getPurchaseHistory(1));

		// System.out.println(customerFacade.getAllPurchasedCouponsByPrice(10));
		// System.out.println(customerFacade.getAllPurchasedCouponsByType(CouponType.FOOD));
		// System.out.println(customerFacade.getAllPurchasedCouponsByType(CouponType.ELECTRICITY));

		// System.out.println(customerFacade.getAllPurchasedCouponsByPrice(100));
		//
		// adminFacade.login("admin", "1234", clientType.ADMINISTRATOR);
		// adminFacade.getAllCompanies();
		// adminFacade.getAllCustomers();
		// companyFacade.removeCoupon(coupon1);

		// companyFacade.login("Japanika", "1234", clientType.COMPANY);
		// facades are able to override logins but not purchase coupon method - stuck
		// with same id when trying to buy coupon

//		companyFacade.login("Yesplanet", "1234", clientType.COMPANY);
//		System.out.println(companyFacade.getAllCoupon());

//		customerFacade.login("Sean", "1234", clientType.CUSTOMER);
//		customerFacade.purchaseCoupon(customer1, coupon1);
//		customerFacade.purchaseCoupon(customer1, coupon1);
//		System.out.println(customerFacade.getCouponsPurchaseHistory());
//
//		customerFacade.login("Michael", "1234", clientType.CUSTOMER);
//		customerFacade.purchaseCoupon(customer2, coupon1);
		
//		customerFacade.login("Tomer", "1234", clientType.CUSTOMER);
//		customerFacade.purchaseCoupon(customer3, coupon1);
//
		// customerFacade.login("Kobi", "1234", clientType.CUSTOMER);
		// //customerFacade.purchaseCoupon(customer4, coupon1);
		// System.out.println(customerFacade.getCouponsPurchaseHistory());
		//
		// customerFacade.login("Maya", "1234", clientType.CUSTOMER);
		// //customerFacade.purchaseCoupon(customer5, coupon1);
		// System.out.println(customerFacade.getCouponsPurchaseHistory());
		//
		// customerFacade.login("Aurora", "1234", clientType.CUSTOMER);
		// //customerFacade.purchaseCoupon(customer6, coupon1);
		// System.out.println(customerFacade.getCouponsPurchaseHistory());

		// System.out.println(customerFacade.getCouponsPurchaseHistory());
		//
		// customerFacade.purchaseCoupon(customer2, coupon1);
		// System.out.println(customerFacade.getCouponsPurchaseHistory());
		
		//CouponSystem.getInstance().startCouponsUpdater();

		System.out.println("End.");
	}

}
