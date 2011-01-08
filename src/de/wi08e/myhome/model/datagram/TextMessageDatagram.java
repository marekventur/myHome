/**
 * 
 */
package de.wi08e.myhome.model.datagram;

import de.wi08e.myhome.model.Node;

/**
 * Keep your message short, at best under 
 * @author Marek
 *
 */
public final class TextMessageDatagram extends TargetedDatagram {
	private String message;
	
	public TextMessageDatagram(Node receiver, String message) {
		super(receiver);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
