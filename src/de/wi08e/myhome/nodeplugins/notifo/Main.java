/**
 * 
 */
package de.wi08e.myhome.nodeplugins.notifo;

import java.util.Map;

import org.w3c.dom.Node;

import de.wi08e.myhome.model.datagram.Datagram;
import de.wi08e.myhome.model.datagram.TextMessageDatagram;
import de.wi08e.myhome.nodeplugins.NodePlugin;
import de.wi08e.myhome.nodeplugins.NodePluginEvent;
import de.wi08e.myhome.nodeplugins.NodePluginException;

/**
 * @author Marek
 *
 */
public class Main implements NodePlugin {

	private NotifoHTTPClient client;
	
	public Main() {
		
	}

	@Override
	public void initiate(NodePluginEvent event, Map<String, String> properties,
			Node data) throws NodePluginException {
		client = new NotifoHTTPClient(properties.get("username"), properties.get("apisecret"));
	}
	
	@Override
	public void chainReceiveDatagram(Datagram datagram) {

	}

	@Override
	public void chainSendDatagramm(Datagram datagram) {
		if (datagram instanceof TextMessageDatagram) {
			TextMessageDatagram textMessageDatagram = (TextMessageDatagram)datagram;
			client.sendNotification(textMessageDatagram.getReceiver().getHardwareId(), "", textMessageDatagram.getMessage(), "");
			datagram.setProcessed(true);
		}
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
