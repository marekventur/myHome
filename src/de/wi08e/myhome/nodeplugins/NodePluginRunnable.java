package de.wi08e.myhome.nodeplugins;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import de.wi08e.myhome.model.datagram.Datagram;
import de.wi08e.myhome.nodeplugins.enoceansimulator.NodePanel;
/**
 * @author Marek_Ventur
 */

public class NodePluginRunnable implements Runnable {
	
	private BlockingQueue<Datagram> outgoingDatagrams = null;
	private BlockingQueue<DatagramQueueHolder> incomingDatagrams = new LinkedBlockingQueue<DatagramQueueHolder>();

	private NodePluginEvent event = new NodePluginEvent() {
		@Override
		public void datagrammReceived(Datagram datagram) {		
			outgoingDatagrams.add(datagram);			
		}			
	};
	
	private NodePlugin nodePlugin;
	
	private boolean running = true;


	public NodePluginRunnable(NodePlugin nodePlugin, Map<String, String> properties, org.w3c.dom.Node data, BlockingQueue<Datagram> outgoingDatagrams) throws NodePluginException {
		this.nodePlugin = nodePlugin;
		this.outgoingDatagrams = outgoingDatagrams;
		nodePlugin.initiate(event, properties, data);
	}
	
	public void chainSendDatagramm(Datagram datagram) {
		incomingDatagrams.add(new DatagramQueueHolder(datagram, DatagramQueueHolder.Type.SEND));
	}
	
	@Override
	public void run() {
		try {
			while (running) {
					DatagramQueueHolder holder = incomingDatagrams.take();
					Datagram datagram = holder.getDatagram();
					
					//  Chain Receive
					if (holder.getType() == DatagramQueueHolder.Type.RECEIVED) 
						nodePlugin.chainReceiveDatagram(datagram);
					
					
					// Chain 
					if (holder.getType() == DatagramQueueHolder.Type.SEND) 
						nodePlugin.chainSendDatagramm(datagram);
					

			}
		} catch (InterruptedException e) {
		}
	}

	public void chainReceiveDatagram(Datagram datagram) {
		incomingDatagrams.add(new DatagramQueueHolder(datagram, DatagramQueueHolder.Type.RECEIVED));
	}
}
