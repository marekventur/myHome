package de.wi08e.myhome.frontend;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Thilo_Gerheim
 */

@XmlRootElement(name="nodeStatusResponse")
public class NodeStatusResponse {
	public String key = "";
	public String value = "";
	
	public NodeStatusResponse() {
		
	}
	
	public NodeStatusResponse(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}
	
}
