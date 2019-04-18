package db;

import java.util.Collection;

import javabeans.Coupon;
import javabeans.Customer;

public interface CustomerDAO {

	public void createCustomer(Customer customer) throws Exception;

	public void removeCustomer(Customer customer) throws Exception;

	public void updateCustomer(Customer customer) throws Exception;

	public Customer getCustomer(long id) throws Exception;

	public Collection<Customer> getAllCustomers() throws Exception;

	public Collection<Coupon> getAllCoupons(long id) throws Exception;

	public boolean login(String custName, String password) throws Exception;

	

}
