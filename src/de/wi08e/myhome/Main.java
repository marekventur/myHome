package de.wi08e.myhome;

import java.util.logging.Logger;

import de.wi08e.myhome.blueprintmanager.BlueprintManager;
import de.wi08e.myhome.communicationplugins.CommunicationManager;
import de.wi08e.myhome.database.Database;
import de.wi08e.myhome.database.MySQLDatabase;
import de.wi08e.myhome.frontend.FrontendInterface;
import de.wi08e.myhome.frontend.httpserver.HTTPServer;
import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.datagram.TextMessageDatagram;
import de.wi08e.myhome.nodemanager.NodeManager;
import de.wi08e.myhome.nodeplugins.NodePluginManager;
import de.wi08e.myhome.scriptmanager.ScriptManager;
import de.wi08e.myhome.statusmanager.StatusManager;
import de.wi08e.myhome.usermanager.UserManager;

public class Main {
	
	private static ScriptManager scriptManager;
	private static StatusManager statusManager;
	private static Database database;
	private static NodePluginManager nodePluginManager;
	private static NodeManager nodeManager;
	private static BlueprintManager blueprintManager;
	private static UserManager userManager;
	private static CommunicationManager communicationManager;
		
	/**
	 * This mtehod starts everything!
	 * @param args
	 */
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
		
		/* Loading all NodePlugins */
		nodePluginManager = new NodePluginManager();
		new Thread(nodePluginManager).start();
		
		/* Create Database-Connection */
		database = new MySQLDatabase(Config.getDatabaseHost(), Config.getDatabasePort(), Config.getDatabaseName(), Config.getDatabaseUser(), Config.getDatabasePassword());
		
		/* Create Usermanager */
		userManager = new UserManager(database);
		
		/* Create CommunicationManager */
		communicationManager = new CommunicationManager(userManager);
		new Thread(communicationManager).start();
		
		
		/* Create Node Manager */
		nodeManager = new NodeManager(database, nodePluginManager);
		
		/* Create scripting engine */
		scriptManager = new ScriptManager(database, nodeManager, userManager, nodePluginManager, statusManager);
		nodeManager.addReceiver(scriptManager);
		new Thread(scriptManager).start();
		
		/* Create statusmanager */
		statusManager = new StatusManager(database, nodeManager, scriptManager);
		nodeManager.addReceiver(statusManager);
		
		/* Create Blueprint Manager */
		blueprintManager = new BlueprintManager(database);		
		
		/* New FrontendInterface */
		FrontendInterface frontendInterface = new FrontendInterface(nodeManager, statusManager, blueprintManager, userManager);
		
		/* Start SOAP service */
		new HTTPServer(frontendInterface);

		/* Bye-bye from here */
		LOGGER.info("Terminating the original user thread");
	}

}
