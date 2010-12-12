package de.wi08e.myhome.frontend;

import java.util.UUID;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import de.wi08e.myhome.frontend.exceptions.*;
import de.wi08e.myhome.usermanager.SessionUserToken;

/**
 * This is the main frontend interface. It is modeled to be a SOAP-Interface via javax.jws.WebService
 * 
 * Most methods require an userToken. This token can be acquired by login() and is valid for a certain 
 * time (default: 30min) after the last successful request with this token.
 * 
 * @author Marek
 *
 */

@WebService
public class FrontendInterface {

	/**
	 * The main login method
	 * @param username The username of the person trying to log in. It's case-insensitive
	 * @param password The corresponding password. It's case-sensitive
	 * @throws LoginUsernameOrPasswordWrong Thrown when password or username is wrong
	 * @return LoginReponse The returning class contains an "isAdmin"-boolean and the newly generated userToken
	 */
	@SOAPBinding(style = Style.RPC)
	public LoginResponse login(String username, String password) throws LoginUsernameOrPasswordWrong {
		System.out.println(username);
		System.out.println(password);
	
		if (username.length() == 0)
			throw new LoginUsernameOrPasswordWrong();
		
		LoginResponse result = new LoginResponse();
		result.isAdmin = true;
		result.userToken = SessionUserToken.INSTANCE.generate();
		return result;	
	}
	
	/**
	 * Checks if userToken is still valid
	 * @param userToken
	 * @return true if valid
	 */
	@SOAPBinding(style = Style.RPC)
	public boolean checkUserToken(String userToken) {
		return userToken.length() > 0;	
	}
	
	/**
	 * Logging the user out
	 * @param userToken
	 * @return true if valid
	 * @throws NotLoggedIn Is thrown when the given userToken can't be found. This mostly happens after a session timeout 
	 */
	@SOAPBinding(style = Style.RPC)
	public void logout(String userToken) throws NotLoggedIn {
		if ("".equals(userToken))
			throw new NotLoggedIn();
	}
	
	/**
	 * Lists all users or just one special user (requires admin rights)
	 * @param userToken Session user token
	 * @throws NotLoggedIn Is thrown when the given userToken can't be found. This mostly happens after a session timeout 
	 * @throws NoAdminRights Is thrown when the user has no admin rights and therefore can't see this list.
	 * @return Array of UserResponse containing username, fullname and isAdmin flag
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
	
	/**
	 * Returns the user with the given username (requires admin rights)
	 * @param userToken Session user token
	 * @param username Username of the requested user 
	 * @throws NotLoggedIn Is thrown when the given userToken can't be found. This mostly happens after a session timeout 
	 * @throws NoAdminRights Is thrown when the user has no admin rights and therefore can't use this function.
	 * @throws UserNotFound Thrown when username is not found in database
	 * @return UserResponse UserResponse object containing username, fullname and isAdmin flag
	 */
	@SOAPBinding(style = Style.RPC)
	public UserResponse getUser(String userToken, String username) throws NotLoggedIn, NoAdminRights, UserNotFound {

		if ("".equals(userToken))
			throw new NotLoggedIn();

		if (!"1234".equals(userToken))
			throw new NoAdminRights();
		
		if ("".equals(username))
			throw new UserNotFound();

		return new UserResponse("hans", "Hans Meier", true);
	}
	
	/**
	 * Deletes an user (requires admin rights)
	 * @param userToken Session user token
	 * @param username Username of the user which should be deleted
	 * @throws NotLoggedIn Is thrown when the given userToken can't be found. This mostly happens after a session timeout 
	 * @throws NoAdminRights Is thrown when the user has no admin rights and therefore can't use this function.
	 * @throws UserNotFound Thrown when username is not found in database
	 */
	@SOAPBinding(style = Style.RPC)
	public void deleteUser(String userToken, String username) throws NotLoggedIn, NoAdminRights, UserNotFound {

		if ("".equals(userToken))
			throw new NotLoggedIn();

		if (!"1234".equals(userToken))
			throw new NoAdminRights();	
		
		if ("".equals(username))
			throw new UserNotFound();

	}
}
