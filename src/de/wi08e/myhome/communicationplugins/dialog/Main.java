package de.wi08e.myhome.communicationplugins.dialog;

import java.util.Map;

import javax.swing.JOptionPane;

import de.wi08e.myhome.communicationplugins.CommunicationEvent;
import de.wi08e.myhome.communicationplugins.CommunicationPlugin;
import de.wi08e.myhome.communicationplugins.CommunicationPluginException;
import de.wi08e.myhome.communicationplugins.Message;

public class Main implements CommunicationPlugin {

	@Override
	public String getType() {
		return "dialog";
	}

	@Override
	public void initiate(CommunicationEvent event,	Map<String, String> properties) throws CommunicationPluginException {
		
	}

	@Override
	public void sendMessage(Message message) {
		if (message.getType().contentEquals("dialog")) 
			JOptionPane.showMessageDialog(null, message.getMessage());

	}

}
