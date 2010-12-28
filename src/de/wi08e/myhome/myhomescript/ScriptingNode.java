package de.wi08e.myhome.myhomescript;

import de.wi08e.myhome.model.NamedNode;

/**
 * @author Marek
 *
 */
public abstract class ScriptingNode {
	private NamedNode node;
	
	public ScriptingNode(NamedNode node) {
		this.node = node;
	}
	
	public String getName() {
		return node.getName();
	}
	
	public String getCategory() {
		return node.getCategory();
	}
	
	public String getType() {
		return node.getCategory();
	}
	
}
