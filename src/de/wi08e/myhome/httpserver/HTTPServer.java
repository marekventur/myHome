package de.wi08e.myhome.httpserver;

import javax.xml.ws.Endpoint;

import de.wi08e.myhome.Config;
import de.wi08e.myhome.frontend.FrontendInterface;

public class HTTPServer {
	
	public HTTPServer() {
		
		Endpoint endpointLogin = Endpoint.create( new FrontendInterface());
		System.out.println("http://"+Config.getProperty("webserverHost")+":"+Config.getProperty("webserverPort")+"/service?wsdl");
		endpointLogin.publish("http://"+Config.getProperty("webserverHost")+":"+Config.getProperty("webserverPort")+"/service");
		
	}	
}
