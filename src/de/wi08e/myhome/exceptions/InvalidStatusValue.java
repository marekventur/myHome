package de.wi08e.myhome.exceptions;

public class InvalidStatusValue extends FrontendException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidStatusValue() {
		super(11, "InvalidStatusValue", "This status change is invalid");
	}

}
