/**
 * 
 */
package de.wi08e.myhome.communicationplugins;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import de.wi08e.myhome.Config;
import de.wi08e.myhome.ConfigPlugin;
import de.wi08e.myhome.frontend.httpserver.HTTPServer;
import de.wi08e.myhome.nodeplugins.NodePlugin;
import de.wi08e.myhome.nodeplugins.NodePluginException;
import de.wi08e.myhome.nodeplugins.NodePluginRunnable;
import de.wi08e.myhome.usermanager.UserManager;

/**
 * @author Marek
 *
 */
public class CommunicationManager implements Runnable {

	private final static Logger LOGGER = Logger.getLogger(HTTPServer.class.getName());
	private List<CommunicationPluginRunnable> plugins;
	private UserManager userManager;
	
	private BlockingQueue<Message> receivedMessages = new LinkedBlockingQueue<Message>();
	
	public CommunicationManager(UserManager userManager) {
		super();
		this.userManager = userManager;
		plugins = Collections.synchronizedList(new ArrayList<CommunicationPluginRunnable>());
	}

	@Override
	public void run() {

		/* Loop plugin list */
		List<ConfigPlugin> configPlugins = Config.getCommunicationPlugins();
		
		for (ConfigPlugin configPlugin: configPlugins) {
			
			Class<?> loadedClass;
			try {
				loadedClass = ClassLoader.getSystemClassLoader().loadClass(configPlugin.getNamespace()+".Main");
			
				Constructor<?> cs = loadedClass.getConstructor();
			
				CommunicationPlugin plugin = (CommunicationPlugin)cs.newInstance();
				CommunicationPluginRunnable pluginRunnable = new CommunicationPluginRunnable(plugin, configPlugin.getProperties(), receivedMessages);
				
				Thread pluginThread = new Thread(pluginRunnable);
				pluginThread.start();
				
				plugins.add(pluginRunnable);
				
			} catch (ClassNotFoundException e) {
				LOGGER.warning("Can't find "+configPlugin.getNamespace());
			} catch (SecurityException e) {
				LOGGER.warning("Security exception in "+configPlugin.getNamespace());
			} catch (NoSuchMethodException e) {
				LOGGER.warning("No constructor found in "+configPlugin.getNamespace());
			} catch (java.lang.ClassCastException e) {
				LOGGER.warning("Can't cast "+configPlugin.getNamespace());
			} catch (CommunicationPluginException e) {
				LOGGER.warning(e.getMessage());
			} catch (Exception e) {
				LOGGER.warning("Other exception in "+configPlugin.getNamespace()+": "+e.getMessage());
			} 
		}
		
	}
	
	public void sendMessage(String username, String messageText) {
		Message message = new Message(userManager.getPreferedCommunicationIdentifier(username), messageText);
		for (CommunicationPluginRunnable pluginRunnable: plugins) 
			pluginRunnable.sendMessage(message);	
	}

}
