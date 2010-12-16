package de.wi08e.myhome.model.datagram;

import de.wi08e.myhome.model.Node;


/**
 * @author Marek
 *
 */
public class SMSDatagram extends TargetedDatagram {

	String message;
	
	public SMSDatagram(Node receiver, String message) {
		super(receiver);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
