/**
 * 
 */
package de.wi08e.myhome.nodeplugins;

import java.awt.Image;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import de.wi08e.myhome.Config;
import de.wi08e.myhome.ConfigPlugin;
import de.wi08e.myhome.httpserver.HTTPServer;
import de.wi08e.myhome.model.datagram.Datagram;

/**
 * @author Marek
 *
 */
public class NodePluginRunnable implements Runnable {
	
	private final static Logger LOGGER = Logger.getLogger(HTTPServer.class.getName());

	private List<NodePlugin> plugins;
	
	public void run() {
		
		/* Add all the plugins from a specific directory */
		// NodePluginLoader.addFile("nodeplugins/xyz.jar");
		// LOGGER.info("Added plugin xyz.jar");
		
		/* Create thread-safe plugin list (not sure, if needed) */
		plugins = Collections.synchronizedList(new ArrayList<NodePlugin>());
		
		/* Loop plugin list */
		List<ConfigPlugin> configPlugins = Config.getPlugins();
		for (ConfigPlugin configPlugin: configPlugins) {
			
			Class<?> loadedClass;
			try {
				loadedClass = ClassLoader.getSystemClassLoader()
					.loadClass(configPlugin.getNamespace()+"."+configPlugin.getClassname());
			
				Constructor<?> cs = loadedClass.getConstructor();
			
				NodePlugin plugin = (NodePlugin)cs.newInstance();
				LOGGER.info("Created instance named '"+plugin.getName()+"'");
				
				plugin.initiate(new NodePluginEvent() {

					@Override
					public void logInfo(String text) {
						System.out.println(text);
					}

					@Override
					public void logError(String text) {
						System.out.println(text);						
					}

					@Override
					public void datagrammReceived(Datagram datagram) {			
						for (NodePlugin plugin: plugins) 
							plugin.chainReceiveDatagram(datagram);	
					}

					@Override
					public void storeImage(String[] tags, Image image) {
						// TODO Auto-generated method stub
						
					}
					
					}, configPlugin.getProperties(), configPlugin.getData());
					LOGGER.info("Intiated '"+plugin.getName()+"'");
				
				plugins.add(plugin);
			} catch (ClassNotFoundException e) {
				LOGGER.warning("Can't find "+configPlugin.getNamespace()+"."+configPlugin.getClassname());
			} catch (SecurityException e) {
				LOGGER.warning("Security exception in "+configPlugin.getNamespace()+"."+configPlugin.getClassname());
			} catch (NoSuchMethodException e) {
				LOGGER.warning("No constructor found in "+configPlugin.getNamespace()+"."+configPlugin.getClassname());
			} catch (java.lang.ClassCastException e) {
				LOGGER.warning("Can't cast "+configPlugin.getNamespace()+"."+configPlugin.getClassname());
			} catch (NodePluginException e) {
				LOGGER.warning(e.getMessage());
			} catch (Exception e) {
				LOGGER.warning("Other exception in "+configPlugin.getNamespace()+"."+configPlugin.getClassname()+": "+e.getMessage());
			} 
		}
		
		
		
	}
}
