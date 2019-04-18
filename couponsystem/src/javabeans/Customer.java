package javabeans;

import java.util.ArrayList;

public class Customer {
	private static int COUNTER = 1;
	private long id;
	private String custName;
	private String password;
	private ArrayList<Coupon> coupons = new ArrayList<Coupon>();
	
	public Customer() {
		
	}
	public Customer(long id, String custname) {
		setId(id);
		setCustName(custname);
	}
	public Customer(long id, String custName, String password) {
		setId(id);
		setPassword(password);
		setCustName(custName);
	}

	public Customer(String custName, String password) {
		super();
		setId(COUNTER++);
		setCustName(custName);
		setPassword(password);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public ArrayList<Coupon> getCoupons() {
		return this.coupons;	
	}
	
	public void setCoupons(ArrayList<Coupon> coupons) {
		this.coupons = coupons;
	}
	
	public void addCoupon(Coupon coupon) {
		this.coupons.add(coupon);
	}
	


	@Override
	public String toString() {
		return "Customer [ ID = " + id + " | Customer Name = " + custName + " | Password = " + password + " | Available Coupons = " + coupons
				+ "]";
	}



}
