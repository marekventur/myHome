package de.wi08e.myhome.statusmanager;

import de.wi08e.myhome.model.datagram.BroadcastDatagram;

public interface SpecializedStatusManager {
	/**
	 * 
	 * @param senderDatabaseId
	 * @param datagram
	 * @return empty String when not handled, type when handled
	 */
	public String handleDatagram(int senderDatabaseId, BroadcastDatagram datagram);
}
