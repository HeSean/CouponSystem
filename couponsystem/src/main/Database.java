package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

	private static String connectionString = "jdbc:mysql://localhost:3306/CouponSystem?user=user&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT";

	public static String getDBURL() {
		return connectionString;
	}

	public static void insertToTable(String name, double price)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		// insert table

		Connection connection = DriverManager.getConnection(connectionString);
		String sql = String.format("insert into Products(name, price) values('%s', %.2f)", name, price);

		PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		preparedStatement.executeUpdate();

		ResultSet resultSet = preparedStatement.getGeneratedKeys();
		resultSet.next();
		System.out.println("Insert succeeded.");
		connection.close();
	}

	public static void updateValueInTable(long id, String name, double price) throws SQLException {
		// update....set....where
		Connection connection = DriverManager.getConnection(connectionString);

		String sql = String.format("update Products set name='%s', price=%.2f where _id=%d", name, price, id);

		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.executeUpdate();
		System.out.println("Update successful.");
		connection.close();
	}

	public static void deleteFromTable(int id) throws SQLException {
		// delete from... where
		Connection connection = DriverManager.getConnection(connectionString);

		String sql = String.format("delete from Products where _id=%d", id);

		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.executeUpdate();

		System.out.println("Delete succesful");
		connection.close();

	}

	public static void getAll() throws SQLException {
		// SELECT * from table_name
		Connection connection = DriverManager.getConnection(connectionString);
		Statement statement = connection.createStatement();

		String sql = "select * from Products";

		ResultSet resultSet = statement.executeQuery(sql);

		while (resultSet.next()) {

			// int id = resultSet.getInt(1);
			int id = resultSet.getInt("_id");

			String name = resultSet.getString("name");
			double price = resultSet.getDouble("price");

			System.out.printf("id : %d, name : %s, price : %.2f\n ", id, name, price);
		}
		connection.close();

	}

}
