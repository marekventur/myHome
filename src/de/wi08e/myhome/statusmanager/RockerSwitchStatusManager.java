package de.wi08e.myhome.statusmanager;

import java.util.List;

import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.Trigger;
import de.wi08e.myhome.model.datagram.BroadcastDatagram;
import de.wi08e.myhome.model.datagram.Datagram;
import de.wi08e.myhome.model.datagram.RockerSwitchDatagram;
import de.wi08e.myhome.model.datagram.RockerSwitchDatagram.Action;
import de.wi08e.myhome.model.datagram.RockerSwitchDatagram.Channel;
import de.wi08e.myhome.model.datagram.RockerSwitchDatagram.State;
import de.wi08e.myhome.scriptmanager.ScriptManager;

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
							statusManager.writeStatusChangeToDatabase(receiver.getReceiver(), "light", String.valueOf(light));
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
	public Datagram findDatagramForStatusChange(String key, String value, Trigger trigger, int[] receiverIds) throws InvalidStatusValueException {
		if (trigger.getSender().getType().contentEquals("rockerswitch")) {
			if (trigger.getReceiver().getType().contentEquals("relais")) {
				if (key.contentEquals("light")) {
					if (!value.matches("[01](.0)?")) 
						throw new InvalidStatusValueException();
					RockerSwitchDatagram.State state = (value.charAt(0)=='1')?RockerSwitchDatagram.State.ON:RockerSwitchDatagram.State.OFF;
					return new RockerSwitchDatagram(trigger.getSender(), RockerSwitchDatagram.Channel.convertFromChar(trigger.getChannel()), state, RockerSwitchDatagram.Action.RELEASED);
				}
			}
		}
		
		return null;
	}

}
