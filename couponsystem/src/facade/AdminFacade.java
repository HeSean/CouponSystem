package facade;

import java.util.Collection;

import dao.CompanyDBDAO;
import dao.CouponDBDAO;
import dao.CustomerDBDAO;
import exception.FailedConnectionException;
import exception.NameExistsException;
import javabeans.Company;
import javabeans.Coupon;
import javabeans.Customer;;

public class AdminFacade implements CouponClientFacade {

	private static final String ADMINUSER = "admin";
	private static final String ADMINPASSWORD = "1234";
	private CompanyDBDAO companyDBDAO= new CompanyDBDAO();
	private CustomerDBDAO customerDBDAO  = new CustomerDBDAO();
	private CouponDBDAO couponDBDAO = new CouponDBDAO();

	public AdminFacade() {

	}

	@Override
	public CouponClientFacade login(String name, String password, clientType c) {
		if (ADMINUSER.equals(name) && ADMINPASSWORD.equals(password)) {
			System.out.println("Welcome admin.");
			return this;
		} else
			return null;
	}

	// Company Methods
	public void createCompany(Company company) {
		try {
			if (!companyDBDAO.checkCompanyName(company)) {
				companyDBDAO.createCompany(company);
			} else
				throw new NameExistsException(company.getCompName(), company);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void removeCompany(Company company) throws FailedConnectionException {
		companyDBDAO.removeCompany(company);
	}

	public void updateCompany(Company company) throws FailedConnectionException {
		companyDBDAO.updateCompany(company);
	}

	public Company getCompany(long id) throws FailedConnectionException {
		return companyDBDAO.getCompany(id);
	}

	public void getAllCompanies() throws FailedConnectionException {
		companyDBDAO.getAllCompanys();
	}

	// Customer Methods
	public void createCustomer(Customer customer) {
		try {
			if (!customerDBDAO.checkCustomerName(customer)) {
				customerDBDAO.createCustomer(customer);
			} else
				throw new NameExistsException(customer.getCustName(), customer);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void removeCustomer(Customer customer) throws FailedConnectionException {
		customerDBDAO.removeCustomer(customer);
	}

	public void updateCustomer(Customer customer) throws FailedConnectionException {
		customerDBDAO.updateCustomer(customer);
	}

	public Customer getCustomer(long id) throws FailedConnectionException {
		return customerDBDAO.getCustomer(id);
	}

	public Collection<Customer> getAllCustomers() throws FailedConnectionException {
		return customerDBDAO.getAllCustomers();
	}

	public Collection<Coupon> getAllCoupons()   {
		try {
			return couponDBDAO.getAllCoupons();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		} return null;
	}

}
