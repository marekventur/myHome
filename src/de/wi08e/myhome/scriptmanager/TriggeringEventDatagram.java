/**
 * 
 */
package de.wi08e.myhome.scriptmanager;

import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.datagram.Datagram;

/**
 * @author Marek
 *
 */
public class TriggeringEventDatagram extends TriggeringEvent {
	public Datagram datagram;

	public TriggeringEventDatagram(Datagram datagram) {
		super();
		this.datagram = datagram;
	}

	public Datagram getDatagram() {
		return datagram;
	}
	
}
