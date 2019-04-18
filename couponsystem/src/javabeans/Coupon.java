package javabeans;

import java.time.LocalDate;

public class Coupon {
	private static int COUNTER = 1;
	private long id;
	private String title;
	private LocalDate startDate;
	private LocalDate endDate;
	private int amount;
	private CouponType type;
	private String message;
	private double price;
	private String image;

	public Coupon() {

	}

	public Coupon(String title, LocalDate startDate, LocalDate endDate, int amount, CouponType type, String message,
			double price) {
		// String image) {
		super();
		setId(COUNTER++);
		setTitle(title);
		setStartDate(startDate);
		setEndDate(endDate);
		setAmount(amount);
		setType(type);
		setMessage(message);
		setPrice(price);
		setImage("null");
	}

	public Coupon(Long id, String title, LocalDate startDate, LocalDate endDate, int amount, CouponType type, String message,
			double price) {
		// String image) {
		super();
		setId(id);
		setTitle(title);
		setStartDate(startDate);
		setEndDate(endDate);
		setAmount(amount);
		setType(type);
		setMessage(message);
		setPrice(price);
		setImage("null");
	}

	public Coupon(Long id, String title, LocalDate sDate, LocalDate eDate, int amount, String type, String message,
			Double price) {
		super();
		setId(id);
		setTitle(title);
		setStartDate(sDate);
		setEndDate(eDate);
		setAmount(amount);
		setType(type);
		setMessage(message);
		setPrice(price);
		//setImage(image2);
	}

	private void setType(CouponType type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int newAmount) {
		amount = newAmount;
	}

	public CouponType getType() {
		return type;
	}

	public void setType(String type) {
		this.type = CouponType.valueOf(type);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "Coupon [ ID = " + id + " | Title = " + title + " | Start Date = " + startDate + " | End Date = "
				+ endDate + " | Amount = " + amount + " | Type = " + type + " | Message = " + message + " | Price = "
				+ price + "]";
	}

}
