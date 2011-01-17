/**
 * 
 */
package de.wi08e.myhome.communicationplugins.notifo;

import java.util.Map;
import de.wi08e.myhome.communicationplugins.CommunicationEvent;
import de.wi08e.myhome.communicationplugins.CommunicationPlugin;
import de.wi08e.myhome.communicationplugins.CommunicationPluginException;
import de.wi08e.myhome.communicationplugins.Message;


/**
 * @author Marek
 *
 */
public class Main implements CommunicationPlugin {

	private NotifoHTTPClient client;
	private String title = "";
	
	public Main() {
		
	}

	@Override
	public void initiate(CommunicationEvent event, Map<String, String> properties) throws CommunicationPluginException {
		client = new NotifoHTTPClient(properties.get("username"), properties.get("apisecret"));
		if (properties.containsKey("title")) 
			title = properties.get("title");
	}
	

	@Override
	public void sendMessage(Message message) {
		if (message.getType().equals("notifo"))
			client.sendNotification(message.getAddress(), title, message.getMessage(), "");
	}

	@Override
	public String getType() {
		return "notifo";
	}

}
