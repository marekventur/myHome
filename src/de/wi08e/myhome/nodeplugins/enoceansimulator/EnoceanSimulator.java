/**
 * 
 */
package de.wi08e.myhome.nodeplugins.enoceansimulator;

import java.util.Map;

import org.w3c.dom.Node;

import de.wi08e.myhome.model.datagram.Datagram;
import de.wi08e.myhome.nodeplugins.NodePlugin;
import de.wi08e.myhome.nodeplugins.NodePluginEvent;
import de.wi08e.myhome.nodeplugins.NodePluginException;

/**
 * @author Marek
 *
 */
public class EnoceanSimulator implements NodePlugin {

	private String identifier = "";
	
	/**
	 * @param event
	 * @param properties
	 * @param data
	 */
	public EnoceanSimulator() {
		System.out.println("Hello World");
		
	}

	@Override
	public void initiate(NodePluginEvent event,	Map<String, String> properties, Node data) throws NodePluginException {
		/* Is there a title? */
		if (!properties.containsKey("title"))
			throw new NodePluginException("Enocean Simulator", "Property 'title' not found");
		identifier = "Enocean Simulator: "+properties.get("title");
		
		/* Is there data? */
		if (data == null)
			throw new NodePluginException(identifier, "no 'data' tag found");

		new GUI(properties.get("title"), data);
	}


	@Override
	public Datagram chainReceiveDatagram(Datagram datagram) {
		return null;
	}

	
	@Override
	public Datagram chainSendDatagramm(Datagram datagram) {
		return null;
	}


	@Override
	public String getName() {
		return "Enocean Simulator";
	}

	

}
