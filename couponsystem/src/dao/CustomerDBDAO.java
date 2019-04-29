package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;

import exception.EmptyException;
import exception.FailedConnectionException;
import exception.IncorrectCredentialsException;
import javabeans.Coupon;
import javabeans.Customer;
import main.ConnectionPoolBlockingQueue;

public class CustomerDBDAO implements CustomerDAO {

	//private ConnectionPool pool;
	private ConnectionPoolBlockingQueue pool;

	public CustomerDBDAO() {
		try {
			pool = ConnectionPoolBlockingQueue.getInstance();
			//pool = ConnectionPool.getInstance();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createCustomer(Customer customer) throws FailedConnectionException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		// String sql = String.format("INSERT INTO customers (id, cust_Name, password)
		// VALUES (?,?,?)");
		try (PreparedStatement preparedStatement = connection.prepareStatement(
				"INSERT INTO customers (id, cust_Name, password) VALUES (?,?,?)",
				PreparedStatement.RETURN_GENERATED_KEYS);) {
			preparedStatement.setLong(1, customer.getId());
			preparedStatement.setString(2, customer.getCustName());
			preparedStatement.setString(3, customer.getPassword());
			preparedStatement.executeUpdate();

			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			resultSet.next();
			System.out.println("New customer creation succeeded." + customer.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
	}

	public boolean checkCustomerName(Customer customer) throws FailedConnectionException { // checking if a customer
																							// already exists with name
		boolean exists = false;
		LinkedHashSet<String> names = new LinkedHashSet<>();
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = "SELECT cust_name FROM customers";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			ResultSet resultSet = preparedStatement.executeQuery(sql);

			while (resultSet.next()) {
				String customerName = resultSet.getString("cust_name");
				names.add(customerName);
			}
			for (String name : names) {
				if (name.equals(customer.getCustName())) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
		return exists;
	}

	@Override
	public void removeCustomer(Customer customer) throws FailedConnectionException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = String.format("delete from customers where id = ?");
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			connection.setAutoCommit(false);
			preparedStatement.setLong(1, customer.getId());
			preparedStatement.executeUpdate();
			System.out
					.println("Delete succesful of customer - " + customer.getCustName() + ", id - " + customer.getId());
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
	}

	@Override
	public void updateCustomer(Customer customer) throws FailedConnectionException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = String.format("UPDATE customers set password = ? WHERE id = ?");
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			connection.setAutoCommit(false);
			preparedStatement.setString(1, customer.getPassword());
			preparedStatement.setLong(2, customer.getId());
			preparedStatement.executeUpdate();
			System.out.println("\nUpdate succesful.\nNew Data - " + customer);
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
	}

	@Override
	public Customer getCustomer(long id) throws FailedConnectionException {
		Customer wantedCustomer = new Customer();
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = String.format("SELECT cust_Name, password FROM customers WHERE id=?");
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setLong(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String custName = resultSet.getString("cust_name");
				String password = resultSet.getString("password");
				wantedCustomer.setId(id);
				wantedCustomer.setCustName(custName);
				wantedCustomer.setPassword(password);
				wantedCustomer.setCoupons((LinkedHashSet<Coupon>) getCoupons((LinkedHashSet<Long>) getCouponsID(id)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
		return wantedCustomer;
	}

	public Customer getCustomer(String name) {
		Customer wantedCustomer = new Customer();
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = String.format("SELECT * FROM customers WHERE cust_name=?");
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, name);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				long id = resultSet.getLong("id");
				String custName = resultSet.getString("cust_name");
				String password = resultSet.getString("password");
				wantedCustomer.setId(id);
				wantedCustomer.setCustName(custName);
				wantedCustomer.setPassword(password);
				wantedCustomer.setCoupons(getCoupons(getCouponsID(id)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wantedCustomer;
	}

	@Override
	public Collection<Customer> getAllCustomers() throws FailedConnectionException {
		LinkedHashSet<Customer> customers = new LinkedHashSet<Customer>();
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = "select * from customers";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			ResultSet resultSet = preparedStatement.executeQuery(sql);

			while (resultSet.next()) {
				long id = resultSet.getLong("id");
				String custName = resultSet.getString("cust_Name");
				customers.add(new Customer(id, custName));
				System.out.printf("id- %d | name- %s\n", id, custName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
		return customers;
	}

	// get all coupon ids bought by customer
	public Collection<Long> getCouponsID(long wantedID) throws FailedConnectionException {
		LinkedHashSet<Long> couponsID = new LinkedHashSet<Long>();
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = "SELECT coupon_ID from customers_coupon WHERE customer_ID = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setLong(1, wantedID);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				couponsID.add(resultSet.getLong("coupon_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
		return couponsID;
	}

	// get all coupons the customer purchased
	public Collection<Coupon> getCoupons(Collection<Long> ids) throws FailedConnectionException {
		LinkedHashSet<Coupon> coupons = new LinkedHashSet<Coupon>();
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		ResultSet resultSet;
		String sql = "select * from coupons WHERE id = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			for (Long iLong : ids) {
				preparedStatement.setLong(1, iLong);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					long id = resultSet.getLong("id");
					String title = resultSet.getString("title");
					LocalDate startDate = resultSet.getDate("start_Date").toLocalDate();
					LocalDate endDate = resultSet.getDate("end_Date").toLocalDate();
					int amount = resultSet.getInt("amount");
					String type = resultSet.getString(6);
					String message = resultSet.getString("message");
					Double price = resultSet.getDouble("price");
					// String image = resultSet.getString("image");
					coupons.add(new Coupon(id, title, startDate, endDate, amount, type, message, price));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			pool.returnConnection(connection);
		}
		return coupons;

	}

	@Override
	public Collection<Coupon> getAllCoupons(long wantedID) throws FailedConnectionException {
		LinkedHashSet<Long> couponsID = new LinkedHashSet<Long>();
		LinkedHashSet<Coupon> coupons = new LinkedHashSet<Coupon>();
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = "select * from customer_coupon WHERE customer_id = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setLong(1, wantedID);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				couponsID.add(resultSet.getLong("coupon_id"));
			}

			while (resultSet.next()) {
				long id = resultSet.getLong("id");
				String title = resultSet.getString("title");
				LocalDate startDate = resultSet.getDate("start_Date").toLocalDate();
				LocalDate endDate = resultSet.getDate("end_Date").toLocalDate();
				int amount = resultSet.getInt("amount");
				String type = resultSet.getString(6);
				String message = resultSet.getString("message");
				Double price = resultSet.getDouble("price");
				// String image = resultSet.getString("image");

				coupons.add(new Coupon(id, title, startDate, endDate, amount, type, message, price));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			pool.returnConnection(connection);
		}
		return coupons;

	}

	@Override
	public boolean login(String custName, String givenPassword) throws FailedConnectionException {
		boolean correctInitials = false;
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = String.format("SELECT id, cust_name, password FROM customers WHERE cust_name = ?");
		String password = null, name = null;
		// long id;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, custName);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				password = resultSet.getString("password");
				name = resultSet.getString("cust_name");
				// id = resultSet.getLong("id");
			}
			if (name == null) {
				throw new IncorrectCredentialsException("Customer with that name doesnt exist.");
			}
			if (givenPassword.equals(password)) {
				correctInitials = true;
			} else {
				throw new IncorrectCredentialsException("Customer with that password doesnt exist.");
			}
		} catch (IncorrectCredentialsException e) {
			e.printStackTrace();
		} catch (SQLSyntaxErrorException e) {
			EmptyException ee = new EmptyException("Customers table does not exist .");
			ee.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			pool.returnConnection(connection);
		}
		return correctInitials;
	}
}
