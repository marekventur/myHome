/**
 * 
 */
package de.wi08e.myhome.nodeplugins;

import java.awt.Image;

import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.datagram.Datagram;

/**
 * @author Marek_Ventur
 */
public interface NodePluginEvent {
	//public void logInfo(String text);
	//public void logError(String text);
	public void datagrammReceived(Datagram datagram);
	public void storeImage(Node node, Image image);
}
