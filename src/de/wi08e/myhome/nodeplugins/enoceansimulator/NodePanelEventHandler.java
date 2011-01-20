/**
 * 
 */
package de.wi08e.myhome.nodeplugins.enoceansimulator;

import de.wi08e.myhome.model.datagram.Datagram;

/**
 * @author Marek_Ventur
 */
public interface NodePanelEventHandler {
	public void receive(Datagram datagram);
}
