package de.wi08e.myhome.exceptions;

/**
 * This exception is thrown when a password is too short
 * @author Marek
 *
 */
public class PasswordTooShort extends FrontendException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param minLength Minimal length for a valid password 
	 */
	public PasswordTooShort(int minLength) {
		super(5, "PasswordTooShort", "The given password is too short (Should be at least "+String.valueOf(minLength)+" symbols long)");
	}

}
