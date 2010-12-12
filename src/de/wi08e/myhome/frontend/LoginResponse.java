package de.wi08e.myhome.frontend;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="loginResponse")
public class LoginResponse {
	public boolean isAdmin;
	public String userToken;	
}
