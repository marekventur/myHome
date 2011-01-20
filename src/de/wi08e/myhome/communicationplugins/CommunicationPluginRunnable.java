/**
 * 
 */
package de.wi08e.myhome.communicationplugins;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Marek
 *
 */
public class CommunicationPluginRunnable implements Runnable {

	private BlockingQueue<Message> outgoingMessages = null;
	private BlockingQueue<Message> incomingMessages = new LinkedBlockingQueue<Message>();

	private CommunicationEvent event = new CommunicationEvent() {
		@Override
		public void MessageReceived(Message message) {
			outgoingMessages.add(message); 			
		}				
	};
	
	private CommunicationPlugin communicationPlugin;
	
	private boolean running = true;


	public CommunicationPluginRunnable(CommunicationPlugin communicationPlugin, Map<String, String> properties, BlockingQueue<Message> outgoingMessages) throws CommunicationPluginException {
		this.communicationPlugin = communicationPlugin;
		this.outgoingMessages = outgoingMessages;
		communicationPlugin.initiate(event, properties);
	}
	
	public void sendMessage(Message message) {
		incomingMessages.add(message);
	}
	
	@Override
	public void run() {
		
		try {
			while (running) {
				Message message = incomingMessages.take();
				
				communicationPlugin.sendMessage(message);
			}
		} catch (InterruptedException e) {
		}
	}
}
