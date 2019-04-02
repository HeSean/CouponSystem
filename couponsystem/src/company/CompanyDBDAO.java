package company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import coupon.Coupon;

public class CompanyDBDAO implements CompanyDAO{

	public  CompanyDBDAO() {
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
			System.out.println("New customer creation succeeded." + company.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	@Override
	public void removeCompany(Company company) throws Exception {
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());

		String sql = String.format("delete from companys where id=?");
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			connection.setAutoCommit(false);
			preparedStatement.setLong(1, company.getId());
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
	public void updateCompany(Company company) throws Exception {
		
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
		String sql = String.format("UPDATE companys set comp_Name = ?, password = ? WHERE id = ?");
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			connection.setAutoCommit(false);
			preparedStatement.setString(1, company.getCompName());
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
		//Company company = new Company();
		Connection connection = DriverManager.getConnection(main.Database.getDBURL());
		String sql = String.format("SELECT comp_Name, password, email FROM companys WHERE id=?");
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setLong(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				
			String compName = resultSet.getString("comp_Name");
			String password = resultSet.getString("password");
			String email = resultSet.getString("email");
			System.out.printf("id- %d | name- %s | password - %s | email - %s\n", id, compName, password, email);
//			company.setId(id);
//			company.setCompName(compName);
//			company.setPassword(password);
//			company.setEmail(email);
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
			companys.add(new Company(id, compName,email));
			System.out.printf("id- %d | name- %s | email - %s\n", id, compName, email);
		}
		connection.close();
		return companys;
	}

	@Override
	public Collection<Coupon> getAllCoupons() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean login(String compName, String password) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
