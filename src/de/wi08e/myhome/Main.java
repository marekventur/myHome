package de.wi08e.myhome;

import de.wi08e.myhome.httpserver.HTTPServer;

public class Main {
	
	public static void main(String[] args) {
		TextFileLogger.setup();
		new HTTPServer();

	}

}
