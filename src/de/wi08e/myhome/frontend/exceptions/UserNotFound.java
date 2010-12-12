package de.wi08e.myhome.frontend.exceptions;

public class UserNotFound extends FrontendException {

	/**
	 * This exception is raised when a requested username is not found.
	 */
	private static final long serialVersionUID = 1L;

	public UserNotFound() {
		super(3, "UserNotFound", "The given user can't be found in the database.");
	}

}
