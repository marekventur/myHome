package de.wi08e.myhome;

import java.util.logging.Logger;

import de.wi08e.myhome.httpserver.HTTPServer;
import de.wi08e.myhome.nodeplugins.NodePluginRunnable;

public class Main {
		
	public static void main(String[] args) {
		
		/* Loading config.xml */
		try {
			Config.initiate("config.xml");
		} catch (Exception e) {
			System.err.println("An error occured during reading the config file:");
			e.printStackTrace();
			System.exit(0);
		}
 
		/* Initiate Logger */
		TextFileLogger.setup();
		
		/* Get Logger for this class */
		final Logger LOGGER = Logger.getLogger(HTTPServer.class.getName());
		LOGGER.info("myHome has been launched");
		
		/* Loading all NodePlugins */
		NodePluginRunnable nodePluginRunnable = new NodePluginRunnable();
		Thread nodePluginThread = new Thread(nodePluginRunnable);
		nodePluginThread.start();
		LOGGER.info("NodePlugins have been loaded");
		
		/* Start SOAP service */
		new HTTPServer();
		LOGGER.info("SOAP service has been started");
		
		/* Bye-bye from here */
		LOGGER.info("Terminating the original user thread");
	}

}
