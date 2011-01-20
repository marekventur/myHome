package de.wi08e.myhome.nodeplugins.mielesimulator;

import java.util.Map;

import de.wi08e.myhome.model.datagram.Datagram;
import de.wi08e.myhome.nodeplugins.NodePlugin;
import de.wi08e.myhome.nodeplugins.NodePluginEvent;
import de.wi08e.myhome.nodeplugins.NodePluginException;


public class Main implements NodePlugin 
{

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
		return "Küchengeräte";
	}

	@Override
	public String getName() {
		return "Miele Dampfgarer";
	}

	@Override
	public void initiate(NodePluginEvent event, Map<String, String> properties,
			org.w3c.dom.Node data) throws NodePluginException 
	{
		new GUI();
	}
	
}
