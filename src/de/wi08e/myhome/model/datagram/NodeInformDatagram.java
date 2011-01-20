package de.wi08e.myhome.model.datagram;

import de.wi08e.myhome.model.Node;

public class NodeInformDatagram extends Datagram {
	private Node node;
	private String name = null;
	
	public NodeInformDatagram(Node node, String name) {
		super();
		this.node = node;
		this.name = name;
	}

	public NodeInformDatagram(Node node) {
		super();
		this.node = node;
	}

	public Node getNode() {
		return node;
	}

	public String getName() {
		return name;
	}
	
	
	
	
}
