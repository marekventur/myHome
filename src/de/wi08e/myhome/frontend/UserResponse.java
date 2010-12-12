package de.wi08e.myhome.frontend;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is a response for FrontendInterface.listUsers and FrontendInterface.getUser. 
 * It encapsulates the username, fullname and the admin flag
 * 
 * @author Marek
 *
 */
@XmlRootElement(name="userResponse")
public class UserResponse {
	public String username = "";
	public String fullname = "";
	public boolean isAdmin = false;
	
	/**
	 * Initiates the object
	 * @param username Username (as used for login)
	 * @param fullname Fullname, which should be used for all end-user texts
	 * @param isAdmin Is true, when the user has admin rights
	 */
	public UserResponse(String username, String fullname, boolean isAdmin) {
		super();
		this.username = username;
		this.fullname = fullname;
		this.isAdmin = isAdmin;
	}
	
	/**
	 * Initiates the object with default values
	 */
	public UserResponse() {	}
	
}
