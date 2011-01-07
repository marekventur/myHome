package de.wi08e.myhome.frontend;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.ws.WebServiceContext;


import de.wi08e.myhome.frontend.exceptions.*;
import de.wi08e.myhome.frontend.httpserver.HTTPServer;
import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.Trigger;
import de.wi08e.myhome.nodemanager.NodeManager;
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

@SOAPBinding(style = Style.RPC)
@WebService(name="myHome") 
public class FrontendInterface {
	
	@Resource WebServiceContext wsContext;

	private final static Logger LOGGER = Logger.getLogger(HTTPServer.class.getName());
	
	private StatusManager statusManager;
	private NodeManager nodeManager;
	
	public FrontendInterface(NodeManager nodeManager, StatusManager statusManager) {
		this.nodeManager = nodeManager;
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
	public LoginResponse login(@WebParam(name="username") String username, @WebParam(name="password")  String password) throws LoginUsernameOrPasswordWrong {
		LOGGER.info("Login attempt: "+username);
	
		System.out.println(wsContext.getUserPrincipal());
		
		if (username.length() == 0)
			throw new LoginUsernameOrPasswordWrong();
		
		return new LoginResponse(true, SessionUserToken.INSTANCE.generate());
	
	}
	
	/**
	 * Checks if userToken is still valid
	 * @param userToken
	 * @return true if valid
	 */
	public boolean checkUserToken(@WebParam(name="userToken") String userToken) {
		return userToken.length() > 0;	
	}
	
	/**
	 * Logging the user out
	 * @param userToken
	 * @return true if valid
	 * @throws NotLoggedIn Is thrown when the given userToken can't be found. This mostly happens after a session timeout 
	 */
	public void logout(@WebParam(name="userToken") String userToken) throws NotLoggedIn {
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
	public UserResponse[] listUsers(@WebParam(name="userToken") String userToken) throws NotLoggedIn, NoAdminRights {

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
	public UserResponse getUser(@WebParam(name="userToken") String userToken,@WebParam(name="username")  String username) throws NotLoggedIn, NoAdminRights, UserNotFound {

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
	public void deleteUser(@WebParam(name="userToken") String userToken,@WebParam(name="username")  String username) throws NotLoggedIn, NoAdminRights, UserNotFound {

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
	public void changePassword(@WebParam(name="userToken") String userToken,@WebParam(name="username")  String username,@WebParam(name="password")  String password) throws NotLoggedIn, NoAdminRights, UserNotFound, PasswordTooShort {

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
	public void addUser(@WebParam(name="userToken") String userToken,@WebParam(name="username") String username,@WebParam(name="password") String password) throws NotLoggedIn, NoAdminRights, PasswordTooShort, UsernameTooShortOrInvalid, UsernameAlreadyInUse {

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
	
	public BlueprintResponse[] listBlueprints(@WebParam(name="userToken") String userToken) throws NotLoggedIn {

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
	
	public BlueprintResponse getBlueprint(@WebParam(name="userToken") String userToken,@WebParam(name="blueprintId") int blueprintId,@WebParam(name="maxHeight") int maxHeight,@WebParam(name="maxWidth") int maxWidth) throws NotLoggedIn, BlueprintNotFound {

		requestUserRights(userToken);
		
		BlueprintResponse erdgeschoss = new BlueprintResponse();
		erdgeschoss.height = 300;
		erdgeschoss.width = 200;
		erdgeschoss.name = "erdgeschoss";
		erdgeschoss.image = "pngblabla";
		
		return erdgeschoss;  
	}
	
	public void addBlueprint(@WebParam(name="userToken") String userToken,@WebParam(name="name") String name,@WebParam(name="image") String image,@WebParam(name="imageType") String imageType) throws NotLoggedIn, NoAdminRights {
		requestAdminRights(userToken); 
	}

	public void deleteBlueprint(@WebParam(name="userToken") String userToken,@WebParam(name="blueprintId") int blueprintId) throws NotLoggedIn, NoAdminRights, BlueprintNotFound {
		requestAdminRights(userToken); 
	}
	
	/* Nodes */
	
	/**
	 * Returns nodes
	 * @param filterByBlueprint 0(=left blank) when no filtering should be applied, else any valid Blueprint ID
	 */
	
	public NodeResponse[] getNodes(@WebParam(name="userToken") String userToken,@WebParam(name="blueprintId") int blueprintId) throws NotLoggedIn, BlueprintNotFound {
		requestUserRights(userToken);
		
		List<Node> nodes;
		
		if (blueprintId > 0)
			nodes = nodeManager.getAllNodesFilteredByBlueprint(blueprintId);
		else
			nodes = nodeManager.getAllNodes();
		
		NodeResponse[] result = new NodeResponse[nodes.size()];
		int i=0;
		for (Node node: nodes) 
			result[i++] = new NodeResponse(node);
		
		return result; 
	}

	public NodeResponse[] getUnnamedNodes(@WebParam(name="userToken") String userToken) throws NotLoggedIn {
		requestUserRights(userToken);
		
		List<Node> nodes = nodeManager.getUnnamedNodes();
		
		NodeResponse[] result = new NodeResponse[nodes.size()];
		int i=0;
		for (Node node: nodes) 
			result[i++] = new NodeResponse(node);
		
		return result; 
	}
	
	/* User defined nodes */
	public NodeResponse[] getUserdefinedNodes(@WebParam(name="userToken") String userToken) throws NotLoggedIn, NoAdminRights {
		requestAdminRights(userToken);
		
		List<Node> nodes = nodeManager.getUserdefinedNodes();
		
		NodeResponse[] result = new NodeResponse[nodes.size()];
		int i=0;
		for (Node node: nodes) 
			result[i++] = new NodeResponse(node);
		
		return result; 
	}
	
	public int addUserdefinedNodes(@WebParam(name="userToken")String userToken,@WebParam(name="name") String name,@WebParam(name="category") String category,@WebParam(name="type") String type) throws NotLoggedIn, NoAdminRights {
		requestAdminRights(userToken);		
		return nodeManager.addUserDefinedNode(name, category, type); 
	}
	
	
	/* Manage trigger (requires admin rights) */
	
	public TriggerResponse[] getSenderForReceiverTrigger(@WebParam(name="userToken") String userToken,@WebParam(name="receiverId") int receiverId) throws NotLoggedIn, NoAdminRights {
		requestAdminRights(userToken);
		
		List<Trigger> triggers = statusManager.getTriggerManager().getSender(receiverId);
		TriggerResponse[] result = new TriggerResponse[triggers.size()];
		int i=0;
		for (Trigger trigger: triggers) 
			result[i++] = new TriggerResponse(trigger);
		
		
		return result;
	}
	
	/**
	 * 
	 * @param userToken
	 * @param senderId
	 * @param receiverId
	 * @param channel empty for no channel, or A, B, C or D
	 * @throws NotLoggedIn
	 * @throws NoAdminRights
	 * @throws NodeNotFound
	 */
	
	public void addOrUpdateSenderToReceiverTrigger(@WebParam(name="userToken") String userToken,@WebParam(name="senderId") int senderId,@WebParam(name="receiverId") int receiverId,@WebParam(name="channel") String channel) throws NotLoggedIn, NoAdminRights, NodeNotFound {
		requestAdminRights(userToken);
		
		try {
			if (channel == null || channel.length() != 1) 
				statusManager.getTriggerManager().addSenderToReciver(senderId, receiverId);
			else
				statusManager.getTriggerManager().addSenderToReciver(senderId, receiverId, channel.charAt(0));
			
		} catch (SQLException e) {
			if (e.getMessage().contains("a foreign key constraint fails")) {
				throw new NodeNotFound();
			}
			else
			{
				// Do nothing - Trigger is already in DB ("Duplicated primary key")
			}
		} 
	}
	
	public void deleteTrigger(@WebParam(name="userToken") String userToken,@WebParam(name="senderId") int senderId,@WebParam(name="receiverId") int receiverId) throws NotLoggedIn, NoAdminRights {
		requestAdminRights(userToken);
		statusManager.getTriggerManager().deleteTrigger(senderId, receiverId);		
	}
	
}
