package company;

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

public class CompanyDBDAO implements CompanyDAO {

	CouponDBDAO couponDBDAO = new CouponDBDAO();

	public CompanyDBDAO() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createCompany(Company company) throws Exception {
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
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
			connection.close();
		}
	}

	// checking to see if a company already exists with wanted name
	public boolean checkCompanyName(Company company) throws Exception {
		boolean exists = false;
		ArrayList<String> names = new ArrayList<>();
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
		Statement statement = connection.createStatement();
		String sql = "SELECT comp_name FROM companys";
		ResultSet resultSet = statement.executeQuery(sql);

		while (resultSet.next()) {
			String companyName = resultSet.getString("comp_name");
			names.add(companyName);
		}
		for (String name : names) {
			if (name.equals(company.getCompName())) {
				return true;
			}
		}
		connection.close();
		return exists;
	}

	@Override
	public void removeCompany(Company company) throws Exception {
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
		String couponSQL = String.format("delete from companys where id=?");
		String couponsCompanySQL = String.format("delete * company_coupon where comp_ID=?");
		PreparedStatement preparedStatement = connection.prepareStatement(couponSQL);
		try {
			connection.setAutoCommit(false);
			preparedStatement.setLong(1, company.getId());
			preparedStatement.executeUpdate();
			System.out.println("Comapny removal from Company table succesful");

			preparedStatement = connection.prepareStatement(couponsCompanySQL);
			connection.setAutoCommit(false);
			preparedStatement.setLong(1, company.getId());
			preparedStatement.executeUpdate();
			System.out.println("Coupons removal succesful from Company - Coupon Table");
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}

	}

	@Override
	public void updateCompany(Company company) throws Exception {
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
		String sql = String.format("UPDATE companys set email = ?, password = ? WHERE id = ?");
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			connection.setAutoCommit(false);
			preparedStatement.setString(1, company.getEmail());
			preparedStatement.setString(2, company.getPassword());
			preparedStatement.setLong(3, company.getId());
			preparedStatement.executeUpdate();
			System.out.println("\nUpdate succesful. New Data - " + company);
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	@Override
	public Company getCompany(long id) throws Exception {
		// Company company = new Company();
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
		String sql = String.format("SELECT comp_Name, password, email FROM companys WHERE id=?");
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setLong(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {

				String compName = resultSet.getString("comp_Name");
				String password = resultSet.getString("password");
				String email = resultSet.getString("email");
				//System.out.printf("id- %d | name- %s | password - %s | email - %s\n", id, compName, password, email);
				// company.setId(id);
				// company.setCompName(compName);
				// company.setPassword(password);
				// company.setEmail(email); 
				return new Company(id, compName, password, email);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return null;
	}

	@Override
	public Collection<Company> getAllCompanys() throws Exception {
		ArrayList<Company> companys = new ArrayList<Company>();
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
		Statement statement = connection.createStatement();
		String sql = "select * from companys";
		ResultSet resultSet = statement.executeQuery(sql);

		while (resultSet.next()) {

			// int id = resultSet.getInt(1);
			long id = resultSet.getLong("id");
			String compName = resultSet.getString("comp_Name");
			String email = resultSet.getString("email");
			companys.add(new Company(id, compName, email));
			System.out.printf("id- %d | name- %s | email - %s\n", id, compName, email);
		}
		connection.close();
		return companys;
	}

	@Override
	public Collection<Coupon> getCoupons() throws Exception {
		
	}

//	// get all coupon ids created by company
//	public Collection<Long> getCouponsID(long wantedID) throws Exception {
//		Collection<Long> couponsID = new ArrayList<Long>();
//		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
//		String sql = "SELECT coupon_ID from company_coupon WHERE comp_ID = ?";
//		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//			preparedStatement.setLong(1, wantedID);
//			ResultSet resultSet = preparedStatement.executeQuery();
//			while (resultSet.next()) {
//				couponsID.add(resultSet.getLong("coupon_ID"));
//			}
//			connection.close();
//			return couponsID;
//		}
//	}
//	
//	//get all coupons the company created
//	public Collection<Coupon> getCoupons(Collection<Long> ids) throws Exception {
//		Collection<Coupon> coupons = new ArrayList<Coupon>();
//		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
//		ResultSet resultSet;
//		String sql = "select * from coupons WHERE id = ?";
//		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//			for (Long iLong : ids) {
//				preparedStatement.setLong(1, iLong);
//				resultSet = preparedStatement.executeQuery();
//				while (resultSet.next()) {
//					long id = resultSet.getLong("id");
//					String title = resultSet.getString("title");
//					LocalDate startDate = resultSet.getDate("start_Date").toLocalDate();
//					LocalDate endDate = resultSet.getDate("end_Date").toLocalDate();
//					int amount = resultSet.getInt("amount");
//					String type = resultSet.getString(6);
//					String message = resultSet.getString("message");
//					Double price = resultSet.getDouble("price");
//					String image = resultSet.getString("image");
//		
//					coupons.add(new Coupon(id, title, startDate, endDate, amount, type, message, price));
//				}
//			}
//			connection.close();
//			return coupons;
//		}
//	}	

	@Override
	public boolean login(String compName, String givenPassword) throws Exception {
		boolean correctInitials = false;
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
		String sql = String.format("SELECT password FROM companys WHERE comp_name = ?");
		String password = null;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, compName);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				password = resultSet.getString("password");
			}
			if (givenPassword == password ) {
				correctInitials =  true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return correctInitials;	
	}

}
