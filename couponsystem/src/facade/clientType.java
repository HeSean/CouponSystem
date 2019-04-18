package facade;

public enum clientType {
		 ADMINISTRATOR("Administrator"),
		COMPANY("Company"),
		CUSTOMER("Customer");
		
	   @SuppressWarnings("unused")
	private String clientType;

       private clientType (String value) {
               this. clientType = value;
       }
}


