package de.wi08e.myhome.statusmanager;

import de.wi08e.myhome.database.Database;
import de.wi08e.myhome.model.datagram.BroadcastDatagram;

public class RockerSwitchStatusManager implements SpecializedStatusManager {

	public RockerSwitchStatusManager(Database database) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean handleDatagram(int senderDatabaseId, BroadcastDatagram datagram) {
		// TODO Auto-generated method stub
		return false;
	}

}
