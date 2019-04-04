package coupon;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class CouponDBDAO implements CouponDAO {

	public CouponDBDAO() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createCoupon(Coupon coupon) throws Exception {
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
		String sql = String.format(
				"INSERT INTO coupons (id, title, start_Date, end_Date, amount, type, message, price, image) VALUES (?,?,?,?,?,?,?,?,?)");

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql,
				PreparedStatement.RETURN_GENERATED_KEYS);) {
			preparedStatement.setLong(1, coupon.getId());
			preparedStatement.setString(2, coupon.getTitle());
			preparedStatement.setDate(3, coupon.getStartDate());
			preparedStatement.setDate(4, coupon.getEndDate());
			preparedStatement.setInt(5, coupon.getAmount());
			preparedStatement.setString(6, coupon.getType().name());
			preparedStatement.setString(7, coupon.getMessage());
			preparedStatement.setDouble(8, coupon.getPrice());
			preparedStatement.setString(9, coupon.getImage());

			preparedStatement.executeUpdate();

			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			resultSet.next();
			System.out.println("New coupon creation succeeded.\n" + coupon);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	public boolean checkCouponName(Coupon coupon) throws Exception { // checking to see if a coupon already exists with
																		// name
		boolean exists = false;
		ArrayList<String> names = new ArrayList<>();
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
		Statement statement = connection.createStatement();
		String sql = "SELECT title FROM coupons";
		ResultSet resultSet = statement.executeQuery(sql);

		while (resultSet.next()) {
			String couponName = resultSet.getString("title");
			names.add(couponName);
		}
		for (String name : names) {
			if (name.equals(coupon.getTitle())) {
				return true;
			}
		}
		connection.close();
		return exists;
	}

	@Override
	public void removeCoupon(Coupon coupon) throws Exception {
		// TODO Auto-generated method stub
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());

		String sql = String.format("delete from coupons where id=?");

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			connection.setAutoCommit(false);
			preparedStatement.setLong(1, coupon.getId());
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
	public void updateCoupon(Coupon coupon) throws Exception {
		// TODO Auto-generated method stub
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
		String sql = String.format(
				"UPDATE coupons set title = ?, start_Date = ?, end_Date = ?, amount = ?, type = ?, message = ?, price = ?, image = ? WHERE id = ?");
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			connection.setAutoCommit(false);
			preparedStatement.setLong(9, coupon.getId());
			preparedStatement.setString(1, coupon.getTitle());
			preparedStatement.setDate(2, coupon.getStartDate());
			preparedStatement.setDate(3, coupon.getEndDate());
			preparedStatement.setInt(4, coupon.getAmount());
			preparedStatement.setString(5, coupon.getType().name());
			preparedStatement.setString(6, coupon.getMessage());
			preparedStatement.setDouble(7, coupon.getPrice());
			preparedStatement.setString(8, coupon.getImage());

			preparedStatement.executeUpdate();
			System.out.println("\nUpdate succesful.\nNew Data - " + coupon);
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	@Override
	public Coupon getCoupon(long id) throws Exception {
		Coupon wantedCoupon = new Coupon();
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
		String sql = String.format(
				"SELECT title, start_Date, end_Date, amount, type, message, price, image FROM coupons WHERE id=?");
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setLong(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String title = resultSet.getString("title");
				String startDate = resultSet.getString("start_Date");
				String endDate = resultSet.getString("end_Date");
				int amount = resultSet.getInt("amount");
				String type = resultSet.getString("type");
				String message = resultSet.getString("message");
				Double price = resultSet.getDouble("price");
				String image = resultSet.getString("image");

				Date sDate = Date.valueOf(startDate);
				Date eDate = Date.valueOf(endDate);

				System.out.printf(
						"id- %d | title - %s | start date - %s | end date - %s | amount - %d | type - %s | message - %s | price - %.2f | image - %s ",
						id, title, sDate, eDate, amount, type, message, price, image);
				wantedCoupon.setTitle(title);
				wantedCoupon.setStartDate(sDate);
				wantedCoupon.setEndDate(eDate);
				wantedCoupon.setAmount(amount);
				wantedCoupon.setType(type);
				wantedCoupon.setMessage(message);
				wantedCoupon.setPrice(price);
				wantedCoupon.setImage(image);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return wantedCoupon;
	}

	@Override
	public Collection<Coupon> getAllCoupons() throws Exception {
		ArrayList<Coupon> coupons = new ArrayList<Coupon>();
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
		Statement statement = connection.createStatement();

		String sql = "select * from coupons";

		ResultSet resultSet = statement.executeQuery(sql);

		while (resultSet.next()) {
			long id = resultSet.getLong("id");
			String title = resultSet.getString("title");
			String startDate = resultSet.getString(3);
			String endDate = resultSet.getString(4);
			int amount = resultSet.getInt("amount");
			String type = resultSet.getString(6);
			String message = resultSet.getString("message");
			Double price = resultSet.getDouble("price");
			String image = resultSet.getString("image");
			Date sDate = Date.valueOf(startDate);
			Date eDate = Date.valueOf(endDate);

			coupons.add(new Coupon(id, title, sDate, eDate, amount, type, message, price));
			System.out.printf(
					"\nid- %d | title - %s | start date - %s | end date - %s | amount - %d | type - %s | message - %s | price - %.2f  | image - %s",
					id, title, sDate, eDate, amount, type, message, price, image);
		}
		System.out.println();
		connection.close();
		return coupons;
	}

	@Override
	public Collection<Coupon> getCouponByType(CouponType wantedType) throws Exception {
		ArrayList<Coupon> coupons = new ArrayList<Coupon>();
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());

		String sql = "select * from coupons WHERE type = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, wantedType.name());
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				long id = resultSet.getLong("id");
				String title = resultSet.getString("title");
				String startDate = resultSet.getString(3);
				String endDate = resultSet.getString(4);
				int amount = resultSet.getInt("amount");
				String type = resultSet.getString(6);
				String message = resultSet.getString("message");
				Double price = resultSet.getDouble("price");
				String image = resultSet.getString("image");
				Date sDate = Date.valueOf(startDate);
				Date eDate = Date.valueOf(endDate);

				coupons.add(new Coupon(id, title, sDate, eDate, amount, type, message, price));
				System.out.printf(
						"id- %d | title - %s | start date - %s | end date - %s | amount - %d | type - %s | message - %s | price - %.2f  | image - %s\n",
						id, title, sDate, eDate, amount, type, message, price, image);
			}
			connection.close();
			return coupons;
		}
	}

	public Collection<Coupon> getCouponByPrice(double wantedPrice) throws Exception {
		ArrayList<Coupon> coupons = new ArrayList<Coupon>();
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
		String sql = "select * from coupons WHERE price < ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setDouble(1, wantedPrice);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				long id = resultSet.getLong("id");
				String title = resultSet.getString("title");
				String startDate = resultSet.getString(3);
				String endDate = resultSet.getString(4);
				int amount = resultSet.getInt("amount");
				String type = resultSet.getString(6);
				String message = resultSet.getString("message");
				Double price = resultSet.getDouble("price");
				String image = resultSet.getString("image");
				Date sDate = Date.valueOf(startDate);
				Date eDate = Date.valueOf(endDate);

				coupons.add(new Coupon(id, title, sDate, eDate, amount, type, message, price));
				System.out.printf(
						"id- %d | title - %s | start date - %s | end date - %s | amount - %d | type - %s | message - %s | price - %.2f  | image - %s\n",
						id, title, sDate, eDate, amount, type, message, price, image);
			}
			connection.close();
			return coupons;
		}
	}

	public boolean canBuy(Coupon coupon) throws Exception {
		boolean isOkayToBuy = true;
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
		int amount = 0;
		String endDate = null;
		String sql = "SELECT amount, end_Date from coupons WHERE id = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setLong(1, coupon.getId());
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				 amount = resultSet.getInt("amount");
				 endDate = resultSet.getString("end_Date");
			}
			Date eDate = Date.valueOf(endDate);
			//System.out.println("Amount " + amount + "\nEnd Date " + eDate);
			// 1. verify the client hasnt purchased a SIMILAR coupon before
			
			// 2. verify coupon amount > 0
			if (amount <= 0) {
				isOkayToBuy = false;
			}
			// 3. verify coupon hasnt expired
			if (eDate.before(new java.util.Date())) {
				isOkayToBuy = false;
			}
			return isOkayToBuy;
		}
	}
}
