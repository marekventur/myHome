package de.wi08e.myhome.frontend;

import javax.jws.WebService;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name="add", namespace="http://loginResponse/")
@XmlType(name="add", namespace="http://loginResponse/", propOrder={"isAdmin", "userToken"})
public class LoginResponse {
	public boolean isAdmin;
	public String userToken;	
}
