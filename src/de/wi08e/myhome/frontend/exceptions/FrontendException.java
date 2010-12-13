package de.wi08e.myhome.frontend.exceptions;

import java.util.logging.Logger;

import de.wi08e.myhome.httpserver.HTTPServer;

public abstract class FrontendException extends Exception {

	private final static Logger LOGGER = Logger.getLogger(HTTPServer.class.getName());
	
	private static final long serialVersionUID = 1L;
	private int code;
	private String name;
	
	public String getCode() {
		return String.valueOf(code);
	}
    
	public String getName() {
		return name;
	}
    
	public FrontendException(int code, String name, String details)  {
		super(details);
		this.name = name;
		this.code = code;
		
		LOGGER.info("Frontend Exception thrown: "+name);
	}

}
