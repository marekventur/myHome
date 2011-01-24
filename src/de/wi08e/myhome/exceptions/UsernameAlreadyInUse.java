package de.wi08e.myhome.exceptions;

/**
 * This exception is thrown when the username is already used by someone else
 * @author Marek
 *
 */
public class UsernameAlreadyInUse extends FrontendException {

	private static final long serialVersionUID = 1L;

	public UsernameAlreadyInUse() {
		super(7, "UsernameAlreadyInUse", "This username is already used by someone else");
	}

}
