package customer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;


import coupon.Coupon;

public class CustomerDBDAO implements CustomerDAO {

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
		String sql = String.format("UPDATE customers set cust_name = ?, password = ? WHERE id = ?");
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			connection.setAutoCommit(false);
			preparedStatement.setString(1, customer.getCustName());
			preparedStatement.setString(2, customer.getPassword());
			preparedStatement.setLong(3, customer.getId());
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
	public Customer getCustomer(long id) throws SQLException {
		Customer wantedCustomer = new Customer();
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
		String sql = String.format("SELECT cust_Name, password FROM customers WHERE id=?");
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setLong(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
			
			String custName = resultSet.getString("cust_name");
			String password = resultSet.getString("password");
			System.out.printf("id- %d | name- %s | password - %s ", id, custName, password);
			wantedCustomer.setId(id);
			wantedCustomer.setCustName(custName);
			wantedCustomer.setPassword(password);
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
		// TODO Auto-generated method stub
		ArrayList<Customer> customers = new ArrayList<Customer>();
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
		Statement statement = connection.createStatement();

		String sql = "select * from customers";

		ResultSet resultSet = statement.executeQuery(sql);

		while (resultSet.next()) {

			// int id = resultSet.getInt(1);
			long id = resultSet.getLong("id");
			String custName = resultSet.getString("cust_Name");
			customers.add(new Customer(id, custName));
			System.out.printf("id- %d | name- %s\n", id, custName);
		}
		connection.close();
		return customers;
	}

	@Override
	public Collection<Coupon> getAllCoupons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean login(String custName, String password) {
		// TODO Auto-generated method stub
		return false;
	}

}
