/**
 * 
 */
package de.wi08e.myhome.nodeplugins;

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
import de.wi08e.myhome.model.datagram.Datagram;
import de.wi08e.myhome.nodemanager.NodeManager;

/**
 * @author Marek
 *
 */
public class NodePluginManager implements Runnable {
	
	private final static Logger LOGGER = Logger.getLogger(HTTPServer.class.getName());
	private List<NodePluginRunnable> plugins;
	
	private BlockingQueue<Datagram> receivedDatagrams = new LinkedBlockingQueue<Datagram>();
	
	private NodeManager nodeManager = null;
	private boolean running = true;

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
		/* Create thread-safe plugin list (not sure, if needed) */
		
	}
	
	public NodePluginManager() {
		
		/* Add all the plugins from a specific directory */
		// NodePluginLoader.addFile("nodeplugins/xyz.jar");
		// LOGGER.info("Added plugin xyz.jar");
		plugins = Collections.synchronizedList(new ArrayList<NodePluginRunnable>());
		
		
		/* Loop plugin list */
		List<ConfigPlugin> configPlugins = Config.getNodePlugins();
		for (ConfigPlugin configPlugin: configPlugins) {
			
			Class<?> loadedClass;
			try {
				loadedClass = ClassLoader.getSystemClassLoader().loadClass(configPlugin.getNamespace()+".Main");
			
				Constructor<?> cs = loadedClass.getConstructor();
			
				NodePlugin plugin = (NodePlugin)cs.newInstance();
				NodePluginRunnable pluginRunnable = new NodePluginRunnable(plugin, configPlugin.getProperties(), configPlugin.getData(), receivedDatagrams);
				
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
			} catch (NodePluginException e) {
				LOGGER.warning(e.getMessage());
			} catch (Exception e) {
				LOGGER.warning("Other exception in "+configPlugin.getNamespace()+": "+e.getMessage());
			} 
		}
	}

	public void sendDatagram(Datagram datagram) {

		for (NodePluginRunnable pluginRunnable: plugins) 
			pluginRunnable.chainSendDatagramm(datagram);	

	}

	@Override
	public void run() {
		try {
			while (running) {

				Datagram datagram = receivedDatagrams.take();
				
				
				
				for (NodePluginRunnable pluginRunnable: plugins) 
					pluginRunnable.chainReceiveDatagram(datagram);	
				if (nodeManager != null)
					nodeManager.receiveDatagram(datagram);

			}
		} catch (InterruptedException e) {
		}		
	}

	
}
