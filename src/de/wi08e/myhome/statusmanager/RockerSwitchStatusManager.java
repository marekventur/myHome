package de.wi08e.myhome.statusmanager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import de.wi08e.myhome.exceptions.InvalidStatusValue;
import de.wi08e.myhome.model.Trigger;
import de.wi08e.myhome.model.datagram.BroadcastDatagram;
import de.wi08e.myhome.model.datagram.Datagram;
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
					
					// Only if button is pressed
					if (rockerSwitchDatagram.getAction() == RockerSwitchDatagram.Action.PRESSED) {
						// Only if correct channel is choosen
						if (rockerSwitchDatagram.getChannel().getChar() == receiver.getChannel()) {
							float light = rockerSwitchDatagram.getState() == RockerSwitchDatagram.State.ON?1:0;
							statusManager.attemptDatabaseStatusChangeFromDatagram(receiver.getReceiver(), "light", String.valueOf(light));
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

	@Override
	public Datagram findDatagramForStatusChange(String key, String value, Trigger trigger, int[] receiverIds) throws InvalidStatusValue {
		System.out.println(trigger);
		if (trigger.getSender().getType().contentEquals("rockerswitch") || trigger.getSender().getType().contentEquals("gateway")) {
			
			if (trigger.getReceiver().getType().contentEquals("relais")) {
				if (key.contentEquals("light")) {
					if (!value.matches("[01](.0)?")) 
						throw new InvalidStatusValue();
					RockerSwitchDatagram.State state = (value.charAt(0)=='1')?RockerSwitchDatagram.State.ON:RockerSwitchDatagram.State.OFF;
					RockerSwitchDatagram rsd = new RockerSwitchDatagram(trigger.getSender(), RockerSwitchDatagram.Channel.convertFromChar(trigger.getChannel()), state, RockerSwitchDatagram.Action.RELEASED);
					System.out.println("Send: "+ rsd);
					
					return rsd;
				}
			}
		}
		
		return null;
	}

	@Override
	public Set<String> getAllTypes() {
		Set<String> result = new HashSet<String>();
		result.add("rockerswitch");
		result.add("relais");
		return result;
	}

}
