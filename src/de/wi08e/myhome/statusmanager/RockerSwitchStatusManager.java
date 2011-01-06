package de.wi08e.myhome.statusmanager;

import java.util.List;

import de.wi08e.myhome.model.Trigger;
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
			List<Trigger> receivers = statusManager.getTriggerManager().getReceiver(datagram.getSender());
			for (Trigger receiver: receivers) {
				
				
				
				// Receiver is Relais
				if (receiver.getReceiver().getType().equalsIgnoreCase("relais")) {
					
					// Only if button is realeased
					if (rockerSwitchDatagram.getAction() == RockerSwitchDatagram.Action.RELEASED) {
						// Only if correct channel is choosen
						if (rockerSwitchDatagram.getChannel().getChar() == receiver.getChannel()) {
							float light = rockerSwitchDatagram.getState() == RockerSwitchDatagram.State.ON?1:0;
							statusManager.writeStatusChangeToDatabase(receiver.getReceiver().getDatabaseId(), "light", String.valueOf(light));
						}
					}
				}
				
				// Receiver is Dimmer
				if (receiver.getReceiver().getType() == "dimmer") {
					// Whatever...
				}
				
				
				
			}
			
			 
			
			return "rockerswitch";
		}
		
		return null;
	}

}
