package customer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import coupon.Coupon;
import coupon.CouponDBDAO;
import coupon.CouponType;
import exception.WrongInfoInsertedException;

public class CustomerDBDAO implements CustomerDAO {

	private Customer customer;
	
	public CustomerDBDAO() {
		// TODO Auto-generated constructor stub

	}

	@Override
	public void createCustomer(Customer customer) throws SQLException {
		// TODO Auto-generated method stub
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
		String sql = String.format("INSERT INTO customers (id, cust_Name, password) VALUES (?,?,?)");
		// String sql = String.format("insert into customer(id, cust_Name, password)
		// values(%d, '%s','%s')",
		// customer.getId(), customer.getCustName(), customer.getPassword());
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql,
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
			connection.close();
		}
	}

	public boolean checkCustomerName(Customer customer) throws Exception { // checking to see if a customer already
																			// exists with name
		boolean exists = false;
		ArrayList<String> names = new ArrayList<>();
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
		Statement statement = connection.createStatement();
		String sql = "SELECT cust_name FROM customers";
		ResultSet resultSet = statement.executeQuery(sql);

		while (resultSet.next()) {
			String customerName = resultSet.getString("cust_name");
			names.add(customerName);
		}
		for (String name : names) {
			if (name.equals(customer.getCustName())) {
				return true;
			}
		}
		connection.close();
		return exists;
	}

	@Override
	public void removeCustomer(Customer customer) throws SQLException {
		// TODO Auto-generated method stub
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());

		String sql = String.format("delete from customers where id=?");
		// String sql = String.format("INSERT INTO customer (id, cust_Name, password)
		// VALUES (?,?,?)");
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			connection.setAutoCommit(false);
			preparedStatement.setLong(1, customer.getId());
			preparedStatement.executeUpdate();
			System.out.println("Delete succesful");
			connection.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}

	}

	@Override
	public void updateCustomer(Customer customer) throws SQLException {
		// TODO Auto-generated method stub
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
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
			connection.close();
		}
	}

	@Override
	public Customer getCustomer(long id) throws Exception {
		Customer wantedCustomer = new Customer();
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
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
				wantedCustomer.setCoupons((ArrayList<Coupon>) getCoupons((ArrayList<Long>) getCouponsID(id)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return wantedCustomer;
	}
	
	public Customer getCustomer(String name) throws Exception {
		Customer wantedCustomer = new Customer();
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
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
		} finally {
			connection.close();
		}
		return wantedCustomer;
	}

	@Override
	public Collection<Customer> getAllCustomers() throws SQLException {
		ArrayList<Customer> customers = new ArrayList<Customer>();
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
		Statement statement = connection.createStatement();

		String sql = "select * from customers";

		ResultSet resultSet = statement.executeQuery(sql);

		while (resultSet.next()) {
			long id = resultSet.getLong("id");
			String custName = resultSet.getString("cust_Name");
			customers.add(new Customer(id, custName));
			System.out.printf("id- %d | name- %s\n", id, custName);
		}
		connection.close();
		return customers;
	}

	// get all coupon ids bought by customer
	public ArrayList<Long> getCouponsID(long wantedID) throws Exception {
		ArrayList<Long> couponsID = new ArrayList<Long>();
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
		String sql = "SELECT coupon_ID from customers_coupon WHERE customer_ID = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setLong(1, wantedID);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				couponsID.add(resultSet.getLong("coupon_id"));
			}
			connection.close();
			return couponsID;
		}
	}
	
	// get all coupons the customer purchased
	public ArrayList<Coupon> getCoupons(ArrayList<Long> ids) throws Exception {
		ArrayList<Coupon> coupons = new ArrayList<Coupon>();
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
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
					String image = resultSet.getString("image");
				

					coupons.add(new Coupon(id, title, startDate, endDate, amount, type, message, price));
				}
			}
			connection.close();
			return coupons;
		}
	}
	@Override
	public Collection<Coupon> getAllCoupons(long wantedID) throws Exception {
		ArrayList<Long> couponsID = new ArrayList<Long>();
		ArrayList<Coupon> coupons = new ArrayList<Coupon>();
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());

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
				String image = resultSet.getString("image");
	

				coupons.add(new Coupon(id, title, startDate, endDate, amount, type, message, price));
			}
			connection.close();
			return coupons;
		}
	}



	@Override
	public boolean login(String custName, String givenPassword) throws SQLException {
		boolean correctInitials = false;
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
		String sql = String.format("SELECT id, cust_name, password FROM customers WHERE cust_name = ?");
		String password = null, name = null;
		long id;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, custName);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				password = resultSet.getString("password");
				name = resultSet.getString("cust_name");
				id = resultSet.getLong("id");
			}
			if (name == null) {
				throw new WrongInfoInsertedException("Company with that name doesnt exist.");
			}
			if (givenPassword.equals(password)) {
				correctInitials = true;
			}
		} catch (WrongInfoInsertedException e) {
			e.printStackTrace();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return correctInitials;
	}
}
