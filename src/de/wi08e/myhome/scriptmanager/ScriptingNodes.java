package de.wi08e.myhome.scriptmanager;

import java.util.ArrayList;
import java.util.List;

import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.nodemanager.NodeManager;
import de.wi08e.myhome.statusmanager.StatusManager;

public class ScriptingNodes {
	
	private NodeManager nodeManager;
	private StatusManager statusManager;
	
	public ScriptingNodes(NodeManager nodeManager, StatusManager statusManager) {
		super();
		this.nodeManager = nodeManager;
		this.statusManager = statusManager;
	}

	private ScriptingNodes(List<ScriptingNode> list, StatusManager statusManager) {
		this.list = list;
		this.statusManager = statusManager;
	}
	
	private List<ScriptingNode> convert(List<Node> nodes) {
		List<ScriptingNode> list = new ArrayList<ScriptingNode>();
		for (Node node: nodes) 
			list.add(new ScriptingNode(node, statusManager));
		return list;
	}
	
	private List<ScriptingNode> list = null;
	
	public ScriptingNode getByName(String name) {
		return new ScriptingNode(nodeManager.getNode(name, true), statusManager);
	}
	
	public ScriptingNodes filterByType(String type) {
		if (list == null) 
			return new ScriptingNodes(convert(nodeManager.getNodesByType(type, true)), statusManager);
		else
		{
			List<ScriptingNode> result = new ArrayList<ScriptingNode>();
			
			for (ScriptingNode scriptingNode: list)
				if (scriptingNode.getType().contentEquals(type)) 
					result.add(scriptingNode);
				
			return new ScriptingNodes(result, statusManager);
		}
	}
	
	
	public ScriptingNodes filterByTag(String tag) {
		if (list == null) 
			return new ScriptingNodes(convert(nodeManager.getNodesByTag(tag, true)), statusManager);
		else
		{
			List<ScriptingNode> result = new ArrayList<ScriptingNode>();
			
			for (ScriptingNode scriptingNode: list)
				if (scriptingNode.getType().contentEquals(tag)) 
					result.add(scriptingNode);
				
			return new ScriptingNodes(result, statusManager);
		}
	}
	
	public int size() {
		return list.size();
	}
	
	public ScriptingNode first() {
		return list.get(0);
	}
	
	public ScriptingNode get(int index) {
		return list.get(index);
	}
}
