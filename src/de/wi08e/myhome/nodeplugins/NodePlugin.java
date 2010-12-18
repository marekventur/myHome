/**
 * 
 */
package de.wi08e.myhome.nodeplugins;

import java.util.HashMap;

import de.wi08e.myhome.model.datagram.Datagram;

/**
 * This interface contains the core methods every node plugin must implement
 * 
 * @author Marek
 *
 */
public abstract class NodePlugin {
	
	private NodePluginEvent event;
	private org.w3c.dom.Node data;
	private HashMap<String, String> properties;
	
	public NodePluginEvent getEvent() {
		return event;
	}

	public NodePlugin(NodePluginEvent event, HashMap<String, String> properties, org.w3c.dom.Node data) {
		this.event = event;
		this.properties = properties;
		this.data = data;
	}
	
	public Datagram chainReceiveDatagram(Datagram datagram) {
		return datagram;
	}
	
	public Datagram chainSendDatagramm(Datagram datagram) {
		return datagram;
	}

}
