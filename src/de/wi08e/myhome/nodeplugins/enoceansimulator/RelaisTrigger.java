/**
 * 
 */
package de.wi08e.myhome.nodeplugins.enoceansimulator;

import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.datagram.RockerSwitchDatagram;

/**
 * @author Marek_Ventur
 */
public class RelaisTrigger {
	private Node node;
	private RockerSwitchDatagram.Channel channel;
	
	public RelaisTrigger(Node node, RockerSwitchDatagram.Channel channel) {
		super();
		this.node = node;
		this.channel = channel;
	}

	public Node getNode() {
		return node;
	}

	public RockerSwitchDatagram.Channel getChannel() {
		return channel;
	}
}
