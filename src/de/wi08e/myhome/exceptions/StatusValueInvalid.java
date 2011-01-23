package de.wi08e.myhome.exceptions;

public class StatusValueInvalid extends FrontendException {

	private static final long serialVersionUID = 1L;

	public StatusValueInvalid() {
		super(10, "StatusValueInvalid", "The given value can't be set for this status key.");
	}

}
