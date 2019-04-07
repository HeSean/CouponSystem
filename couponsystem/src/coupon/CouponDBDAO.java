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

import customer.Customer;

public class CouponDBDAO implements CouponDAO {

	public CouponDBDAO() {
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
			System.out.println("\nNew coupon submit into Coupons table succeeded." + coupon);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}
	
	// insert new created coupon to company-coupon table
	public void insertCouponToJoinTable (Coupon coupon, long compID) throws Exception {
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
		String sqlCompanyCoupon = "INSERT INTO company_coupon (comp_ID, coupon_ID) VALUSE (?,?)";
		try  {
			PreparedStatement preparedStatement = connection.prepareStatement(sqlCompanyCoupon,
					PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement = connection.prepareStatement(sqlCompanyCoupon);
			preparedStatement.setLong(1, coupon.getId());
			preparedStatement.setLong(2, compID);
			preparedStatement.executeUpdate();
			System.out.println("\nNew coupon submit into Company - Coupon table succeeded." + coupon);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	// checking to see if a coupon already exists with new name
	public boolean checkCouponName(Coupon coupon) throws Exception {
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
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
		String couponsSQL = String.format("delete from coupons where id=?");
		String couponsCustomerSQL = String.format("delete * customer_coupon where coupon_ID=?");
		String couponsCompanySQL = String.format("delete * company_coupon where coupon_ID=?");
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(couponsSQL);
			connection.setAutoCommit(false);
			preparedStatement.setLong(1, coupon.getId());
			preparedStatement.executeUpdate();
			System.out.println("Delete succesful from coupons Table");

			preparedStatement = connection.prepareStatement(couponsCustomerSQL);
			connection.setAutoCommit(false);
			preparedStatement.setLong(1, coupon.getId());
			preparedStatement.executeUpdate();
			System.out.println("Delete succesful from Customer - Coupon Table");

			preparedStatement = connection.prepareStatement(couponsCompanySQL);
			connection.setAutoCommit(false);
			preparedStatement.setLong(1, coupon.getId());
			preparedStatement.executeUpdate();
			System.out.println("Delete succesful from Company - Coupon Table");
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}

	}

	@Override
	public void updateCoupon(Coupon coupon) throws Exception {
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
	// can the wanted coupon be bought? 1 = yes, 2 = customer has already bought a similar coupon, 3 = no coupons left in stock (amont <= 0), 4 = coupon expired.
	public int canBuy(Customer customer, Coupon coupon) throws Exception {
		// boolean isOkayToBuy = true;
		int msg = 1;
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
			// System.out.println("Amount " + amount + "\nEnd Date " + eDate);
			// 1. verify the client hasnt purchased a SIMILAR coupon before
			if (hasAlreadyBought(customer, coupon)) {
				// isOkayToBuy = false;
				msg = 2;
			}
			// 2. verify coupon amount > 0
			if (amount <= 0) {
				// isOkayToBuy = false;
				msg = 3;
			}
			// 3. verify coupon hasnt expired
			if (eDate.before(new java.util.Date())) {
				// isOkayToBuy = false;
				msg = 4;
			}
			return msg;
		}
	}
	
	// did the customer already purchase a coupon like this? false for no and true for yes
		public boolean hasAlreadyBought(Customer customer, Coupon coupon) throws Exception {
			boolean boughtOnce = false;
			ArrayList<Long> couponsID = new ArrayList<Long>();
			Connection connection = DriverManager.getConnection(main.Database.getDBURL());
			String sql = "select * from customer_coupon WHERE cust_ID = ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.setLong(1, customer.getId());
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					long id = resultSet.getLong("coupon_ID");
					couponsID.add(id);
				}
				for (Long couponID : couponsID) {
					if (couponID == coupon.getId()) {
						boughtOnce = true;
					}
				}
			} catch (Exception e) {
				System.out.println("no coupons were bought by any customer.");
			} finally {
				connection.close();
			}
			return boughtOnce;
		}

	// purchasing coupon
	public void purchaseCoupon(Customer customer, Coupon coupon) throws Exception {
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
		String sql = String.format("INSERT INTO customer_coupon (cust_ID, coupon_ID) VALUES (?,?)");
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql,
				PreparedStatement.RETURN_GENERATED_KEYS);) {
			preparedStatement.setLong(1, customer.getId());
			preparedStatement.setLong(2, coupon.getId());
			preparedStatement.executeUpdate();
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			resultSet.next();
			System.out.println("Purchased coupon succesfully. Customer " + customer.getCustName() + " bought coupon - "
					+ coupon.getTitle());
			customer.addCoupon(coupon);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}

	}

}
