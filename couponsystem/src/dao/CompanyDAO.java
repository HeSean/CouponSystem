package dao;

import java.util.Collection;

import javabeans.Company;
import javabeans.Coupon;

public interface CompanyDAO {
		public void createCompany (Company company) throws Exception;
		
		public void removeCompany (Company company) throws Exception;
		
		public void updateCompany(Company company) throws Exception;
		
		public Company getCompany (long id) throws Exception;
		
		public Collection<Company> getAllCompanys() throws Exception;

		public Collection<Coupon> getCoupons() throws Exception;

		public boolean login(String compName, String password) throws Exception;
}
