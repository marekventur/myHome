package de.wi08e.myhome.httpserver;

import javax.xml.ws.Endpoint;

import de.wi08e.myhome.Config;
import de.wi08e.myhome.frontend.FrontendInterface;

public class HTTPServer {
	
	public HTTPServer() {
		
		Endpoint endpoint = Endpoint.create( new FrontendInterface());
		String uri = "http://"+Config.getProperty("webserverHost")+":"+Config.getProperty("webserverPort")+"/service";
		System.out.println(uri+"/service?wsdl");
		endpoint.publish(uri);
	}	
}
