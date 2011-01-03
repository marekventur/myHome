package de.wi08e.myhome.statusmanager;

import de.wi08e.myhome.model.datagram.BroadcastDatagram;

public interface SpecializedStatusManager {
	public boolean handleDatagram(int senderDatabaseId, BroadcastDatagram datagram);
}
