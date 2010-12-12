/**
 * 
 */
package de.wi08e.myhome.frontend.exceptions;

/**
 * This exception is raised when the username is wrong, non-existend or does not match the given password
 * @author Marek
 *
 */
public class LoginUsernameOrPasswordWrong extends FrontendException {

	private static final long serialVersionUID = 1L;


	public LoginUsernameOrPasswordWrong() {
		super(4, "LoginUsernameOrPasswordWrong", "The username is wrong, non-existend or does not match the given password");
	}

}
