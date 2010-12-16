/**
 * 
 */
package de.wi08e.myhome.nodeplugins;

import de.wi08e.myhome.model.datagram.Datagram;

/**
 * This interface contains the core methods every node plugin must implement
 * 
 * @author Marek
 *
 */
public abstract class NodePlugin {
	
	private NodePluginEvent event;

	public NodePluginEvent getEvent() {
		return event;
	}

	public NodePlugin(NodePluginEvent event) {
		this.event = event;
	}
	
	public Datagram chainReceiveDatagram(Datagram datagram) {
		return datagram;
	}
	
	public Datagram chainSendDatagramm(Datagram datagram) {
		return datagram;
	}

}
