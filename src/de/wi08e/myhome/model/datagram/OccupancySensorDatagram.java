/**
 * 
 */
package de.wi08e.myhome.model.datagram;

import de.wi08e.myhome.model.Node;

/**
 * @author Marek
 *
 */
public class OccupancySensorDatagram extends BroadcastDatagram {

	private boolean occupied;

	public OccupancySensorDatagram(Node sender, boolean occupied) {
		super(sender);
		this.occupied = occupied;
	}

	public boolean isOccupied() {
		return occupied;
	}

}
