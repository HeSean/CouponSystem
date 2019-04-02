package company;

public class CompanyFacade {

	CompanyDBDAO companyDBDAO = new CompanyDBDAO();

	public void createCompany(Company company) throws Exception {
		companyDBDAO.createCompany(company);
	}

	public void removeCompany(Company company) throws Exception {
		companyDBDAO.removeCompany(company);
	}

	public void updateCompany(Company company) throws Exception {
		companyDBDAO.updateCompany(company);
	}

	public void getCompany(long id) throws Exception {
		companyDBDAO.getCompany(id);
	}

	public void getAllCompanies() throws Exception {
		companyDBDAO.getAllCompanys();
	}

}
