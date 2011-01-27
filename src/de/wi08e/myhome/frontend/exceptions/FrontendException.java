package de.wi08e.myhome.frontend.exceptions;


/**
 * 
 * @author christoph ebenau
 *
 */
public abstract class FrontendException extends Exception {

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
	}

}
