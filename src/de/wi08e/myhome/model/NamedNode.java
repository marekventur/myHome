/**
 * 
 */
package de.wi08e.myhome.model;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Marek
 *
 */
public class NamedNode extends Node {

	private String name;
	private final Set<String> tags = new HashSet<String>();

	public NamedNode(String type, String manufacturer, String id) {
		super(type, manufacturer, id);
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getTags() {
		return tags;
	}
	
}
