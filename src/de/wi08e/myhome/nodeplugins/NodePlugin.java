/**
 * 
 */
package de.wi08e.myhome.nodeplugins;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

import de.wi08e.myhome.communicationplugins.CommunicationEvent;
import de.wi08e.myhome.model.datagram.Datagram;

/**
 * This interface contains the core methods every node plugin must implement
 * @author Marek_Ventur
 */

public interface NodePlugin {
	public void initiate(NodePluginEvent event, Map<String, String> properties, org.w3c.dom.Node data) throws NodePluginException;
	public void chainReceiveDatagram(Datagram datagram);
	public void chainSendDatagramm(Datagram datagram);
	public String getName();
	public String getCategory();
}
