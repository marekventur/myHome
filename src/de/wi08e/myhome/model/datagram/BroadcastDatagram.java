/**
 * 
 */
package de.wi08e.myhome.model.datagram;

import de.wi08e.myhome.model.Node;

/**
 * This is 
 * @author Marek
 *
 */
public class BroadcastDatagram extends Datagram {
	private Node sender;

	public BroadcastDatagram(Node sender) {
		super();
		this.sender = sender;
	}

	public Node getSender() {
		return sender;
	}
	
	
}
