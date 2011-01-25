package de.wi08e.myhome.nodeplugins.camerasimulator;

import java.util.Map;

import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.Snapshot;
import de.wi08e.myhome.model.datagram.Datagram;
import de.wi08e.myhome.nodeplugins.NodePlugin;
import de.wi08e.myhome.nodeplugins.NodePluginEvent;
import de.wi08e.myhome.nodeplugins.NodePluginException;

public class Main implements NodePlugin {
	
	private JuliaGenerator julia = new JuliaGenerator();

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Snapshot getLastSnapshot(Node node) {
		Snapshot snapshot = new Snapshot(julia.generate(), node, "");
		return snapshot;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initiate(NodePluginEvent event, Map<String, String> properties,
			org.w3c.dom.Node data) throws NodePluginException {
		// TODO Auto-generated method stub
		
	}
	
}
