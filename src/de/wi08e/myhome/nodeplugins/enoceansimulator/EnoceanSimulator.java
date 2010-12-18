/**
 * 
 */
package de.wi08e.myhome.nodeplugins.enoceansimulator;

import java.util.HashMap;

import org.w3c.dom.Node;

import de.wi08e.myhome.model.datagram.Datagram;
import de.wi08e.myhome.nodeplugins.NodePlugin;
import de.wi08e.myhome.nodeplugins.NodePluginEvent;

/**
 * @author Marek
 *
 */
public class EnoceanSimulator implements NodePlugin {

	/**
	 * @param event
	 * @param properties
	 * @param data
	 */
	public EnoceanSimulator() {
		System.out.println("Hello World");
	}

	@Override
	public void initiate(NodePluginEvent event,	HashMap<String, String> properties, Node data) {
		System.out.println(properties.get("title"));
	}


	@Override
	public Datagram chainReceiveDatagram(Datagram datagram) {
		return null;
	}

	
	@Override
	public Datagram chainSendDatagramm(Datagram datagram) {
		return null;
	}

}
