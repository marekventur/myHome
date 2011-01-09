package de.wi08e.myhome;

import java.util.logging.Logger;

import de.wi08e.myhome.blueprintmanager.BlueprintManager;
import de.wi08e.myhome.database.Database;
import de.wi08e.myhome.database.MySQLDatabase;
import de.wi08e.myhome.frontend.FrontendInterface;
import de.wi08e.myhome.frontend.httpserver.HTTPServer;
import de.wi08e.myhome.myhomescript.ScriptingEngine;
import de.wi08e.myhome.nodemanager.NodeManager;
import de.wi08e.myhome.nodeplugins.NodePluginRunnable;
import de.wi08e.myhome.statusmanager.StatusManager;

public class Main {
	
	private static ScriptingEngine scriptingEngine;
	private static StatusManager statusManager;
	private static Database database;
	private static NodePluginRunnable nodePlugin;
	private static NodeManager nodeManager;
	private static BlueprintManager blueprintManager;
		
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
		
		/* Create Node Manager */
		nodeManager = new NodeManager(database, nodePlugin);
		
		/* Create scripting engine */
		scriptingEngine = new ScriptingEngine(database, nodeManager);
		
		/* Create statusmanager */
		statusManager = new StatusManager(database, nodeManager, scriptingEngine);
		nodeManager.addReceiver(statusManager);
		
		/* Create Blueprint Manager */
		blueprintManager = new BlueprintManager(database);
		
		/* New FrontendInterface */
		FrontendInterface frontendInterface = new FrontendInterface(nodeManager, statusManager, blueprintManager);
		
		/* Start SOAP service */
		new HTTPServer(frontendInterface);
		LOGGER.info("SOAP service has been started");
		
		/* Bye-bye from here */
		LOGGER.info("Terminating the original user thread");
	}

}
