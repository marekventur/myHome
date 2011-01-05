package de.wi08e.myhome.statusmanager;

import java.util.List;

import de.wi08e.myhome.model.NamedNode;
import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.datagram.BroadcastDatagram;
import de.wi08e.myhome.model.datagram.RockerSwitchDatagram;

public class RockerSwitchStatusManager implements SpecializedStatusManager {

	private StatusManager statusManager;
	
	public RockerSwitchStatusManager(StatusManager statusManager) {
		this.statusManager = statusManager;
	}

	@Override
	public String handleBroadcastDatagram(BroadcastDatagram datagram) {
		
		if (datagram instanceof RockerSwitchDatagram) {			
			RockerSwitchDatagram rockerSwitchDatagram = (RockerSwitchDatagram) datagram;
	
			// Find all triggered nodes
			List<Node> receivers = statusManager.getTriggerManager().getReceiver(datagram.getSender());
			for (Node receiver: receivers) {
				float light = rockerSwitchDatagram.getOnOff() == RockerSwitchDatagram.State.ON?1:0;
				statusManager.writeStatusChangeToDatabase(receiver.getDatabaseId(), "light", String.valueOf(light));
			}
			
			 
			
			return "rockerswitch";
		}
		
		return null;
	}

}
