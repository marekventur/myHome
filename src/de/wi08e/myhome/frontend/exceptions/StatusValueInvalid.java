package de.wi08e.myhome.frontend.exceptions;
/**
 * 
 * @author christoph ebenau
 *
 */
public class StatusValueInvalid extends FrontendException {

	private static final long serialVersionUID = 1L;

	public StatusValueInvalid() {
		super(10, "StatusValueInvalid", "The given value can't be set for this status key.");
	}

}
