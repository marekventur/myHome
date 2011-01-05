package de.wi08e.myhome.statusmanager;

import de.wi08e.myhome.model.datagram.BroadcastDatagram;

public interface SpecializedStatusManager {
	/**
	 * 
	 * @param senderDatabaseId
	 * @param datagram
	 * @return null when not handled, type as String when handled
	 */
	public String handleBroadcastDatagram(BroadcastDatagram datagram);
}
