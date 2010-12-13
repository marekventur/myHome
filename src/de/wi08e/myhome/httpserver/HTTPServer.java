package de.wi08e.myhome.httpserver;

import java.util.logging.Logger;

import javax.xml.ws.Endpoint;

import de.wi08e.myhome.Config;
import de.wi08e.myhome.frontend.FrontendInterface;

/**
 * This starts the webserver at the configured port and host (see config.properties)
 */

public class HTTPServer {
	
	private final static Logger LOGGER = Logger.getLogger(HTTPServer.class.getName());
	
	public HTTPServer() {
		Endpoint endpoint = Endpoint.create( new FrontendInterface());
		String uri = "http://"+Config.getProperty("webserverHost")+":"+Config.getProperty("webserverPort")+"/service";
		endpoint.publish(uri);
		LOGGER.info("SOAP server has started. WSDL can be found at "+uri+"?wsdl");
	}	
}
