package de.wi08e.myhome.frontend.exceptions;


public class NotLoggedIn extends FrontendException {
	
	private static final long serialVersionUID = 1L;

	public NotLoggedIn() {
		super(1, "NotLoggedIn", "The given userToken is not valid. It might has expired.");
	}
	
}
