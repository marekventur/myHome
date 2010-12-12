package de.wi08e.myhome.frontend.exceptions;


public class NoAdminRights extends FrontendException {
	
	private static final long serialVersionUID = 1L;

	public NoAdminRights() {
		super(2, "NoAdminRights", "This operation requires admin rights, which the given user does not posses");
	}
	
}
