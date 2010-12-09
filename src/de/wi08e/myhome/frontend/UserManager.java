package de.wi08e.myhome.frontend;

import java.util.ArrayList;
import java.util.HashMap;

import de.wi08e.myhome.frontend.exceptions.*;

public class UserManager {
	
	/**
	 * Lists all users or just one special user
	 * @param userToken
	 * @throws NotLoggedIn
	 * @throws NoAdminRights
	 * @return ArrayList of HashMap (username, isAdmin)
	 * @throws NotLoggedIn 
	 */
	
	public ArrayList<HashMap<String,Object>> listUsers(String userToken) throws NotLoggedIn, NoAdminRights {
		
		if ("".equals(userToken))
			throw new NotLoggedIn();
		
		if (!"1234".equals(userToken))
			throw new NoAdminRights();
		
		ArrayList<HashMap<String,Object>> result = new ArrayList<HashMap<String,Object>>();
		
		HashMap<String, Object> hans = new HashMap<String, Object>();
		hans.put("isAdmin", new Boolean(false));
		hans.put("username", "Hans");
		result.add(hans);
		
		HashMap<String, Object> peter = new HashMap<String, Object>();
		peter.put("isAdmin", new Boolean(true));
		peter.put("username", "Peter");
		result.add(peter);
		
		return result;
	}
	
	/**
	 * Deletes an user
	 * @param token
	 */
	public void deleteUser(String token) {
		
	}
	
	
}
