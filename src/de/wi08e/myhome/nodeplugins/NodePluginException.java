/**
 * 
 */
package de.wi08e.myhome.nodeplugins;

/**
 * @author Marek_Ventur
 */
public class NodePluginException extends Exception {

	private static final long serialVersionUID = 1L;
	private String identifier;
	
	public NodePluginException(String identifier, String text) {
	 	super(text);
	 	this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
		
	}
	
	public String getMessage() {
		return getIdentifier()+": "+super.getMessage();
	}
	
}
