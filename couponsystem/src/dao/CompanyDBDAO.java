package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

import exception.EmptyException;
import exception.FailedConnectionException;
import exception.IncorrectCredentialsException;
import javabeans.Company;
import javabeans.Coupon;
import javabeans.CouponType;
import main.ConnectionPool;
import main.ConnectionPoolBlockingQueue;

@SuppressWarnings("unused")
public class CompanyDBDAO implements CompanyDAO {

	private long companyID;
	// private ConnectionPool pool;
	private CouponDBDAO couponDBDAO;
	private ConnectionPoolBlockingQueue pool;

	public CompanyDBDAO() {
		couponDBDAO = new CouponDBDAO();
		try {
			// pool = ConnectionPool.getInstance();
			pool = ConnectionPoolBlockingQueue.getInstance();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createCompany(Company company) throws FailedConnectionException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = String.format("INSERT INTO companys (id, comp_name, password,email) VALUES (?,?,?,?)");
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql,
				PreparedStatement.RETURN_GENERATED_KEYS);) {
			preparedStatement.setLong(1, company.getId());
			preparedStatement.setString(2, company.getCompName());
			preparedStatement.setString(3, company.getPassword());
			preparedStatement.setString(4, company.getEmail());
			preparedStatement.executeUpdate();

			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			resultSet.next();
			System.out.println("New company creation succeeded." + company.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
	}

	// checking to see if a company already exists with wanted name
	public boolean checkCompanyName(Company company) throws FailedConnectionException {
		boolean exists = false;
		LinkedHashSet<String> names = new LinkedHashSet<>();
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = "SELECT comp_name FROM companys";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			ResultSet resultSet = preparedStatement.executeQuery(sql);

			while (resultSet.next()) {
				String companyName = resultSet.getString("comp_name");
				names.add(companyName);
			}
			for (String name : names) {
				if (name.equals(company.getCompName())) {
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

	@Override
	public void removeCompany(Company company) throws FailedConnectionException {
		try {
			if (!doesCompanyExist(company.getId())) {
				throw new EmptyException("Company ID " + company.getId() + " does not exist in database.");
			}
		} catch (EmptyException e) {
			e.printStackTrace();
			return;
		}
		
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String companySQL = String.format("delete from companys where id=?");
		String couponsCompanySQL = String.format("delete from companys_coupon where company_ID=?");
		String couponsSQL = String.format("DELETE FROM coupons where message like ? ");

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(couponsCompanySQL);

			connection.setAutoCommit(false);
			preparedStatement.setLong(1, company.getId());
			preparedStatement.executeUpdate();
			System.out.println("Company removal succesful from Company - Coupon Table");

			preparedStatement = connection.prepareStatement(couponsSQL);
			connection.setAutoCommit(false);
			preparedStatement.setString(1, "%" + company.getCompName() + "%");
			preparedStatement.executeUpdate();
			System.out.println("Companys coupons removal succesful from Coupons Table");

			preparedStatement = connection.prepareStatement(companySQL);
			connection.setAutoCommit(false);
			preparedStatement.setLong(1, company.getId());
			preparedStatement.executeUpdate();
			System.out.println("Comapny removal from Company table succesful");
			connection.commit();
		} catch (SQLSyntaxErrorException e) {
			EmptyException ee = new EmptyException("Companys tables do not exist .");
			ee.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}

	}

	@Override
	public void updateCompany(Company company) throws FailedConnectionException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = String.format("UPDATE companys set email = ?, password = ? WHERE id = ?");
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			connection.setAutoCommit(false);
			preparedStatement.setString(1, company.getEmail());
			preparedStatement.setString(2, company.getPassword());
			preparedStatement.setLong(3, company.getId());
			preparedStatement.executeUpdate();
			System.out.println("Update succesful. New Data - " + company);
			connection.commit();
		} catch (SQLSyntaxErrorException e) {
			EmptyException ee = new EmptyException("Companys table does not exist .");
			ee.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
	}

	public boolean doesCompanyExist(long id) throws FailedConnectionException {
		boolean doesCompanyExist = false;
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = String.format("SELECT comp_name FROM companys WHERE id=?");
		String compName = null;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setLong(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				compName = resultSet.getString("comp_name");
			}
			if (compName != null) {
				doesCompanyExist = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
		return doesCompanyExist;
	}

	@Override
	public Company getCompany(long id) throws FailedConnectionException {
		try {
			if (!doesCompanyExist(id)) {
				throw new EmptyException("Company ID " + id + " does not exist in database.");
			}
		} catch (EmptyException e) {
			e.printStackTrace();
		}
		Company company = new Company();
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = String.format("SELECT comp_Name, password, email FROM companys WHERE id=?");
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setLong(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				company.setId(id);
				company.setCompName(resultSet.getString("comp_Name"));
				company.setPassword(resultSet.getString("password"));
				company.setEmail(resultSet.getString("email"));
				// System.out.printf("id- %d | name- %s | email - %s", id,
				// resultSet.getString("comp_Name"),
				// resultSet.getString("email"));
			}
		} catch (EmptyException e) {
			e.printStackTrace();
		} catch (SQLSyntaxErrorException e) {
			EmptyException ee = new EmptyException("Companys table does not exist .");
			ee.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
		return company;
	}

	@Override
	public Collection<Company> getAllCompanys() throws FailedConnectionException {
		LinkedHashSet<Company> companys = new LinkedHashSet<Company>();
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = "select * from companys";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			ResultSet resultSet = preparedStatement.executeQuery(sql);
			while (resultSet.next()) {
				long id = resultSet.getLong("id");
				String compName = resultSet.getString("comp_Name");
				String email = resultSet.getString("email");
				companys.add(new Company(id, compName, email));
				// System.out.printf("id- %d | name- %s | email - %s\n", id, compName, email);
			}
		} catch (SQLSyntaxErrorException e) {
			EmptyException ee = new EmptyException("Companys table does not exist .");
			ee.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
		return companys;
	}

	@Override
	public Collection<Coupon> getCoupons() throws Exception {
		LinkedHashSet<Long> couponsID = getCouponsID(companyID);
		return getCoupons(couponsID);
	}

	public Company getCompany(String name) throws FailedConnectionException {
		Company wantedCompany = new Company();
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = String.format("SELECT * FROM companys WHERE comp_name = ?");
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, name);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				long id = resultSet.getLong("id");
				String compName = resultSet.getString("comp_name");
				String password = resultSet.getString("password");
				String email = resultSet.getString("email");
				wantedCompany.setId(id);
				wantedCompany.setCompName(compName);
				wantedCompany.setPassword(password);
				wantedCompany.setEmail(email);
				wantedCompany.setCoupons(getCoupons(getCouponsID(id)));
				// System.out.println(wantedCompany);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
		return wantedCompany;
	}

	// get all coupon ids created by company
	public LinkedHashSet<Long> getCouponsID(long id) throws FailedConnectionException {
		LinkedHashSet<Long> couponsID = new LinkedHashSet<Long>();
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = "SELECT coupon_ID from companys_coupon WHERE company_ID = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setLong(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				couponsID.add(resultSet.getLong("coupon_ID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
		return couponsID;

	}

	// get all coupons the company created
	public Collection<Coupon> getCoupons(LinkedHashSet<Long> ids) throws FailedConnectionException {
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
					String image = resultSet.getString("image");

					coupons.add(new Coupon(id, title, startDate, endDate, amount, type, message, price, image));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			EmptyException ee = new EmptyException("Tried to run pointer on null object.");
			ee.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
		return coupons;

	}

	@Override
	public boolean login(String compName, String givenPassword) throws FailedConnectionException {
		boolean correctInitials = false;
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (FailedConnectionException e) {
			e.printStackTrace();
		}
		String sql = String.format("SELECT id, comp_name, password, email FROM companys WHERE comp_name = ?");
		String password = null, name = null;
		long id = 0;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, compName);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				password = resultSet.getString("password");
				name = resultSet.getString("comp_name");
				id = resultSet.getLong("id");
			}
			if (name == null) {
				throw new IncorrectCredentialsException("Company with that name doesnt exist.");
			}
			if (givenPassword.equals(password)) {
				correctInitials = true;
				companyID = id;
			} else {
				throw new IncorrectCredentialsException("Company with that password doesnt exist.");
			}
		} catch (IncorrectCredentialsException e) {
			e.printStackTrace();
		} catch (SQLSyntaxErrorException e) {
			EmptyException ee = new EmptyException("Companys table does not exist .");
			ee.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(connection);
		}
		return correctInitials;
	}

}
