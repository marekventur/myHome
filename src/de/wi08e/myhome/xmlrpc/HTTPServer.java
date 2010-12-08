package de.wi08e.myhome.xmlrpc;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

import de.wi08e.myhome.Config;

public class HTTPServer {
	
	public HTTPServer() {
		try {
			final Server server = new Server();
	
			final Connector connector = new SelectChannelConnector();
			connector.setPort(Integer.parseInt(Config.getProperty("webserverPort")));
			//final Connector connector = new SslSocketConnector();
			//connector.setPort(8081);
			server.setConnectors(new Connector[] { connector });
	
			final WebAppContext webappcontext = new WebAppContext();
			webappcontext.setContextPath("");
			webappcontext.setWar("httpServer");
	
			server.setHandler(webappcontext);
			server.start();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}	
}
