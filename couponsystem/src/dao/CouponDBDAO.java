package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;

import exception.CouponPurchaseException;
import exception.EmptyException;
import exception.FailedConnectionException;
import javabeans.Company;
import javabeans.Coupon;
import javabeans.CouponType;
import javabeans.Customer;
import main.ConnectionPoolBlockingQueue;

public class CouponDBDAO implements CouponDAO {

	// private ConnectionPool pool;
	private CompanyDBDAO companyDBDAO;
	private ConnectionPoolBlockingQueue pool;

	public CouponDBDAO() {
		try {
			// pool = ConnectionPool.getInstance();
			pool = ConnectionPoolBlockingQueue.getInstance();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createCoupon(Coupon coupon) throws FailedConnectionException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = String.format(
				"INSERT INTO coupons (id, title, start_Date, end_Date, amount, type, message, price, image) VALUES (?,?,?,?,?,?,?,?,?)");
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql,
				PreparedStatement.RETURN_GENERATED_KEYS);) {
			preparedStatement.setLong(1, coupon.getId());
			preparedStatement.setString(2, coupon.getTitle());
			preparedStatement.setDate(3, Date.valueOf(coupon.getStartDate()));
			preparedStatement.setDate(4, Date.valueOf(coupon.getEndDate()));
			preparedStatement.setInt(5, coupon.getAmount());
			preparedStatement.setString(6, coupon.getType().name());
			preparedStatement.setString(7, coupon.getMessage());
			preparedStatement.setDouble(8, coupon.getPrice());
			preparedStatement.setString(9, coupon.getImage());
			preparedStatement.executeUpdate();
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			resultSet.next();
			System.out.println("New coupon submit into Coupons table succeeded. \n" + coupon);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
	}

	// checking to see if a coupon already exists with new name
	public boolean checkCouponName(Coupon coupon) throws FailedConnectionException {
		boolean exists = false;
		LinkedHashSet<String> names = new LinkedHashSet<>();
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = "SELECT title FROM coupons";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			ResultSet resultSet = preparedStatement.executeQuery(sql);
			while (resultSet.next()) {
				String couponName = resultSet.getString("title");
				names.add(couponName);
			}
			for (String name : names) {
				if (name.equals(coupon.getTitle())) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
		return exists;
	}

	// delete one item from DB stock on purchase
	public void deleteFromStockDB(Coupon boughtCoupon) throws FailedConnectionException {
		Connection connection = null;
		if (boughtCoupon.getAmount() > 0) {
			try {
				connection = pool.getConnection();
			} catch (FailedConnectionException e) {
				e.printStackTrace();
			}
		}
		String sql = String.format("UPDATE coupons SET amount = amount - 1 WHERE id = ?");
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			connection.setAutoCommit(false);
			preparedStatement.setLong(1, boughtCoupon.getId());
			preparedStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
	}

	// delete one item from JB stock on purchase
	public void deleteFromStockJB(Coupon boughtCoupon) throws Exception {
		if (boughtCoupon.getAmount() > 0) {

			int newAmount = boughtCoupon.getAmount() - 1;
			long companyID = 0;
			String companyName = null;
			String sqlCompanyID = String.format("SELECT * FROM companys_coupon WHERE coupon_id = ?");
			String sqlCompanyName = String.format("SELECT * FROM companys WHERE id = ?");
			Company wantedCompany = new Company();
			companyDBDAO = new CompanyDBDAO();
			ResultSet resultSet;
			PreparedStatement preparedStatement;

			Connection connection = DriverManager.getConnection(main.Database.getDBURL());
			try {
				// getting company ID
				preparedStatement = connection.prepareStatement(sqlCompanyID);
				preparedStatement.setLong(1, boughtCoupon.getId());
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					companyID = resultSet.getLong("company_id");
				}

				// getting company Name
				preparedStatement = connection.prepareStatement(sqlCompanyName);
				preparedStatement.setLong(1, companyID);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					companyName = resultSet.getString("comp_name");
				}
				// updating current coupons list to remove one item from coupon amount
				wantedCompany = companyDBDAO.getCompany(companyName);
				LinkedHashSet<Coupon> list = (LinkedHashSet<Coupon>) wantedCompany.getCoupons();
				for (Coupon coupon : list) {
					if (coupon.equals(boughtCoupon)) {
						coupon.setAmount(newAmount);
					}
				}
				wantedCompany.setCoupons(list);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				connection.close();
			}
		}
	}

	// insert new created coupon to company-coupon table
	public void insertCouponToCompanysCouponJoinTable(long couponID, long compID) throws FailedConnectionException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sqlCompanyCoupon = "INSERT INTO companys_coupon (company_id, coupon_id) VALUES (?,?)";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCompanyCoupon,
				PreparedStatement.RETURN_GENERATED_KEYS);) {
			preparedStatement.setLong(1, compID);
			preparedStatement.setLong(2, couponID);
			preparedStatement.executeUpdate();
			System.out.println("New coupon submit into Company - Coupon table succeeded.");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
	}

	// insert new created coupon to company-coupon table
	public void insertCouponToCustomersCouponJoinTable(long couponID, long custID) throws FailedConnectionException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sqlCustomerCoupon = "INSERT INTO customers_coupon (customer_id, coupon_id) VALUES (?,?)";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCustomerCoupon,
				PreparedStatement.RETURN_GENERATED_KEYS);) {
			preparedStatement.setLong(1, custID);
			preparedStatement.setLong(2, couponID);
			preparedStatement.executeUpdate();
			System.out.println("\nNew coupon submit into Customer - Coupon table succeeded.");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
	}

	@Override
	public void removeCoupon(Coupon coupon) throws FailedConnectionException {
		if (!doesCouponExist(coupon.getId())) {
			throw new EmptyException("Coupon ID " + coupon.getId()+ " does not exist in database." );
		}
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String couponsSQL = String.format("delete from coupons where id = ?");
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(couponsSQL);
			connection.setAutoCommit(false);
			preparedStatement.setLong(1, coupon.getId());
			preparedStatement.executeUpdate();
			System.out.println("Delete succesful from Coupons Table of coupon id = " + coupon.getId());
			connection.commit();
		} catch (EmptyException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
	}

	// find expired coupons from db if exists
	public void findExpiredCoupons() throws FailedConnectionException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = String.format("select id from coupons WHERE end_Date < current_date();");
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				System.out.println("Found an expired coupon..");
				removeCouponByID(resultSet.getLong("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
	}

	// remove coupon by id
	public void removeCouponByID(long id) throws FailedConnectionException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String couponsSQL = String.format("delete from coupons where id=?");
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(couponsSQL);
			preparedStatement.setLong(1, id);
			if (preparedStatement.execute()) {
				System.out.println("Delete succesful from Coupons Table of coupon - " + id);
			} else
				throw new EmptyException("No coupon found with id - " + id);
		} catch (EmptyException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
	}

	public boolean doesCouponExist(long id) throws FailedConnectionException {
		boolean doesCouponExist = false;
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = String.format("SELECT title FROM coupons WHERE id=?");
		String title = null;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setLong(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				title = resultSet.getString("title");
			}
			if (title != null) {
				doesCouponExist = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
		return doesCouponExist;
	}

	@Override
	public void updateCoupon(Coupon coupon) throws FailedConnectionException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = String.format(
				"UPDATE coupons set title = ?, start_Date = ?, end_Date = ?, amount = ?, type = ?, message = ?, price = ?, image = ? WHERE id = ?");
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			connection.setAutoCommit(false);
			preparedStatement.setLong(9, coupon.getId());
			preparedStatement.setString(1, coupon.getTitle());
			preparedStatement.setDate(2, Date.valueOf(coupon.getStartDate()));
			preparedStatement.setDate(3, Date.valueOf(coupon.getEndDate()));
			preparedStatement.setInt(4, coupon.getAmount());
			preparedStatement.setString(5, coupon.getType().name());
			preparedStatement.setString(6, coupon.getMessage());
			preparedStatement.setDouble(7, coupon.getPrice());
			preparedStatement.setString(8, coupon.getImage());

			preparedStatement.executeUpdate();
			System.out.println("Update succesful.\nNew Data - " + coupon);
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
	}

	@Override
	public Coupon getCoupon(long id) throws FailedConnectionException {
		Coupon wantedCoupon = new Coupon();
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = String.format(
				"SELECT title, start_Date, end_Date, amount, type, message, price, image FROM coupons WHERE id=?");
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setLong(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String title = resultSet.getString("title");
				LocalDate startDate = resultSet.getDate("start_Date").toLocalDate();
				LocalDate endDate = resultSet.getDate("end_Date").toLocalDate();
				int amount = resultSet.getInt("amount");
				String type = resultSet.getString("type");
				String message = resultSet.getString("message");
				Double price = resultSet.getDouble("price");
				String image = resultSet.getString("image");

//				System.out.printf(
//						"id- %d | title - %s | start date - %s | end date - %s | amount - %d | type - %s | message - %s | price - %.2f | image - %s\n",
//						id, title, startDate, endDate, amount, type, message, price, image);
				wantedCoupon.setId(id);
				wantedCoupon.setTitle(title);
				wantedCoupon.setStartDate(startDate);
				wantedCoupon.setEndDate(endDate);
				wantedCoupon.setAmount(amount);
				wantedCoupon.setType(type);
				wantedCoupon.setMessage(message);
				wantedCoupon.setPrice(price);
				wantedCoupon.setImage(image);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
		return wantedCoupon;
	}

	@Override
	public Collection<Coupon> getAllCoupons() throws FailedConnectionException, SQLSyntaxErrorException {
		LinkedHashSet<Coupon> coupons = new LinkedHashSet<Coupon>();
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = "select * from coupons";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			ResultSet resultSet = preparedStatement.executeQuery(sql);
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

				coupons.add(new Coupon(id, title, startDate, endDate, amount, type, message, price, image));
				// System.out.printf(
				// "\nid- %d | title - %s | start date - %s | end date - %s | amount - %d | type
				// - %s | message - %s | price - %.2f | image - %s",
				// id, title, startDate, endDate, amount, type, message, price, image);
			}
		} catch (SQLSyntaxErrorException e) {
			EmptyException ee = new EmptyException("Coupons table does not exist .");
			ee.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
		return coupons;
	}

	@Override
	public Collection<Coupon> getCouponByType(CouponType wantedType) throws FailedConnectionException {
		LinkedHashSet<Coupon> coupons = new LinkedHashSet<Coupon>();
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = "select * from coupons WHERE type = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, wantedType.name());
			ResultSet resultSet = preparedStatement.executeQuery();

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

				coupons.add(new Coupon(id, title, startDate, endDate, amount, type, message, price, image));
				System.out.printf(
						"id- %d | title - %s | start date - %s | end date - %s | amount - %d | type - %s | message - %s | price - %.2f  | image - %s\n",
						id, title, startDate, endDate, amount, type, message, price, image);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
		return coupons;

	}

	public Collection<Coupon> getCouponByPrice(double wantedPrice) throws FailedConnectionException {
		LinkedHashSet<Coupon> coupons = new LinkedHashSet<Coupon>();
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = "select * from coupons WHERE price < ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setDouble(1, wantedPrice);
			ResultSet resultSet = preparedStatement.executeQuery();

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

				coupons.add(new Coupon(id, title, startDate, endDate, amount, type, message, price, image));
				System.out.printf(
						"id- %d | title - %s | start date - %s | end date - %s | amount - %d | type - %s | message - %s | price - %.2f  | image - %s\n",
						id, title, startDate, endDate, amount, type, message, price, image);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
		return coupons;

	}

	public Collection<Coupon> getCouponByDate(LocalDate wantedDate) throws FailedConnectionException {
		LinkedHashSet<Coupon> coupons = new LinkedHashSet<Coupon>();
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = "select * from coupons WHERE end_Date > ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			Date date = Date.valueOf(wantedDate);
			preparedStatement.setDate(1, date);
			ResultSet resultSet = preparedStatement.executeQuery();

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

				coupons.add(new Coupon(id, title, startDate, endDate, amount, type, message, price, image));
				System.out.printf(
						"id- %d | title - %s | start date - %s | end date - %s | amount - %d | type - %s | message - %s | price - %.2f  | image - %s\n",
						id, title, startDate, endDate, amount, type, message, price, image);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
		return coupons;

	}

	// can the wanted coupon be bought?
	// 1 = yes
	// 2 = no coupons left in stock (amount <= 0)
	// 3 = coupon expired.
	public int canBuy(Customer customer, Coupon coupon) throws FailedConnectionException {
		// boolean isOkayToBuy = true;
		int msg = 1;
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
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
			// verify coupon amount > 0
			if (amount <= 0) {
				// isOkayToBuy = false;
				msg = 2;
			}
			// verify coupon hasnt expired
			if (eDate.before(new java.util.Date())) {
				// isOkayToBuy = false;
				msg = 3;
			}
		} catch (SQLSyntaxErrorException e) {
			EmptyException ee = new EmptyException("Coupons table does not exist. ");
			ee.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
		return msg;

	}

	// purchasing coupon
	public void purchaseCoupon(Customer customer, Coupon coupon) throws FailedConnectionException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = String.format("INSERT INTO customers_coupon (customer_ID, coupon_ID) VALUES (?,?)");
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setLong(1, customer.getId());
			preparedStatement.setLong(2, coupon.getId());
			preparedStatement.executeUpdate();
			System.out.println("Purchased coupon succesfully. Customer " + customer.getCustName() + " bought coupon - "
					+ coupon.getTitle());
			customer.addCoupon(coupon);
			deleteFromStockDB(coupon);
		} catch (SQLIntegrityConstraintViolationException e) {
			CouponPurchaseException c = new CouponPurchaseException(
					"Customer cannot buy more than one of the same coupon.");
			c.printStackTrace();
		} catch (SQLSyntaxErrorException e) {
			EmptyException ee = new EmptyException("Customers_Coupon table does not exist. ");
			ee.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
	}

}
