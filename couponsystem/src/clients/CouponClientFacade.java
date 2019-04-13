package clients;

import main.clientType;

public interface CouponClientFacade {
		
		public CouponClientFacade login (String name, String password, clientType c) throws Exception;
}
