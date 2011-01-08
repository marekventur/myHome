/**
 * 
 */
package de.wi08e.myhome.nodeplugins.notifo;

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
public class Main implements NodePlugin {

	/**
	 * 
	 */
	
	
	
	public Main() {
		// TODO Auto-generated constructor stub
		
	}

	
	@Override
	public void initiate(NodePluginEvent event, Map<String, String> properties,
			Node data) throws NodePluginException {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void chainReceiveDatagram(Datagram datagram) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void chainSendDatagramm(Datagram datagram) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public String getName() {
		return "Notifo Push Messages";
	}

	@Override
	public String getCategory() {
		return "notifo";
	}

}
