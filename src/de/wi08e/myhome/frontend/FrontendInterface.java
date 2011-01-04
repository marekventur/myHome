package de.wi08e.myhome.frontend;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import de.wi08e.myhome.frontend.exceptions.*;
import de.wi08e.myhome.httpserver.HTTPServer;
import de.wi08e.myhome.model.NamedNode;
import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.statusmanager.StatusManager;
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

	private final static Logger LOGGER = Logger.getLogger(HTTPServer.class.getName());
	
	private StatusManager statusManager;
	
	public FrontendInterface(StatusManager statusManager) {
		this.statusManager = statusManager;
	}


	private void requestUserRights(String userToken) throws NotLoggedIn {
		
	}
	
	private void requestAdminRights(String userToken) throws NotLoggedIn, NoAdminRights {
		
	}
	
	/**
	 * The main login method
	 * @param username The username of the person trying to log in. It's case-insensitive
	 * @param password The corresponding password. It's case-sensitive
	 * @throws LoginUsernameOrPasswordWrong Thrown when password or username is wrong
	 * @return LoginReponse The returning class contains an "isAdmin"-boolean and the newly generated userToken
	 */
	@SOAPBinding(style = Style.RPC)
	public LoginResponse login(String username, String password) throws LoginUsernameOrPasswordWrong {
		LOGGER.info("Login attempt: "+username);
	
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
	
	/**
	 * Change the password for an user (requires admin rights when not changing you own password)
	 * @param userToken Session user token
	 * @param username Username of the user which should be deleted
	 * @param password New password
	 * @throws NotLoggedIn Is thrown when the given userToken can't be found. This mostly happens after a session timeout 
	 * @throws NoAdminRights Is thrown when the user has no admin rights and therefore can't use this function.
	 * @throws UserNotFound Thrown when username is not found in database
	 * @throws PasswordTooShort Thrown when the password is too short
	 */
	@SOAPBinding(style = Style.RPC)
	public void changePassword(String userToken, String username, String password) throws NotLoggedIn, NoAdminRights, UserNotFound, PasswordTooShort {

		if ("".equals(userToken))
			throw new NotLoggedIn();

		if (!"1234".equals(userToken) && !"hans".equals(username))
			throw new NoAdminRights();	
		
		if ("".equals(username))
			throw new UserNotFound();
		
		if (password.length()<8)
			throw new PasswordTooShort(8);
	}
	
	/**
	 * Creates a new user account (requires admin rights)
	 * @param userToken Session user token
	 * @param username Username of the user which should be added
	 * @param fullname Fullname of the user which should be added
	 * @param password Password
	 * @throws NotLoggedIn Is thrown when the given userToken can't be found. This mostly happens after a session timeout 
	 * @throws NoAdminRights Is thrown when the user has no admin rights and therefore can't use this function.
	 * @throws PasswordTooShort Thrown when the password is too short
	 * @throws UsernameTooShortOrInvalid Thrown when username is too short or invalid
	 * @throws UsernameAlreadyInUse Thrown when username is already used by someone else
	 */
	@SOAPBinding(style = Style.RPC)
	public void addUser(String userToken, String username, String password) throws NotLoggedIn, NoAdminRights, PasswordTooShort, UsernameTooShortOrInvalid, UsernameAlreadyInUse {

		if ("".equals(userToken))
			throw new NotLoggedIn();

		if (!"1234".equals(userToken))
			throw new NoAdminRights();	
		
		if (username.length() < 4)
			throw new UsernameTooShortOrInvalid(4);
		
		if ("hans".equals(username))
			throw new UsernameAlreadyInUse();
		
		if (password.length() < 8)
			throw new PasswordTooShort(8);
	}
	
	/* Blueprints */
	
	@SOAPBinding(style = Style.RPC)
	public BlueprintResponse[] listBlueprints(String userToken) throws NotLoggedIn {

		requestUserRights(userToken);
		
		BlueprintResponse erdgeschoss = new BlueprintResponse();
		erdgeschoss.height = 300;
		erdgeschoss.width = 200;
		erdgeschoss.name = "erdgeschoss";
		
		BlueprintResponse obergeschoss = new BlueprintResponse();
		obergeschoss.height = 400;
		obergeschoss.width = 300;
		obergeschoss.name = "obergeschoss";
		
		return new BlueprintResponse[] {erdgeschoss, obergeschoss};  
	}
	
	@SOAPBinding(style = Style.RPC)
	public BlueprintResponse getBlueprint(String userToken, int blueprintId, int maxHeight, int maxWidth) throws NotLoggedIn, BlueprintNotFound {

		requestUserRights(userToken);
		
		BlueprintResponse erdgeschoss = new BlueprintResponse();
		erdgeschoss.height = 300;
		erdgeschoss.width = 200;
		erdgeschoss.name = "erdgeschoss";
		erdgeschoss.image = "pngblabla";
		
		return erdgeschoss;  
	}
	
	@SOAPBinding(style = Style.RPC)
	public void addBlueprint(String userToken, String name, String image, String imageType) throws NotLoggedIn, NoAdminRights {
		requestAdminRights(userToken); 
	}

	@SOAPBinding(style = Style.RPC)
	public void deleteBlueprint(String userToken, int id) throws NotLoggedIn, NoAdminRights, BlueprintNotFound {
		requestAdminRights(userToken); 
	}
	
	/* Nodes */
	
	/**
	 * Returns nodes
	 * @param filterByBlueprint -1 when no filtering should be applied, else any valid Blueprint ID
	 */
	@SOAPBinding(style = Style.RPC)
	public NodeResponse[] getNodes(String userToken, int filterByBlueprint) throws NotLoggedIn, BlueprintNotFound {
		requestUserRights(userToken);
		
		List<Node> nodes = statusManager.getAllNodes();
		
		NodeResponse[] result = new NodeResponse[nodes.size()];
		int i=0;
		for (Node node: nodes) 
			result[i++] = new NodeResponse(node);
		
		return result; 
	}

	@SOAPBinding(style = Style.RPC)
	public NodeResponse[] getUnnamedNodes(String userToken) throws NotLoggedIn, NoAdminRights {
		requestAdminRights(userToken);
		
		NodeResponse node1 = new NodeResponse();
		node1.status = new NodeStatusResponse[] {new NodeStatusResponse("key1", "value1"), new NodeStatusResponse("key2", "value2")}; 
		
		return new NodeResponse[] {node1}; 
	}
	
	/* Manage trigger (requires admin rights) */
	
	@SOAPBinding(style = Style.RPC)
	public int[] getSender(String userToken, int ReceiverId) throws NotLoggedIn, NoAdminRights {
		requestAdminRights(userToken);
		
		List<Integer> ids = statusManager.getSenderIds(ReceiverId);
		int[] result = new int[ids.size()];
		int i=0;
		for (Integer id: ids) 
			result[i++] = id;
		
		return result;
	}
	
}
