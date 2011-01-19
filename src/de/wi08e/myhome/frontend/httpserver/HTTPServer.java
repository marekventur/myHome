package de.wi08e.myhome.frontend.httpserver;

import java.util.Map;
import java.util.logging.Logger;
//import com.sun.net.httpserver.HttpsServer;

import javax.xml.ws.Endpoint;

import de.wi08e.myhome.Config;
import de.wi08e.myhome.ConfigSOAPInterface;
import de.wi08e.myhome.frontend.FrontendInterface;

/**
 * This starts the webserver at the configured port and host (see config.properties)
 */

public class HTTPServer {
	
	private final static Logger LOGGER = Logger.getLogger(HTTPServer.class.getName());
	
	public HTTPServer(FrontendInterface frontendInterface) {
		
		/* Only the first one will be used at the moment */
		ConfigSOAPInterface config = Config.getSoapInterfaces().get(0); 
		
		Endpoint endpoint = Endpoint.create(frontendInterface);
		String uri = "http://"+config.getHost()+":"+config.getPort()+config.getPath();
		endpoint.publish(uri);
		
		LOGGER.info("SOAP server has started. WSDL can be found at "+uri+"?wsdl");
	}	
}
