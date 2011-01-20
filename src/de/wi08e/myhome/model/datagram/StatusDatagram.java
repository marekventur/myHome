package de.wi08e.myhome.model.datagram;

import de.wi08e.myhome.model.Node;

public class StatusDatagram extends Datagram {
	private Node node;
	private String key;
	private String value;
	
	public StatusDatagram(Node node, String key, String value) {
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
