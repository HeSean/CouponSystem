package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import exception.EmptyException;
import exception.NameExistsException;

public class Database {

	private static String connectionString = "jdbc:mysql://localhost:3306/CouponSystem?user=user&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT";

	public static String getDBURL() {
		return connectionString;
	}

	// Create table method
	public void createCouponsTable() throws Exception {
		Connection connection = DriverManager.getConnection(getDBURL());
		String sql = "CREATE TABLE if not exists Coupons (id int(64) NOT NULL AUTO_INCREMENT PRIMARY KEY , title varchar(45), start_Date Date, end_Date Date,  amount int (64), type ENUM(\"RESTAURANTS\",\r\n"
				+ "		\"ELECTRICITY\",\r\n" + "		\"FOOD\",\r\n" + "		\"HEALTH\",\r\n"
				+ "		\"SPORTS\",\r\n" + "		\"CAMPING\",\r\n"
				+ "		\"TRAVELLING\"), message varchar(45), price double, image varchar(45));";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			if (preparedStatement.executeUpdate() == 0) {
				System.out.println("Coupon table succesfully created.");
			} else
				throw new NameExistsException("Coupon Table");
		} catch (NameExistsException e) {
			e.printStackTrace();
		}
	}

	public void createCustomersTable() throws Exception {
		Connection connection = DriverManager.getConnection(getDBURL());
		String sql = "CREATE TABLE if not exists Customers (id int(64) NOT NULL AUTO_INCREMENT PRIMARY KEY , cust_name varchar(45), password varchar(45));";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			if (preparedStatement.executeUpdate() == 0) {
				System.out.println("Customer table succesfully created.");
			} else
				throw new NameExistsException("Customer Table");
		} catch (NameExistsException e) {
			e.printStackTrace();
		}
	}

	public void createCompanysTable() throws Exception {
		Connection connection = DriverManager.getConnection(getDBURL());
		String sql = "CREATE TABLE if not exists companys (id int(64) NOT NULL AUTO_INCREMENT PRIMARY KEY , comp_name varchar(45), password varchar(45), email varchar(45));";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			if (preparedStatement.executeUpdate() == 0) {
				System.out.println("Companys table succesfully created.");
			} else
				throw new NameExistsException("Companys Table");
		} catch (NameExistsException e) {
			e.printStackTrace();
		}
	}

	public void createCustomerCouponTable() throws Exception {
		Connection connection = DriverManager.getConnection(getDBURL());
		String sql = "CREATE TABLE if not exists customers_coupon (\r\n" + "customer_id int NOT NULL ,\r\n"
				+ "coupon_id int NOT NULL PRIMARY KEY ,    \r\n"
				+ "FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE cascade,\r\n"
				+ "FOREIGN KEY (coupon_id) REFERENCES coupons(id) ON DELETE cascade\r\n" + ");";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			if (preparedStatement.executeUpdate() == 0) {
				System.out.println("Customer - Coupon table succesfully created.");
			} else
				throw new NameExistsException("Customer - Coupon Table");
		} catch (NameExistsException e) {
			e.printStackTrace();
		}
	}

	public void createCompanysCouponTable() throws Exception {
		Connection connection = DriverManager.getConnection(getDBURL());
		String sql = "CREATE TABLE if not exists companys_coupon (\r\n" + "company_id int NOT NULL ,\r\n"
				+ "coupon_id int NOT NULL PRIMARY KEY ,    \r\n"
				+ "FOREIGN KEY (company_id) REFERENCES companys(id) ON DELETE cascade,\r\n"
				+ "FOREIGN KEY (coupon_id) REFERENCES coupons(id) ON DELETE cascade\r\n" + ");";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			if (preparedStatement.executeUpdate() == 0) {
				System.out.println("Company - Coupon table succesfully created.");
			} else
				throw new NameExistsException("Company - Coupon Table");
		} catch (NameExistsException e) {
			e.printStackTrace();
		}
	}

	// drop table method
	public void dropCouponsTable() throws Exception {
		Connection connection = DriverManager.getConnection(getDBURL());
		String sql = "DROP table if exists Coupons";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			if (preparedStatement.executeUpdate() == 0) {
				System.out.println("Coupon table succesfully dropped.");
			} else
				throw new EmptyException("Coupon Table");
		} catch (EmptyException e) {
			e.printStackTrace();
		}
	}

	public void dropCustomersTable() throws Exception {
		Connection connection = DriverManager.getConnection(getDBURL());
		String sql = "DROP table if exists Customers";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			if (preparedStatement.executeUpdate() == 0) {
				System.out.println("Customers table succesfully dropped.");
			} else
				throw new EmptyException("Customer Table");
		} catch (EmptyException e) {
			e.printStackTrace();
		}
	}

	public void dropCompanysTable() throws Exception {
		Connection connection = DriverManager.getConnection(getDBURL());
		String sql = "DROP table if exists Companys";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			if (preparedStatement.executeUpdate() == 0) {
				System.out.println("Companys table succesfully dropped.");
			} else
				throw new EmptyException("Companys Table");
		} catch (EmptyException e) {
			e.printStackTrace();
		}
	}

	public void dropCompanyCouponTable() throws Exception {
		Connection connection = DriverManager.getConnection(getDBURL());
		String sql = "DROP table if exists Companys_Coupon";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			if (preparedStatement.executeUpdate() == 0) {
				System.out.println("Company_Coupon table succesfully dropped.");
			} else
				throw new EmptyException("Company_Coupon Table");
		} catch (EmptyException e) {
			e.printStackTrace();
		}
	}

	public void dropCustomerCouponTable() throws Exception {
		Connection connection = DriverManager.getConnection(getDBURL());
		String sql = "DROP table if exists Customers_Coupon";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			if (preparedStatement.executeUpdate() == 0) {
				System.out.println("Customer_Coupon table succesfully dropped.");
			} else
				throw new EmptyException("Customer_Coupon Table");
		} catch (EmptyException e) {
			e.printStackTrace();
		}
	}

	// alter table method
	public void alterTable(String tableName, String oldColumn, String newColumn, String type) throws Exception {
		Connection connection = DriverManager.getConnection(getDBURL());
		String sql = "ALTER table if exists" + tableName + "CHANGE " + oldColumn + " " + newColumn + " " + type;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			if (preparedStatement.executeUpdate() != 0) {
				System.out.println("Alter action succesfully --> " + "ALTER table if exists" + tableName + "CHANGE " + oldColumn + " " + newColumn + " " + type);
			} else
				throw new EmptyException("Customer_Coupon Table");
		} catch (EmptyException e) {
			e.printStackTrace();
		}
	}
}
