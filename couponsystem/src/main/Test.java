package main;

public class Test {

	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		System.out.println("start");
		ConfigUtils configUtils = new ConfigUtils();
		CouponSystem.getInstance().startCouponsUpdater();

//		for (int i = 0; i< 2; i++) {
//			if (i == 0) {
//				configUtils.DropAll();
//			} else configUtils.Initialization();
//		}
//		

		// configUtils.Login();

		for (int i = 0; i < 20; i++) {
			System.out.println("taking some time");
		}
		
		//configUtils.AdminActions();

		// configUtils.CompanyActions();

		// configUtils.CustomerActions();
		CouponSystem.getInstance().stopTask();
		System.out.println("end");
	}

}
