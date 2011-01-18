/**
 * 
 */
package de.wi08e.myhome.communicationplugins;

import java.util.Map;

/**
 * @author Marek
 *
 */
public interface CommunicationPlugin {
	public void initiate(CommunicationEvent event, Map<String, String> properties) throws CommunicationPluginException;
	public void sendMessage(Message message);
	public String getType();
}
