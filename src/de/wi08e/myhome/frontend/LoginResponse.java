package de.wi08e.myhome.frontend;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="loginResponse")
public class LoginResponse {
	
	
	private boolean admin;
	private String userToken;
	
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	
	public LoginResponse() {}
	
	public LoginResponse(boolean admin, String userToken) {
		super();
		this.admin = admin;
		this.userToken = userToken;
	}
	
	
}
