package de.wi08e.myhome.exceptions;


/**
 * 
 * @author christoph ebenau
 *
 */
public class BlueprintNotFound extends FrontendException {
	private static final long serialVersionUID = 1L;

	public BlueprintNotFound() {
		super(8, "BlueprintNotFound", "This blueprint can't be found");
	}
}
