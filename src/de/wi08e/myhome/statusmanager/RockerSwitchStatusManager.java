package de.wi08e.myhome.statusmanager;

import de.wi08e.myhome.database.Database;
import de.wi08e.myhome.model.datagram.BroadcastDatagram;
import de.wi08e.myhome.model.datagram.RockerSwitchDatagram;

public class RockerSwitchStatusManager implements SpecializedStatusManager {

	private StatusManager statusManager;
	
	public RockerSwitchStatusManager(StatusManager statusManager) {
		this.statusManager = statusManager;
	}

	@Override
	public String handleDatagram(int senderDatabaseId, BroadcastDatagram datagram) {
		
		if (datagram instanceof RockerSwitchDatagram) {
			RockerSwitchDatagram rockerSwitchDatagram = (RockerSwitchDatagram) datagram;
	
			float light = rockerSwitchDatagram.getOnOff() == RockerSwitchDatagram.OnOff.ON?1:0;
			statusManager.writeStatusChangeToDatabase(senderDatabaseId, "light", String.valueOf(light)); 
			
			return "rockerswitch";
		}
		
		return "";
	}

}
