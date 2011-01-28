/**
 * 
 */
package de.wi08e.myhome.model.datagram;

import de.wi08e.myhome.model.Node;

/**
 * @author Marek
 *
 */
public class StartSnapshottingDatagram extends TargetedDatagram {

	/**
	 * @param receiver
	 */
	public StartSnapshottingDatagram(Node receiver) {
		super(receiver);
	}

}
