package de.wi08e.myhome.nodeplugins;

import java.awt.Image;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.Snapshot;
import de.wi08e.myhome.model.datagram.Datagram;
/**
 * @author Marek_Ventur
 */

public class NodePluginRunnable implements Runnable {
	
	private BlockingQueue<MessageFromPluginQueueHolder> outgoingMessages = null;
	private BlockingQueue<MessageToPluginQueueHolder> incomingDatagrams = new LinkedBlockingQueue<MessageToPluginQueueHolder>();

	private NodePluginEvent event = new NodePluginEvent() {
		@Override
		public void datagrammReceived(Datagram datagram) {		
			outgoingMessages.add(new MessageFromPluginQueueHolder(datagram));			
		}

		@Override
		public void storeImage(Node node, Image image) {
			outgoingMessages.add(new MessageFromPluginQueueHolder(new Snapshot(image, node, "")));	
		}			
	};
	
	private NodePlugin nodePlugin;

	
	private boolean running = true;


	public NodePluginRunnable(NodePlugin nodePlugin, Map<String, String> properties, org.w3c.dom.Node data, BlockingQueue<MessageFromPluginQueueHolder> outgoingMessages) throws NodePluginException {
		this.nodePlugin = nodePlugin;
		this.outgoingMessages = outgoingMessages;
		nodePlugin.initiate(event, properties, data);
	}
	
	public void chainSendDatagramm(Datagram datagram) {
		
		incomingDatagrams.add(new MessageToPluginQueueHolder(datagram, MessageToPluginQueueHolder.Type.SEND));
	}
	
	@Override
	public void run() {
		try {
			while (running) {
					MessageToPluginQueueHolder holder = incomingDatagrams.take();
					Datagram datagram = holder.getDatagram();
					
					//  Chain Receive
					if (holder.getType() == MessageToPluginQueueHolder.Type.RECEIVED) 
						nodePlugin.chainReceiveDatagram(datagram);
					
					
					// Chain 
					if (holder.getType() == MessageToPluginQueueHolder.Type.SEND) 
						nodePlugin.chainSendDatagramm(datagram);
					

			}
		} catch (InterruptedException e) {
		}
	}

	public void chainReceiveDatagram(Datagram datagram) {
		incomingDatagrams.add(new MessageToPluginQueueHolder(datagram, MessageToPluginQueueHolder.Type.RECEIVED));
	}
	
	public Image getLastSnapshot(Node node) {
		return nodePlugin.getLastSnapshot(node);
	}
}
