/**
 * 
 */
package de.wi08e.myhome;

import java.util.Map;

import org.w3c.dom.Node;

/**
 * @author Marek
 */
public class ConfigPlugin {
	private String namespace;
	private Map<String, String> properties;
	private org.w3c.dom.Node data;
	
	public ConfigPlugin(String namespace,
			Map<String, String> properties, Node data) {
		super();
		this.namespace = namespace;
		this.properties = properties;
		this.data = data;
	}

	public String getNamespace() {
		return namespace;
	}


	public Map<String, String> getProperties() {
		return properties;
	}

	public org.w3c.dom.Node getData() {
		return data;
	}
	
	
}
