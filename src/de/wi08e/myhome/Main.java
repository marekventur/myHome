package de.wi08e.myhome;

import java.util.logging.Logger;

import de.wi08e.myhome.database.Database;
import de.wi08e.myhome.database.MySQLDatabase;
import de.wi08e.myhome.frontend.FrontendInterface;
import de.wi08e.myhome.httpserver.HTTPServer;
import de.wi08e.myhome.myhomescript.ScriptingEngine;
import de.wi08e.myhome.nodeplugins.NodePluginRunnable;
import de.wi08e.myhome.statusmanager.StatusManager;

public class Main {
	
	private static ScriptingEngine scriptingEngine;
	private static StatusManager statusManager;
	private static Database database;
	private static NodePluginRunnable nodePlugin;
		
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
		nodePlugin = new NodePluginRunnable();
		Thread nodePluginThread = new Thread(nodePlugin);
		nodePluginThread.start();
		LOGGER.info("NodePlugins have been loaded");
		
		/* Create Database-Connection */
		database = new MySQLDatabase(Config.getDatabaseHost(), Config.getDatabasePort(), Config.getDatabaseName(), Config.getDatabaseUser(), Config.getDatabasePassword());
		
		/* Create scripting engine */
		scriptingEngine = new ScriptingEngine(database, nodePlugin);
		
		/* Create statusmanager */
		statusManager = new StatusManager(database, nodePlugin, scriptingEngine);
		nodePlugin.addReceiver(statusManager);
		
		/* New FrontendInterface */
		FrontendInterface frontendInterface = new FrontendInterface(statusManager);
		
		/* Start SOAP service */
		new HTTPServer(frontendInterface);
		LOGGER.info("SOAP service has been started");
		
		/* Bye-bye from here */
		LOGGER.info("Terminating the original user thread");
	}

}
