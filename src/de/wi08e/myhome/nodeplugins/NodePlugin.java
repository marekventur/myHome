package de.wi08e.myhome.nodeplugins;

import java.awt.Image;
import java.util.Map;

import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.datagram.Datagram;

/**
 * This interface contains the core methods every node plugin must implement. 
 * @author Marek_Ventur
 */

public interface NodePlugin {
	
	/**
	 * This method notifies all other plugins when a datagram has been received by a plugin.
	 * This method will run in the context of a private thread. Don't worry about speed...
	 */
	public void chainReceiveDatagram(Datagram datagram);
	
	/**
	 * Is called, when a Datagram should be send. Modify the datagram, when sending was successful.
	 * This method will run in the context of a private thread. Don't worry about speed...
	 */
	public void chainSendDatagramm(Datagram datagram);
	
	/**
	 * Initaiates the plugin.
	 * This method has to return _quickly_, so start a thread for ongoing work 
	 */
	public void initiate(NodePluginEvent event, Map<String, String> properties, org.w3c.dom.Node data) throws NodePluginException;
	
	/**
	 * Returns the last snapshot of a special node
	 * Be careful, because this functions must be threadsafe! Make it fast, another thread might be waiting!
	 * @return Snapshot when node is found and a snapshot has been made, otherwise null 
	 */
	public Image getLastSnapshot(Node node);
	
	/**
	 * Returns a human-readable name for the plugin.
	 * Be careful, because this functions must be threadsafe!
	 */
	public String getName();
	
	/**
	 * Return the category of nodes this plugin will handle
	 * Be careful, because this functions must be threadsafe!
	 */
	public String getCategory();
}
