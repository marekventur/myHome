package de.wi08e.myhome.frontend.exceptions;



public class FrontendException extends Exception {

	public FrontendException(int code, String text) {
		super(text);
	}

}
