package de.wi08e.myhome.nodeplugins.camera;

/**
 * @author Nico
 *
 */


import java.util.Map;

import org.w3c.dom.Node;

import de.wi08e.myhome.model.datagram.AlarmDatagram;
import de.wi08e.myhome.model.datagram.Datagram;
import de.wi08e.myhome.model.datagram.StreamDatagram;
import de.wi08e.myhome.nodeplugins.NodePlugin;
import de.wi08e.myhome.nodeplugins.NodePluginEvent;
import de.wi08e.myhome.nodeplugins.NodePluginException;


public class Main implements NodePlugin{

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FtpServer server = new FtpServer();
		try {
			server.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void initiate(NodePluginEvent event, Map<String, String> properties,
			org.w3c.dom.Node data) throws NodePluginException {
		// TODO Auto-generated method stub
		FtpServer server = new FtpServer();
		try {
			server.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public void chainReceiveDatagram(Datagram datagram) {
		if (datagram instanceof AlarmDatagram) {
			}				
	}

	@Override
	public void chainSendDatagramm(Datagram datagram) {
		// TODO Auto-generated method stub
		if (datagram instanceof StreamDatagram){
			
		}
	}

	@Override
	public String getName() {
		return "IP Kamera Tuer";
	}

	@Override
	public String getCategory() {
		return "ip-camera";
	}

}
