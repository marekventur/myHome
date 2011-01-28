package de.wi08e.myhome.nodeplugins.camerasimulator;

import java.awt.Image;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.datagram.Datagram;
import de.wi08e.myhome.model.datagram.NodeInformDatagram;
import de.wi08e.myhome.model.datagram.StartSnapshottingDatagram;
import de.wi08e.myhome.nodeplugins.NodePlugin;
import de.wi08e.myhome.nodeplugins.NodePluginEvent;
import de.wi08e.myhome.nodeplugins.NodePluginException;

public class Main implements NodePlugin {
	
	private class SnapshottingTimer extends TimerTask {
		private int count = 0;
		@Override
		public void run() {
			event.storeImage(node, julia.generate());
			if (count++ == 12) {
				cancel();
				snapshottingTimer = null;
			}	
		}
		
		public void reset() {
			count = 0;
		}
	}
	
	private Timer timer = new Timer();
	private SnapshottingTimer snapshottingTimer = null; 
	private JuliaGenerator julia = new JuliaGenerator();
	
	private Node node;
	private NodePluginEvent event;

	@Override
	public void chainReceiveDatagram(Datagram datagram) {
		
	}

	@Override
	public void chainSendDatagramm(Datagram datagram) {
		
		if (datagram instanceof StartSnapshottingDatagram) {
			
			StartSnapshottingDatagram startSnapshotingDatagram = (StartSnapshottingDatagram)datagram;
			
			if (startSnapshotingDatagram.getReceiver().equals(node)) {
				
				if (snapshottingTimer == null) {
					snapshottingTimer = new SnapshottingTimer();
					timer.schedule(snapshottingTimer, 5000, 10000);
				}	
				else
					snapshottingTimer.reset();
			}
		}
	}

	@Override
	public String getCategory() {
		return "camera";
	}

	@Override
	public Image getLastSnapshot(Node node) {
		if (this.node.equals(node)) {
			// This really should be done in a separated thread...
			return julia.generate();
		}
		return null;
	}

	@Override
	public String getName() {
		return "Fractal Camera Simulator";
	}

	@Override
	public void initiate(NodePluginEvent event, Map<String, String> properties,
			org.w3c.dom.Node data) throws NodePluginException {
		if (!properties.containsKey("id"))
			throw new NodePluginException("camerasimulator", "Parameter 'id' not found!");
		node = new Node(getCategory(), "simulator", properties.get("id"));
		node.setType("simulatorcamera");
		this.event = event;
		if (properties.containsKey("name")) 
			event.datagrammReceived(new NodeInformDatagram(node, properties.get("name")));
		else
			event.datagrammReceived(new NodeInformDatagram(node));
	}
	
}
