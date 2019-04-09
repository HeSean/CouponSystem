package clients;

public enum clientType {
		 ADMINISTRATOR("Administrator"),
		COMPANY("Company"),
		CUSTOMER("Customer");
		
	   private String clientType;

       private clientType (String value) {
               this. clientType = value;
       }
}


