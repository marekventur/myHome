/**
 * 
 */
package de.wi08e.myhome.nodeplugins.german;

import de.wi08e.myhome.nodeplugins.NodePlugin;

/**
 * @author Marek
 *
 */
public class German implements NodePlugin {

	/* (non-Javadoc)
	 * @see de.wi08e.myhome.nodeplugins.NodePluginInterface#greet(java.lang.String)
	 */
	public German(String init) {
		System.out.println(init);
	}
	
	public German() {
		System.out.println("init");
	}
	
	@Override
	public String greet(String name) {
		return "Hallo2 "+name;
	}

}
