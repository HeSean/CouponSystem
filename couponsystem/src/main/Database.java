package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

import exception.WrongInfoInsertedException;
import exception.EmptyException;
import exception.NameExistsException;

public class Database {

	private static String connectionString = "jdbc:mysql://localhost:3306/CouponSystem?user=user&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT";

	public static String getDBURL() {
		return connectionString;
	}

	// Create table method
	public void createCouponsTable() throws NameExistsException {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(getDBURL());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sql = "CREATE TABLE Coupons (id int(64) NOT NULL AUTO_INCREMENT PRIMARY KEY , title varchar(45)  NOT NULL, start_Date Date  NOT NULL, end_Date Date  NOT NULL,  amount int (64)  NOT NULL, type ENUM(\"RESTAURANTS\",\r\n"
				+ "		\"ELECTRICITY\",\r\n" + "		\"FOOD\",\r\n" + "		\"HEALTH\",\r\n"
				+ "		\"SPORTS\",\r\n" + "		\"CAMPING\",\r\n"
				+ "		\"TRAVELLING\")  NOT NULL, message varchar(45)  NOT NULL, price double, image varchar(45)  NOT NULL);";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.executeUpdate();
			System.out.println("Coupon table succesfully created.");
		} catch (SQLSyntaxErrorException e) {
			throw new NameExistsException("Coupon table");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createCustomersTable() throws NameExistsException {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(getDBURL());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String sql = "CREATE TABLE Customers (id int(64) NOT NULL AUTO_INCREMENT PRIMARY KEY , cust_name varchar(45)  NOT NULL, password varchar(45)  NOT NULL);";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.executeUpdate();
			System.out.println("Customer table succesfully created.");
		} catch (SQLSyntaxErrorException e) {
			throw new NameExistsException("Customer table");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createCompanysTable() throws NameExistsException {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(getDBURL());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String sql = "CREATE TABLE companys (id int(64) NOT NULL AUTO_INCREMENT PRIMARY KEY , comp_name varchar(45)  NOT NULL, password varchar(45)  NOT NULL, email varchar(45)  NOT NULL);";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.executeUpdate();
			System.out.println("Companys table succesfully created.");
		} catch (SQLSyntaxErrorException e) {
			throw new NameExistsException("Companys table");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void createCustomerCouponTable() throws NameExistsException {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(getDBURL());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sql = "\r\n" + "CREATE TABLE customers_coupon (\r\n" + "customer_id int NOT NULL ,\r\n"
				+ "coupon_id int NOT NULL ,    \r\n"
				+ "FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE cascade,\r\n"
				+ "FOREIGN KEY (coupon_id) REFERENCES coupons(id) ON DELETE cascade,\r\n"
				+ "primary key (customer_id, coupon_id)\r\n" + ");";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.executeUpdate();
			System.out.println("Customer - Coupon table succesfully created.");
		} catch (SQLSyntaxErrorException e) {
			throw new NameExistsException("Customer - Coupon table");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createCompanysCouponTable() throws NameExistsException {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(getDBURL());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String sql = "CREATE TABLE companys_coupon (\r\n" + "company_id int NOT NULL ,\r\n"
				+ "coupon_id int NOT NULL,    \r\n"
				+ "FOREIGN KEY (company_id) REFERENCES companys(id) ON DELETE cascade,\r\n"
				+ "FOREIGN KEY (coupon_id) REFERENCES coupons(id) ON DELETE cascade,\r\n"
				+ "primary key (company_id, coupon_id)\r\n" + ");";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.executeUpdate();
			System.out.println("Company - Coupon table succesfully created.");
		} catch (SQLSyntaxErrorException e) {
			throw new NameExistsException("Company - Coupon table");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// drop table method
	public void dropCouponsTable() throws EmptyException {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(getDBURL());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String sql = "DROP table Coupons";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.executeUpdate();
			System.out.println("Coupon table succesfully dropped.");
		} catch (SQLSyntaxErrorException e) {
			throw new EmptyException("Coupon table does not exist. ");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void dropCustomersTable() throws EmptyException {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(getDBURL());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String sql = "DROP table Customers";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.executeUpdate();
			System.out.println("Customers table succesfully dropped.");
		} catch (SQLSyntaxErrorException e) {
			throw new EmptyException("Customers table does not exist. ");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void dropCompanysTable() throws EmptyException {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(getDBURL());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String sql = "DROP table Companys";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.executeUpdate();
			System.out.println("Companys table succesfully dropped.");
		} catch (SQLSyntaxErrorException e) {
			throw new EmptyException("Companys table does not exist. ");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void dropCompanyCouponTable() throws EmptyException {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(getDBURL());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String sql = "DROP table Companys_Coupon";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.executeUpdate();
			System.out.println("Company_Coupon table succesfully dropped.");
		} catch (SQLSyntaxErrorException e) {
			throw new EmptyException("Company_Coupon table does not exist. ");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void dropCustomerCouponTable() throws EmptyException {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(getDBURL());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String sql = "DROP table Customers_Coupon";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.executeUpdate();
			System.out.println("Customer_Coupon table succesfully dropped.");
		} catch (SQLSyntaxErrorException e) {
			throw new EmptyException("Customers_Coupon table does not exist. ");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// alter table method
	public void alterTable(String tableName, String oldColumn, String newColumn, String type) throws Exception {
		Connection connection = DriverManager.getConnection(getDBURL());
		String sql = "ALTER table if exists" + tableName + "CHANGE " + oldColumn + " " + newColumn + " " + type;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			if (preparedStatement.executeUpdate() != 0) {
				System.out.println("Alter action succesfull --> " + "ALTER table if exists" + tableName + "CHANGE "
						+ oldColumn + " " + newColumn + " " + type);
			} else
				throw new WrongInfoInsertedException("Customer_Coupon Table");
		} catch (WrongInfoInsertedException e) {
			e.printStackTrace();
		}
	}
}
