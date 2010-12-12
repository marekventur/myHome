package de.wi08e.myhome.frontend.exceptions;

/**
 * This exception is thrown when an username is too short
 * @author Marek
 *
 */
public class UsernameTooShortOrInvalid extends FrontendException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param minLength Minimal length for a valid username 
	 */
	public UsernameTooShortOrInvalid(int minLength) {
		super(6, "UsernameTooShort", "The given username is too short (Should be at least "+String.valueOf(minLength)+" symbols long) or invalid");
	}

}
