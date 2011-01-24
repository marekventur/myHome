/**
 * 
 */
package de.wi08e.myhome.nodeplugins.enoceansimulator;

import java.awt.LayoutManager;

import javax.swing.JPanel;

import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.datagram.Datagram;

/**
 * @author Marek_Ventur
 */
public abstract class NodePanel extends JPanel {
	
	private Node node;
	private NodePanelEventHandler eventHandler; 
	
	public NodePanel(LayoutManager layout, String title, Node node, NodePanelEventHandler eventHandler) {
		super(layout);
		this.node = node;
		this.eventHandler = eventHandler;
	}
	
	public Node getNode() {	
		return node;
	}
	
	public NodePanelEventHandler getEventHandler() {
		return eventHandler;
	}

	/**
	 * Should be implemented when Panel is an actor of some kind
	 * @param datagram
	 */
	public void handleDatagram(Datagram datagram) { }
}
