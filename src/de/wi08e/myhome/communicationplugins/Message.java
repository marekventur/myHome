/**
 * 
 */
package de.wi08e.myhome.communicationplugins;

import java.security.InvalidParameterException;

/**
 * @author Marek
 *
 */
public class Message {
	private String adress;
	private String password;
	private String type;
	private String message;
	
	public Message(String adress, String password, String type, String message) {
		super();
		this.adress = adress;
		this.password = password;
		this.type = type;
		this.message = message;
	}
	
	public Message(String userIdentifier, String message) {
		super();
		String[] userList = userIdentifier.split(":");
		if (userList.length!=3)
			throw new InvalidParameterException("Invalid user-identifier. Has to be \"adress:password:type\"");
		this.adress = userList[0];
		this.password = userList[1];
		this.type = userList[2];
		this.message = message;
	}

	public String getAddress() {
		return adress;
	}

	public String getPassword() {
		return password;
	}

	public String getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}
	
	
}
