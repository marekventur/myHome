package de.wi08e.myhome.frontend;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="add", namespace="http://userResponse/")
@XmlType(name="add", namespace="http://userResponse/", propOrder={"username", "fullname", "isAdmin"})
public class UserResponse {
	public String username;
	public String fullname;
	public boolean isAdmin = false;
}
