/**
 * 
 */
package de.wi08e.myhome;

import java.util.HashMap;

import org.w3c.dom.Node;

/**
 * @author Marek
 *
 */
public class ConfigPlugin {
	private String namespace;
	private String classname;
	private HashMap<String, String> properties = new HashMap<String, String>();
	private org.w3c.dom.Node xmlNode;
	
	public ConfigPlugin(String namespace, String classname,
			HashMap<String, String> properties, Node xmlNode) {
		super();
		this.namespace = namespace;
		this.classname = classname;
		this.properties = properties;
		this.xmlNode = xmlNode;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getClassname() {
		return classname;
	}

	public HashMap<String, String> getProperties() {
		return properties;
	}

	public org.w3c.dom.Node getXmlNode() {
		return xmlNode;
	}
	
	
}
