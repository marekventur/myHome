package de.wi08e.myhome.scriptmanager;

import de.wi08e.myhome.model.Node;

public class TriggeringEventStatusChange extends TriggeringEvent {
	private Node node;
	private String key;
	private String value;
	
	public TriggeringEventStatusChange(Node node, String key, String value) {
		super();
		this.node = node;
		this.key = key;
		this.value = value;
	}

	public Node getNode() {
		return node;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
	
	
}
