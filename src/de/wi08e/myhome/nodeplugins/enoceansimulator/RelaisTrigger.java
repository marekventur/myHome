/**
 * 
 */
package de.wi08e.myhome.nodeplugins.enoceansimulator;

import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.datagram.RockerSwitchDatagram;

/**
 * @author Marek
 *
 */
public class RelaisTrigger {
	private Node node;
	private RockerSwitchDatagram.Button button;
	
	public RelaisTrigger(Node node, RockerSwitchDatagram.Button button) {
		super();
		this.node = node;
		this.button = button;
	}

	public Node getNode() {
		return node;
	}

	public RockerSwitchDatagram.Button getButton() {
		return button;
	}
}
