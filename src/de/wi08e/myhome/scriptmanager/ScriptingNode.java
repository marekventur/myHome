package de.wi08e.myhome.scriptmanager;

import de.wi08e.myhome.model.NamedNode;
import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.statusmanager.InvalidStatusValueException;
import de.wi08e.myhome.statusmanager.StatusManager;

/**
 * @author Marek
 *
 */
public class ScriptingNode {
	private Node node;
	private StatusManager statusManager;
	
	public ScriptingNode(Node node, StatusManager statusManager) {
		this.node = node;
		this.statusManager = statusManager;
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
	
	public String getIdentifier() {
		return node.toString();
	}
	
	public boolean setStatus(String key, String value) {
		try {
			statusManager.setStatus(node, key, value);
			return true;
		} catch (InvalidStatusValueException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
