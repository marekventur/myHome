package de.wi08e.myhome.nodeplugins.mielesimulator;

import java.awt.Image;
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
	private GUI gui;

	@Override
	public void chainReceiveDatagram(Datagram datagram) {

		
	}

	@Override
	public void chainSendDatagramm(Datagram datagram) {
		if (datagram instanceof StatusDatagram) {
			StatusDatagram statusDatagram = (StatusDatagram)datagram;
			Node node = statusDatagram.getNode();
			if(node.equals(this.node))
			{
				if (statusDatagram.getKey().equalsIgnoreCase("power")) {
					statusDatagram.setProcessed(true);
					gui.setPower(statusDatagram.getValue());
				}
				if (statusDatagram.getKey().equalsIgnoreCase("decidedTemperatur")) {
					statusDatagram.setProcessed(true);
					gui.decidedTemperatur(Integer.parseInt(statusDatagram.getValue()));
				}			
			}
		}
	}

	@Override
	public String getCategory() {
		return "mieleathome";
	}

	@Override
	public String getName() {
		return "Miele Dampfgarer";
	}

	@Override
	public void initiate(NodePluginEvent event, Map<String, String> properties,
			org.w3c.dom.Node data) throws NodePluginException 
	{
		this.event=event;
		node=new Node("mieleathome", "simulator", properties.get("node"));
		gui=new GUI(this);
		NodeInformDatagram datagram = new NodeInformDatagram(node);
		event.datagrammReceived(datagram);
	}
	
	public void setStatus(String key, String value)
	{
		event.datagrammReceived(new StatusDatagram(node, key, value));
	}

	@Override
	public Image getLastSnapshot(Node node) {
		return null;
	}
	
}
