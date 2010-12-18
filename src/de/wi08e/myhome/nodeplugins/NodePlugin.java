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
public interface NodePlugin {
	public void initiate(NodePluginEvent event, HashMap<String, String> properties, org.w3c.dom.Node data);
	public Datagram chainReceiveDatagram(Datagram datagram);
	public Datagram chainSendDatagramm(Datagram datagram);
}
