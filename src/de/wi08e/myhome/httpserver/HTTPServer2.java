package de.wi08e.myhome.httpserver;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.ws.Endpoint;

@WebService
public class HTTPServer2 {
	@SOAPBinding(style = Style.RPC)
	public String getQuote(String category) {
		if (category.equals("fun")) {
			return "5 is a sufficient approximation of infinity.";
		}
		if (category.equals("work")) {
			return "Remember to enjoy life, even during difficult situatons.";
		} else {
			return "Becoming a master is relatively easily. Do something well and then continue to do it for the next 20 years";
		}
	}

	public static void main(String[] args) {
		Endpoint endpoint = Endpoint.create(new HTTPServer2());
		endpoint.publish("http://localhost:9191/wisequotes");
		
	}
}
