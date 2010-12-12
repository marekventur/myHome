package de.wi08e.myhome.frontend;

import java.util.UUID;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import de.wi08e.myhome.frontend.exceptions.*;

@WebService
public class FrontendInterface {

	/**
	 * The main login method
	 * @param username
	 * @param password
	 * @return HashMap (isAdmin, userToken)
	 */
	@SOAPBinding(style = Style.RPC)
	public LoginResponse login(String username, String password) {
		System.out.println(username);
		System.out.println(password);
	
		LoginResponse result = new LoginResponse();
		result.isAdmin = true;
		result.userToken = UUID.randomUUID().toString();;
		return result;	
	}
	
	/**
	 * Lists all users or just one special user
	 * @param userToken
	 * @throws NotLoggedIn
	 * @throws NoAdminRights
	 * @return ArrayList of HashMap (username, isAdmin)
	 * @throws NotLoggedIn 
	 */
	@SOAPBinding(style = Style.RPC)
	public UserResponse[] listUsers(String userToken) throws NotLoggedIn, NoAdminRights {

		if ("".equals(userToken))
			throw new NotLoggedIn();

		if (!"1234".equals(userToken))
			throw new NoAdminRights();

		UserResponse hans = new UserResponse();;
		hans.username = "hans";
		hans.fullname = "Hans Meier";
		

		UserResponse peter = new UserResponse();;
		peter.isAdmin = true;
		peter.username = "peter";
		peter.fullname = "Peter Schmidt";
		

		return new UserResponse[] {hans, peter};
	}
}
