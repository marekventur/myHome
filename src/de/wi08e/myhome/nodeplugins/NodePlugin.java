/**
 * 
 */
package de.wi08e.myhome.nodeplugins;

import java.util.Map;

import de.wi08e.myhome.model.datagram.Datagram;

/**
 * This interface contains the core methods every node plugin must implement
 * 
 * @author Marek
 *
 */
public interface NodePlugin {
	public void initiate(NodePluginEvent event, Map<String, String> properties, org.w3c.dom.Node data) throws NodePluginException;
	public void chainReceiveDatagram(Datagram datagram);
	public void chainSendDatagramm(Datagram datagram);
	public String getName();
	public String getCategory();
}
