package company;

import java.util.ArrayList;
import java.util.Collection;
import coupon.Coupon;

public class Company {
	private static int COUNTER = 1;
	private long id;
	private String compName;
	private String password;
	private String email;
	private Collection<Coupon> coupons = new ArrayList<Coupon>();

	public Company() {

	}
	
	public Company (long id, String compName, String email) {
		setId(id);
		setCompName(compName);
		setEmail(email);
	}

	public Company(String compName, String password, String email) {
		super();
		setId(++COUNTER);
		setCompName(compName);
		setPassword(password);
		setEmail(email);
	}

	public Company(long id2, String compName2, String password2, String email2) {
		setId(id2);
		setCompName(compName2);
		setPassword(password2);
		setEmail(email2);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Collection<Coupon> getCoupons() {
		return this.coupons;
	}
	
	public void addCoupon(Coupon coupon) {
		this.coupons.add(coupon);
	}


	@Override
	public String toString() {
		return "Company [ ID = " + id + " | Company Name = " + compName + " | Password = " + password + "| Email Address = " + email+ " | Available Coupons = " + coupons
				+ "]";
	}

	


}
