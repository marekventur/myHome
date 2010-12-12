package de.wi08e.myhome.frontend.exceptions;

/**
 * This exception is raised when a requested username is not found.
 */

public class UserNotFound extends FrontendException {

	private static final long serialVersionUID = 1L;

	public UserNotFound() {
		super(3, "UserNotFound", "The given user can't be found in the database.");
	}

}
