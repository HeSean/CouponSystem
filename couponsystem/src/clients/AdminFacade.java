package clients;

import company.Company;
import company.CompanyDBDAO;
import customer.Customer;
import customer.CustomerDBDAO;
import exception.NameExistsException;;

public class AdminFacade implements CouponClientFacade {

	CompanyDBDAO companyDBDAO = new CompanyDBDAO();
	CustomerDBDAO customerDBDAO = new CustomerDBDAO();

	@Override
	public  CouponClientFacade login(String name, String password, clientType c) throws Exception {
		if (name.equals("admin") && password.equals("1234")) {
			System.out.println("Welcome admin.");
		}
		return null;
	}

	public AdminFacade() {
		// TODO Auto-generated constructor stub
	}

	// Company Methods
	public void createCompany(Company company) throws NameExistsException {
		try {
			if (!companyDBDAO.checkCompanyName(company)) {
				companyDBDAO.createCompany(company);
			} else
				throw new NameExistsException(company.getCompName(), company);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void removeCompany(Company company) throws Exception {
		companyDBDAO.removeCompany(company);
		// missing removal of company's coupons and coupons purchased by customers
	}

	public void updateCompany (Company company) throws Exception{
		companyDBDAO.updateCompany(company);
	}
//	public void updateCompany(long id, String password, String email) throws Exception {
//		Company beforeUpdate = companyDBDAO.getCompany(id);
//		Company afterUpdate = new Company(id, beforeUpdate.getCompName(), password, email);
//		companyDBDAO.updateCompany(afterUpdate);
//	}

	public Company getCompany(long id) throws Exception {
		return companyDBDAO.getCompany(id);
	}

	public void getAllCompanies() throws Exception {
		companyDBDAO.getAllCompanys();
	}

	// Customer Methods
	public void createCustomer(Customer customer) throws NameExistsException {
		try {
			if (!customerDBDAO.checkCustomerName(customer)) {
				customerDBDAO.createCustomer(customer);
			} else
				throw new NameExistsException(customer.getCustName(), customer);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void removeCustomer(Customer customer) throws Exception {
		customerDBDAO.removeCustomer(customer);
		// missing removal of coupon purchase history
	}

	public void updateCustomer(Customer customer) throws Exception {
		customerDBDAO.updateCustomer(customer);

		// updateCustomer(long id, String password)
		// Customer beforeUpdate = customerDBDAO.getCustomer(id);
		// Customer afterUpdate = new Customer(id, beforeUpdate.getCustName(),
		// password);
		// customerDBDAO.updateCustomer(afterUpdate);
	}

	public void getCustomer(long id) throws Exception {
		customerDBDAO.getCustomer(id);
	}

	public void getAllCustomers() throws Exception {
		customerDBDAO.getAllCustomers();
	}

}
