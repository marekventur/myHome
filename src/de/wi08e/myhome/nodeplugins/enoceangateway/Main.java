package de.wi08e.myhome.nodeplugins.enoceangateway;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.datagram.BroadcastDatagram;
import de.wi08e.myhome.model.datagram.Datagram;
import de.wi08e.myhome.model.datagram.RockerSwitchDatagram;
import de.wi08e.myhome.nodeplugins.NodePlugin;
import de.wi08e.myhome.nodeplugins.NodePluginEvent;
import de.wi08e.myhome.nodeplugins.NodePluginException;

public class Main implements NodePlugin {

	NetworkRunnable network;
	
	@Override
	public void chainReceiveDatagram(Datagram datagram) {
		// TODO Auto-generated method stub

	}

	@Override
	public void chainSendDatagramm(Datagram datagram) {
		System.out.println("chainSend: "+datagram);
		if (datagram instanceof RockerSwitchDatagram) {
			RockerSwitchDatagram rsd = (RockerSwitchDatagram) datagram;
			if (rsd.getSender().getManufacturer().equals("thermokon")) {
				network.sendTelegram2(rsd.getSender().getHardwareId(), "50000000", "30");
				//network.sendTelegram2(rsd.getSender().getHardwareId(), "00000000", "20");
			}
			
		}

	}

	@Override
	public String getCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image getLastSnapshot(Node node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initiate(NodePluginEvent event, Map<String, String> properties,
			org.w3c.dom.Node data) throws NodePluginException {
		
		network = new NetworkRunnable(properties.get("ip"), Integer.parseInt(properties.get("port")), event);
		new Thread(network).start();
		
	}
	
	

}
