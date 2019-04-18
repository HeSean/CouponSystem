package facade;

import java.util.Collection;

import db.CompanyDBDAO;
import db.CustomerDBDAO;
import exception.NameExistsException;
import javabeans.Company;
import javabeans.Coupon;
import javabeans.Customer;;

public class AdminFacade implements CouponClientFacade {

	private static final String ADMINUSER = "admin";
	private static final String ADMINPASSWORD = "1234";
	private CompanyDBDAO companyDBDAO;
	private CustomerDBDAO customerDBDAO;

	public AdminFacade() throws Exception {
		companyDBDAO = new CompanyDBDAO();
		customerDBDAO = new CustomerDBDAO();
	}
	
	@Override
	public  CouponClientFacade login(String name, String password, clientType c) throws Exception {
		if (ADMINUSER.equals(name) && ADMINPASSWORD.equals(password)) {
			System.out.println("Welcome admin.");
			return this;
		} else 
			return null;
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
	}

	public void updateCompany (Company company) throws Exception{
		companyDBDAO.updateCompany(company);
	}


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
	}

	public void updateCustomer(Customer customer) throws Exception {
		customerDBDAO.updateCustomer(customer);
	}

	public Customer getCustomer(long id) throws Exception {
		return customerDBDAO.getCustomer(id);
	}

	public Collection<Customer> getAllCustomers() throws Exception {
		return customerDBDAO.getAllCustomers();
	}
	
	public Collection<Coupon> getAllCoupons() throws Exception{
		return null;
	}

}
