/**
 * 
 */
package de.wi08e.myhome.nodeplugins.enoceansimulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.datagram.Datagram;
import de.wi08e.myhome.model.datagram.RockerSwitchDatagram;
import de.wi08e.myhome.nodeplugins.NodePlugin;
import de.wi08e.myhome.nodeplugins.NodePluginEvent;
import de.wi08e.myhome.nodeplugins.NodePluginException;

/**
 * @author Marek
 *
 */
public class Main implements NodePlugin {
	
	private final List<NodePanel> nodePanels = new ArrayList<NodePanel>();
	private GUI gui;
	private NodePluginEvent event;

	private String identifier = "";
	
	public Main() {

	}

	@Override
	public void initiate(final NodePluginEvent event, Map<String, String> properties, org.w3c.dom.Node data) throws NodePluginException {
		this.event = event;
		
		/* Is there a title? */
		if (!properties.containsKey("title"))
			throw new NodePluginException("Enocean Simulator", "Property 'title' not found");
		identifier = "Enocean Simulator: "+properties.get("title");
		
		/* Is there data? */
		if (data == null)
			throw new NodePluginException(identifier, "no 'data' tag found");
		
		/* Generate NodePanelEventHandler */
		NodePanelEventHandler nodePanelEventHandler = new NodePanelEventHandler() {
			@Override
			public void receive(Datagram datagram) {
				event.datagrammReceived(datagram);
			}
		};
		
		/* Go through data */
		NodeList configNodes = ((Element)data).getElementsByTagName("node");
		for (int i = 0; i < configNodes.getLength(); i++) {
			org.w3c.dom.Node configNode = configNodes.item(i);
			
			/* Get type */
			if (configNode.getAttributes().getNamedItem("type") == null)
				throw new NodePluginException("Enocean Simulator", "Attribute 'type' for tag 'node' not found");
			String type = configNode.getAttributes().getNamedItem("type").getNodeValue();
			
			/* is there content in this node? */
			if (configNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
				
				/* Get manufacturer, id & title */
				NodeList manufacturerNodes = ((Element) configNode).getElementsByTagName("manufacturer");
				NodeList idNodes = ((Element) configNode).getElementsByTagName("id");
				NodeList titleNodes = ((Element) configNode).getElementsByTagName("title");
				
				String manufacturer = "simulator";
				String id;
				String title;
				
				if (manufacturerNodes.getLength() > 0)
					manufacturer = manufacturerNodes.item(0).getChildNodes().item(0).getNodeValue().toString();
				if ((idNodes.getLength() < 1) || (titleNodes.getLength() < 1))
					throw new NodePluginException("Enocean Simulator", "Tag 'id' an/or 'title' not found for tag 'node'");
				id = idNodes.item(0).getChildNodes().item(0).getNodeValue().toString();
				title = titleNodes.item(0).getChildNodes().item(0).getNodeValue().toString();
				
				/* Create node from that */
				Node node = new Node("enocean", manufacturer, id);
				
				/* TwoRockerSwitch */
				if (type.equalsIgnoreCase("TwoRockerSwitch")) {
					nodePanels.add(new TwoRockerSwitch(title, node, nodePanelEventHandler));
				}
				
				/* Relais */
				if (type.equalsIgnoreCase("Relais")) {
					List<RelaisTrigger> triggers = new ArrayList<RelaisTrigger>();
					
					NodeList triggerNodes = ((Element) configNode).getElementsByTagName("trigger");
					
					for (int j=0; j<triggerNodes.getLength(); j++) {
						
						org.w3c.dom.Node triggerNode = triggerNodes.item(j); 
						if (triggerNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
							
							/* Read trigger from xml */
							manufacturerNodes = ((Element) triggerNode).getElementsByTagName("manufacturer");
							idNodes = ((Element) triggerNode).getElementsByTagName("id");
							NodeList channelNodes = ((Element) triggerNode).getElementsByTagName("channel");
							RockerSwitchDatagram.Channel channel;
							
							if (manufacturerNodes.getLength() > 0)
								manufacturer = manufacturerNodes.item(0).getChildNodes().item(0).getNodeValue().toString();
							if (idNodes.getLength() == 0)
								throw new NodePluginException("Enocean Simulator", "Tag 'id' not found for tag 'trigger'");
							id = idNodes.item(0).getChildNodes().item(0).getNodeValue().toString();
							if (channelNodes.getLength() == 0)
								throw new NodePluginException("Enocean Simulator", "Tag 'channel' not found for tag 'trigger'");
							if (channelNodes.item(0).getChildNodes().item(0).getNodeValue().toString().equalsIgnoreCase("A"))
								channel = RockerSwitchDatagram.Channel.A;
							else
								channel = RockerSwitchDatagram.Channel.B;
							
							
							
							triggers.add(new RelaisTrigger(new Node("enocean", manufacturer, id), channel));
							
							
							
						}
					}
					
					nodePanels.add(new Relais(title, node, nodePanelEventHandler, triggers));
				}
			}
			
		}
		
		gui = new GUI(properties.get("title"), nodePanels);
		

		/* Screenposition */
		int left = 0;
		int top = 0;
		if (properties.containsKey("left")) 
			left = Integer.parseInt(properties.get("left"));
		
		if (properties.containsKey("top"))
			top = Integer.parseInt(properties.get("top"));
		
		gui.setLocation(left, top);

		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		        gui.createAndShowGUI();
		    }
		});

	}


	@Override
	public void chainReceiveDatagram(Datagram datagram) {
		for(NodePanel nodePanel: nodePanels)
			nodePanel.handleDatagram(datagram);	
	}

	
	@Override
	public void chainSendDatagramm(Datagram datagram) {
		for(NodePanel nodePanel: nodePanels)
			nodePanel.handleDatagram(datagram);		
	}


	@Override
	public String getName() {
		return "Enocean Simulator";
	}


	@Override
	public String getCategory() {
		return "enocean";
	}

	

}
