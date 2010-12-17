package de.wi08e.myhome;

import de.wi08e.myhome.httpserver.HTTPServer;

public class Main {
	
	public static void main(String[] args) {
		
		try {
			Config.initiate("config.xml");
		} catch (Exception e) {
			System.err.println("An error occured during reading the config file:");
			e.printStackTrace();
			System.exit(0);
		}
 
		
		TextFileLogger.setup();
		new HTTPServer();

	}

}
