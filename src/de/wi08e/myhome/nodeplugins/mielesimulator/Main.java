package de.wi08e.myhome.nodeplugins.mielesimulator;

import java.util.Map;
import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.datagram.Datagram;
import de.wi08e.myhome.model.datagram.NodeInformDatagram;
import de.wi08e.myhome.model.datagram.StatusDatagram;
import de.wi08e.myhome.nodeplugins.NodePlugin;
import de.wi08e.myhome.nodeplugins.NodePluginEvent;
import de.wi08e.myhome.nodeplugins.NodePluginException;


public class Main implements NodePlugin 
{

	private NodePluginEvent event;
	private Node node;

	@Override
	public void chainReceiveDatagram(Datagram datagram) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void chainSendDatagramm(Datagram datagram) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCategory() {
		return "Küchengeräte";
	}

	@Override
	public String getName() {
		return "Miele Dampfgarer";
	}

	@Override
	public void initiate(NodePluginEvent event, Map<String, String> properties,
			org.w3c.dom.Node data) throws NodePluginException 
	{
		try {
		this.event=event;
		node=new Node(properties.get("node"));
		new GUI(this);
		NodeInformDatagram datagram = new NodeInformDatagram(node);
		event.datagrammReceived(datagram);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setStatus(String key, String value)
	{
		event.datagrammReceived(new StatusDatagram(node, key, value));
	}
	
}
