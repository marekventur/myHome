package de.wi08e.myhome.frontend;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.ws.WebServiceContext;


import de.wi08e.myhome.blueprintmanager.BlueprintManager;
import de.wi08e.myhome.exceptions.*;
import de.wi08e.myhome.frontend.httpserver.HTTPServer;
import de.wi08e.myhome.model.Blueprint;
import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.NodeWithPosition;
import de.wi08e.myhome.model.Trigger;
import de.wi08e.myhome.nodemanager.NodeManager;
import de.wi08e.myhome.statusmanager.StatusManager;
import de.wi08e.myhome.usermanager.SessionUserToken;
import de.wi08e.myhome.usermanager.UserManager;

/**
 * This is the main frontend interface. It is modeled to be a SOAP-Interface via javax.jws.WebService
 * 
 * Most methods require an userToken. This token can be acquired by login() and is valid for a certain 
 * time (default: 30min) after the last successful request with this token.
 * 
 * @author Marek
 */


@SOAPBinding(style = Style.RPC)
@WebService(name="myHome") 
public class FrontendInterface {
	
	@Resource WebServiceContext wsContext;

	private final static Logger LOGGER = Logger.getLogger(HTTPServer.class.getName());
	
	private StatusManager statusManager;
	private NodeManager nodeManager;
	private BlueprintManager blueprintManager;
	private UserManager userManager;
	
	public FrontendInterface(NodeManager nodeManager, StatusManager statusManager, BlueprintManager blueprintManager, UserManager userManager) {
		this.nodeManager = nodeManager;
		this.statusManager = statusManager;
		this.blueprintManager = blueprintManager;
		this.userManager = userManager;
	}

	/* Helper */
	
	private void requestUserRights(String userToken) throws NotLoggedIn {
		
	}
	/**
	 * @param userToken Session user token
	 * @throws NotLoggedIn Is thrown when the given userToken can't be found. This mostly happens after a session timeout 
	 * @throws NoAdminRights Is thrown when the user has no admin rights and therefore can't use this function.
	 */
	private void requestAdminRights(String userToken) throws NotLoggedIn, NoAdminRights {
		
	}
	
	private NodeResponse[] convertListToResponseArrayNode(List<Node> nodes) {
		NodeResponse[] result = new NodeResponse[nodes.size()];
		int i=0;
		for (Node node: nodes) 
			result[i++] = new NodeResponse(node);
		return result;
	}
	
	private NodeResponse[] convertListToResponseArrayNodeWithPosition(List<NodeWithPosition> nodes) {
		NodeResponse[] result = new NodeResponse[nodes.size()];
		int i=0;
		for (NodeWithPosition node: nodes) 
			result[i++] = new NodeResponse(node);
		return result;
	}
	
	/**
	 * @param triggers convert List to response Array Trigger
	 * @return new result when Trigger response (result i++)
	 */
	
	private TriggerResponse[] convertListToResponseArrayTrigger(List<Trigger> triggers) {
		TriggerResponse[] result = new TriggerResponse[triggers.size()];
		int i=0;
		for (Trigger trigger: triggers) 
			result[i++] = new TriggerResponse(trigger);
		return result;
	}	
	/**
	 * @param blueprints convert List to response Array Blueprint
	 * @return new result when Blueprint response (result i++)
	 */
	
	private BlueprintResponse[] convertListToResponseArrayBlueprint(List<Blueprint> blueprints) {
		BlueprintResponse[] result = new BlueprintResponse[blueprints.size()];
		int i=0;
		for (Blueprint blueprint: blueprints) 
			result[i++] = new BlueprintResponse(blueprint);
		return result;
	}	
	
	/* Usermanager */
	
	/**
	 * The main login method
	 * @param username The username of the person trying to log in. It's case-insensitive
	 * @param password The corresponding password. It's case-sensitive
	 * @throws LoginUsernameOrPasswordWrong Thrown when password or username is wrong
	 * @return LoginReponse The returning class contains an "isAdmin"-boolean and the newly generated userToken
	 */
	
	public LoginResponse login(@WebParam(name="username") String username, @WebParam(name="password")  String password) throws LoginUsernameOrPasswordWrong {
	
		if (!username.contentEquals("admin") || !username.contentEquals("admin"))
			throw new LoginUsernameOrPasswordWrong();
		
		return new LoginResponse(true, SessionUserToken.INSTANCE.generate());
	
	}
	
	/**
	 * Checks if userToken is still valid
	 * @param userToken Session user token
	 * @return true if valid
	 */
	
	public boolean checkUserToken(@WebParam(name="userToken") String userToken) {
		return userToken.length() > 0;	
	}
	
	/**
	 * Logging the user out
	 * @param userToken this is the userToken you get when you call the loggin function
	 * @return true if valid
	 * @throws NotLoggedIn Is thrown when the given userToken can't be found. This mostly happens after a session timeout 
	 */
	
	public void logout(@WebParam(name="userToken") String userToken) throws NotLoggedIn {
		requestUserRights(userToken);
	}
	
	/**
	 * Lists all users or just one special user (requires admin rights)
	 * @param userToken Session user token
	 * @throws NotLoggedIn Is thrown when the given userToken can't be found. This mostly happens after a session timeout 
	 * @throws NoAdminRights Is thrown when the user has no admin rights and therefore can't see this list.
	 * @return Array of UserResponse containing username, fullname and isAdmin flag
	 */
	
	public UserResponse[] getUsers(@WebParam(name="userToken") String userToken) throws NotLoggedIn, NoAdminRights {

		requestAdminRights(userToken);

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

		requestAdminRights(userToken);
		
		if ("".equals(username))
			throw new UserNotFound();

		return new UserResponse("hans", "Hans Meier", true);
	}
	
	/**
	 * Deletes an user (requires admin rights)
	 * @param userToken Session user token
	 * @param username username of the user which should be deleted
	 * @throws NotLoggedIn Is thrown when the given userToken can't be found. This mostly happens after a session timeout 
	 * @throws NoAdminRights Is thrown when the user has no admin rights and therefore can't use this function.
	 * @throws UserNotFound Thrown when username is not found in database
	 */
	
	public void deleteUser(@WebParam(name="userToken") String userToken,@WebParam(name="username")  String username) throws NotLoggedIn, NoAdminRights, UserNotFound {

		requestAdminRights(userToken);
		
		if ("".equals(username))
			throw new UserNotFound();
	}
	
	/**
	 * Change the password for an user (requires admin rights when not changing you own password)
	 * @param userToken Session user token
	 * @param username username of the user which should be deleted
	 * @param password New password
	 * @throws NotLoggedIn Is thrown when the given userToken can't be found. This mostly happens after a session timeout 
	 * @throws NoAdminRights Is thrown when the user has no admin rights and therefore can't use this function.
	 * @throws UserNotFound Thrown when username is not found in database
	 * @throws PasswordTooShort Thrown when the password is too short
	 */
	
	public void changePassword(@WebParam(name="userToken") String userToken,@WebParam(name="username")  String username,@WebParam(name="password")  String password) throws NotLoggedIn, NoAdminRights, UserNotFound, PasswordTooShort {

		if ("hans".equals(username))
			requestUserRights(userToken);
		else
			requestAdminRights(userToken);
		
		if ("".equals(username))
			throw new UserNotFound();
		
		if (password.length()<8)
			throw new PasswordTooShort(8);
	}
	
	/**
	 * Creates a new user account (requires admin rights)
	 * @param userToken Session user token
	 * @param username username of the user which should be added
	 * @param fullname fullname of the user which should be added
	 * @param password Password
	 * @throws NotLoggedIn Is thrown when the given userToken can't be found. This mostly happens after a session timeout 
	 * @throws NoAdminRights Is thrown when the user has no admin rights and therefore can't use this function.
	 * @throws PasswordTooShort Thrown when the password is too short
	 * @throws UsernameTooShortOrInvalid Thrown when username is too short or invalid
	 * @throws UsernameAlreadyInUse Thrown when username is already used by someone else
	 */
	
	public void addUser(@WebParam(name="userToken") String userToken,@WebParam(name="username") String username,@WebParam(name="password") String password) throws NotLoggedIn, NoAdminRights, PasswordTooShort, UsernameTooShortOrInvalid, UsernameAlreadyInUse {

		requestAdminRights(userToken);
		
		if (username.length() < 4)
			throw new UsernameTooShortOrInvalid(4);
		
		if ("hans".equals(username))
			throw new UsernameAlreadyInUse();
		
		if (password.length() < 8)
			throw new PasswordTooShort(8);
	}
	
	/* Blueprints */
	
	public BlueprintResponse[] getBlueprints(@WebParam(name="userToken") String userToken) throws NotLoggedIn {
		requestUserRights(userToken);
		return convertListToResponseArrayBlueprint(blueprintManager.getAllBlueprints());  
	}
	
	public BlueprintResponse getBlueprint(@WebParam(name="userToken") String userToken,@WebParam(name="blueprintId") int blueprintId,@WebParam(name="maxHeight") int maxHeight,@WebParam(name="maxWidth") int maxWidth) throws NotLoggedIn, BlueprintNotFound {
		requestUserRights(userToken);
		return new BlueprintResponse(blueprintManager.getBlueprint(blueprintId, maxHeight, maxWidth));  
	}
	
	public void addBlueprint(@WebParam(name="userToken") String userToken,@WebParam(name="name") String name,@WebParam(name="image") java.awt.Image image) throws NotLoggedIn, NoAdminRights {
		requestAdminRights(userToken); 
		blueprintManager.addBlueprint(name, image);  
	}
	
	public void deleteBlueprint(@WebParam(name="userToken") String userToken,@WebParam(name="blueprintId") int blueprintId) throws NotLoggedIn, NoAdminRights, BlueprintNotFound {
		requestAdminRights(userToken); 
		blueprintManager.deleteBlueprint(blueprintId); 
		
	}
	
	public void renameBlueprint(@WebParam(name="userToken") String userToken,@WebParam(name="blueprintId") int blueprintId, @WebParam(name="name") String name) throws NotLoggedIn, NoAdminRights, BlueprintNotFound {
		requestAdminRights(userToken); 
		blueprintManager.renameBlueprint(blueprintId, name);
	}
	
	public int addBlueprintLink(@WebParam(name="userToken") String userToken,@WebParam(name="blueprintId") int blueprintId, @WebParam(name="linkingBlueprintId")  int linkingBlueprintId,@WebParam(name="x") float x,@WebParam(name="y") float y) throws NotLoggedIn, NoAdminRights, BlueprintNotFound {
		requestAdminRights(userToken); 
		return blueprintManager.addLink(blueprintId, linkingBlueprintId, x, y);
	}
	
	public void setBlueprintIsPrimary(@WebParam(name="userToken") String userToken,@WebParam(name="blueprintId") int blueprintId, @WebParam(name="primary") boolean primary) throws NotLoggedIn, NoAdminRights, BlueprintNotFound {
		requestAdminRights(userToken); 
		blueprintManager.setBlueprintIsPrimary(blueprintId, primary);
	}
	
	/* Nodes */
	
	/**
	 * Returns nodes, nodes have certain parameters defined in NodesResponse.java
	 */
	
	public NodeResponse[] getNodes(@WebParam(name="userToken") String userToken) throws NotLoggedIn {
		requestUserRights(userToken);
		return convertListToResponseArrayNode(nodeManager.getAllNodes());
	}
	
	public NodeResponse[] getNodesByBlueprint(@WebParam(name="userToken") String userToken,@WebParam(name="blueprintId") int blueprintId) throws NotLoggedIn, BlueprintNotFound {
		requestUserRights(userToken);
		return convertListToResponseArrayNodeWithPosition(nodeManager.getAllNodesFilteredByBlueprint(blueprintId)); 
	}
	
	/**
	 * @param userToken Session user token
	 * @param nodeId must be set
	 * @return if nodeId is not found return new NodeResponse
	 * @throws NotLoggedIn NotLoggedIn Is thrown when the given userToken can't be found. This mostly happens after a session timeout 
	 * @throws NodeNotFound Exception thrown when node not found
	 */
	
	public NodeResponse getNode(@WebParam(name="userToken") String userToken,@WebParam(name="nodeId") int nodeId) throws NotLoggedIn, NodeNotFound {
		requestUserRights(userToken);
		
		Node node = nodeManager.getNode(nodeId, true);
		
		if (node == null) 
			throw new NodeNotFound();
		
		return new NodeResponse(node); 
	}
	
	/**
	 * @param userToken Session user token
	 * @return ???
	 * @throws NotLoggedIn NotLoggedIn Is thrown when the given userToken can't be found. This mostly happens after a session timeout
	 */
	
	public NodeResponse[] getUnnamedNodes(@WebParam(name="userToken") String userToken) throws NotLoggedIn {
		requestUserRights(userToken);
		return convertListToResponseArrayNode(nodeManager.getUnnamedNodes());
	}
	
	/**
	 * This method searches the database for tagged Nodes. 
	 * Tags help to identify nodes by grouping them. For example one can add "kitchen" to all nodes in the kitchen 
	 * @param userToken Session user token
	 * @param tag 
	 * @return ??
	 * @throws NotLoggedIn NotLoggedIn Is thrown when the given userToken can't be found. This mostly happens after a session timeout
	 */
	
	public NodeResponse[] getTaggedNodes(@WebParam(name="userToken") String userToken, @WebParam(name="tag") String tag) throws NotLoggedIn {
		requestUserRights(userToken);
		return convertListToResponseArrayNode(nodeManager.getTaggedNodes(tag));
	}
	
	public void renameNode(@WebParam(name="userToken") String userToken, @WebParam(name="nodeId") int nodeId, @WebParam(name="name") String name) throws NotLoggedIn, NoAdminRights, NodeNotFound {
		requestAdminRights(userToken);
		nodeManager.nameNode(nodeId, name);
	}
	/* Snapshot */
	
	public SnapshotResponse getSnapshot(@WebParam(name="userToken") String userToken, @WebParam(name="nodeId") int nodeId) {
		return new SnapshotResponse(nodeManager.getLastSnapshot(nodeId));
	}
	
	/* User defined nodes */
	public NodeResponse[] getUserdefinedNodes(@WebParam(name="userToken") String userToken) throws NotLoggedIn, NoAdminRights {
		requestAdminRights(userToken);
		return convertListToResponseArrayNode(nodeManager.getUserdefinedNodes());
	}
	
	public void positionNodeOnBlueprint(@WebParam(name="userToken") String userToken,@WebParam(name="nodeId") int nodeId,@WebParam(name="blueprintId") int blueprintId,@WebParam(name="x") float x,@WebParam(name="y") float y) throws NotLoggedIn, NoAdminRights, BlueprintNotFound, NodeNotFound {
		requestAdminRights(userToken);
		nodeManager.positionNodeOnBlueprint(nodeId, blueprintId, x, y);			
	}
	
	/**
	 * @param userToken Session user token
	 * @param name name of node
	 * @param category enocean
	 * @param type type like light, heating
	 * @return return name, category, type
	 * @throws NotLoggedIn NotLoggedIn Is thrown when the given userToken can't be found. This mostly happens after a session timeout
	 * @throws NoAdminRights Is thrown when the user has no admin rights and therefore can't use this function
	 */
	
	public int addUserdefinedNode(@WebParam(name="userToken")String userToken,@WebParam(name="name") String name,@WebParam(name="category") String category,@WebParam(name="type") String type) throws NotLoggedIn, NoAdminRights {
		requestAdminRights(userToken);		
		return nodeManager.addUserDefinedNode(name, category, type); 
	}
	
	public String[] getCategories(@WebParam(name="userToken")String userToken) throws NotLoggedIn {
		requestUserRights(userToken);	
		return new String[] {"enocean", "camera"};
	}
	
	public String[] getTypes(@WebParam(name="userToken")String userToken) throws NotLoggedIn {
		requestUserRights(userToken);	
		
		String[] types = new String[statusManager.getTypes().size()];
		int i = 0;
		for (String type: statusManager.getTypes())
			types[i++] = type;
		
		return types;
	}
	
	/* Manage tags */
	
	public boolean addTag(@WebParam(name="userToken")String userToken, @WebParam(name="nodeId")int nodeId, @WebParam(name="tag")String tag) throws NotLoggedIn, NoAdminRights {
		requestAdminRights(userToken);
		return nodeManager.addTag(nodeId, tag);
	}
	
	public boolean deleteTag(@WebParam(name="userToken")String userToken, @WebParam(name="nodeId")int nodeId, @WebParam(name="tag")String tag) throws NotLoggedIn, NoAdminRights {
		requestAdminRights(userToken);
		return nodeManager.deleteTag(nodeId, tag);
	}
	
	/* Manage trigger (requires admin rights) */
	
	public TriggerResponse[] getSenderForReceiverTrigger(@WebParam(name="userToken") String userToken,@WebParam(name="receiverId") int receiverId) throws NotLoggedIn, NoAdminRights {
		requestAdminRights(userToken);
		
		return convertListToResponseArrayTrigger(statusManager.getTriggerManager().getSender(receiverId));
	}
	
	/**
	 * @param userToken Session user token
	 * @param senderId senderId from sendernode
	 * @param receiverId receiverId from receivernode
	 * @param channel empty for no channel, or A, B, C or D
	 * @throws NotLoggedIn NotLoggedIn Is thrown when the given userToken can't be found. This mostly happens after a session timeout
	 * @throws NoAdminRights Is thrown when the user has no admin rights and therefore can't use this function
	 * @throws NodeNotFound node not found for the senderId or receiverId
	 */
	
	public void addSenderToReceiverTrigger(@WebParam(name="userToken") String userToken,@WebParam(name="senderId") int senderId,@WebParam(name="receiverId") int receiverId,@WebParam(name="channel") String channel) throws NotLoggedIn, NoAdminRights, NodeNotFound {
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
	
	/* Set Status */
	
	public NodeResponse[] setStatus(@WebParam(name="userToken") String userToken,@WebParam(name="nodeId") int nodeId, @WebParam(name="key") String key, @WebParam(name="value") String value) throws NotLoggedIn, StatusValueInvalid {
		requestUserRights(userToken);
		try {
			return convertListToResponseArrayNode(statusManager.setStatus(nodeManager.getNode(nodeId, true), key, value));
		} catch (InvalidStatusValue e) {
			throw new StatusValueInvalid();
		}
	}

	/* Camera Interface */

	/**
	 * Get Settings for alarm notification to users (Setting only for admin)
	 * @param userToken Session user token
	 * @throws NotLoggedIn Is thrown when the given userToken can't be found. This mostly happens after a session timeout 
	 * @throws NoAdminRights Is thrown when the user has no admin rights and therefore can't use this function.
	 * @return boolean Is true if alarm notification is set, false if not
	 */

	public boolean getAlarmSetting (@WebParam(name="userToken") String userToken, @WebParam(name="CameraId") int cameraId) throws NotLoggedIn, NodeNotFound, NoAdminRights{
		requestAdminRights(userToken);
		String alarm = null;
		Node node = nodeManager.getNode(cameraId, true);
		if (node == null) 
			throw new NodeNotFound();
		alarm = node.getStatus().get("alarm");
		if(alarm == "1"){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Enables or disables alarm notification to users (Setting only for admin)
	 * @param userToken Session user token
	 * @throws NotLoggedIn Is thrown when the given userToken can't be found. This mostly happens after a session timeout 
	 * @throws NoAdminRights Is thrown when the user has no admin rights and therefore can't use this function.
	 * @return boolean Is true if alarm notification is turned on, false if turned off
	 */

	public boolean setAlarm (@WebParam(name="userToken") String userToken, @WebParam(name="CameraId") int cameraId) throws NotLoggedIn,  NodeNotFound, NoAdminRights{
		requestAdminRights(userToken);
		String alarm = null;
		Node node = nodeManager.getNode(cameraId, true);
		if (node == null) 
			throw new NodeNotFound();
		alarm = node.getStatus().get("alarm");
		if(alarm == "1"){
			alarm = node.getStatus().put("alarm", "0");
			return false;
		}else{
			alarm = node.getStatus().put("alarm", "1");
			return true;
		}
	}
	
	/**
	 * Gives user information on the adresse where to recieve the camera stream from
	 * @param userToken Session user token
	 * @throws NotLoggedIn Is thrown when the given userToken can't be found. This mostly happens after a session timeout 
	 * @return String Returns the URL of the camera stream as a String
	 */
	
	public String getStream(@WebParam(name="userToken") String userToken,@WebParam(name="CameraId") int cameraId) throws NotLoggedIn, NodeNotFound, NoAdminRights{
		requestUserRights(userToken);
		String streamURL = null;
		Node node = nodeManager.getNode(cameraId, true);
		if (node == null) 
			throw new NodeNotFound();
		streamURL = node.getStatus().get("url");
		return streamURL;
	}
}
