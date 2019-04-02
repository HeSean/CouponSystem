package customer;

public class CustomerFacade {

	CustomerDBDAO customerDBDAO = new CustomerDBDAO();

	public void createCustomer(Customer customer) throws Exception {
		customerDBDAO.createCustomer(customer);
	}

	public void removeCustomer(Customer customer) throws Exception {
		customerDBDAO.removeCustomer(customer);
	}

	public void updateCustomer(Customer customer) throws Exception {
		customerDBDAO.updateCustomer(customer);
	}

	public void getCustomer(long id) throws Exception {
		customerDBDAO.getCustomer(id);
	}

	public void getAllCustomers() throws Exception {
		customerDBDAO.getAllCustomers();
	}

}
