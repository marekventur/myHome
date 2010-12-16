/**
 * 
 */
package de.wi08e.myhome.model.datagram;

import de.wi08e.myhome.model.Node;

/**
 * @author Marek
 *
 */
public class TargetedDatagram extends Datagram {

	private Node receiver;
	
	public TargetedDatagram(Node receiver) {
		super();
		this.receiver = receiver;
	}

	public Node getReceiver() {
		return receiver;
	}

}
