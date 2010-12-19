/**
 * 
 */
package de.wi08e.myhome;

import java.util.Map;

import org.w3c.dom.Node;

/**
 * @author Marek
 *
 */
public class ConfigPlugin {
	private String namespace;
	private String classname;
	private Map<String, String> properties;
	private org.w3c.dom.Node data;
	
	public ConfigPlugin(String namespace, String classname,
			Map<String, String> properties, Node data) {
		super();
		this.namespace = namespace;
		this.classname = classname;
		this.properties = properties;
		this.data = data;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getClassname() {
		return classname;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public org.w3c.dom.Node getData() {
		return data;
	}
	
	
}
