package de.wi08e.myhome.scriptmanager;

import de.wi08e.myhome.model.NamedNode;
import de.wi08e.myhome.model.Node;

/**
 * @author Marek
 *
 */
public class ScriptingNode {
	private Node node;
	
	public ScriptingNode(Node node) {
		this.node = node;
	}
	
	public String getName() {
		if (node instanceof NamedNode)
			return ((NamedNode)node).getName();
		return null;
	}
	
	public String getCategory() {
		return node.getCategory();
	}
	
	public String getType() {
		return node.getCategory();
	}
	
	public String getHardwareId() {
		return node.getHardwareId();
	}
	
	public int getDatabaseId() {
		return node.getDatabaseId();
	}
	
	
	
}
