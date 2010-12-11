package de.wi08e.myhome.frontend;

import java.util.HashMap;
import java.util.UUID;

public class Login {
	
	/**
	 * The main login method
	 * @param username
	 * @param password
	 * @return HashMap (isAdmin, userToken)
	 */
	public HashMap<String, Object> login(String username, String password) {
		
		String userToken = UUID.randomUUID().toString();
		boolean isAdmin = true;
	
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("isAdmin", new Boolean(isAdmin));
		result.put("userToken", userToken);
		return result;
		
	}
}
