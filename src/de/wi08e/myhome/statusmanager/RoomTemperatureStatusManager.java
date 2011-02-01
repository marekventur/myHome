/**
 * 
 */
package de.wi08e.myhome.statusmanager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.wi08e.myhome.exceptions.InvalidStatusValue;
import de.wi08e.myhome.model.Trigger;
import de.wi08e.myhome.model.datagram.BroadcastDatagram;
import de.wi08e.myhome.model.datagram.Datagram;
import de.wi08e.myhome.model.datagram.RockerSwitchDatagram;
import de.wi08e.myhome.model.datagram.RoomTemperatureAndSetPointDatagram;

/**
 * @author Marek
 *
 */
public class RoomTemperatureStatusManager implements SpecializedStatusManager {

	private StatusManager statusManager;
	
	public RoomTemperatureStatusManager(StatusManager statusManager) {
		this.statusManager = statusManager;
	}
	
	@Override
	public String handleBroadcastDatagram(BroadcastDatagram datagram) {
		
		if (datagram instanceof RoomTemperatureAndSetPointDatagram) {			
			RoomTemperatureAndSetPointDatagram roomTemperatureDatagram = (RoomTemperatureAndSetPointDatagram) datagram;
	
			statusManager.writeStatusChangeToDatabase(roomTemperatureDatagram.getSender(), "setpoint", String.valueOf(roomTemperatureDatagram.getSetPoint()));
			statusManager.writeStatusChangeToDatabase(roomTemperatureDatagram.getSender(), "roomtemperature", String.valueOf(roomTemperatureDatagram.getRoomTemperature()));
			
			// Find all triggered nodes
			List<Trigger> receivers = statusManager.getTriggerManager().getReceiver(datagram.getSender());
			for (Trigger receiver: receivers) {
		
				// Receiver is heatingonoff
				if (receiver.getReceiver().getType().equalsIgnoreCase("heatingonoff")) {	
					// statusManager.writeStatusChangeToDatabase(node, key, value);
				}
				
				// Receiver is Dimmer
				if (receiver.getReceiver().getType() == "dimmer") {
					// Whatever...
				}
			
			}
		
			return "temperaturesensor";
		}
		return null;
	}

	@Override
	public Datagram findDatagramForStatusChange(String key, String value,
			Trigger trigger, int[] receiverIds) throws InvalidStatusValue {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getAllTypes() {
		Set<String> result = new HashSet<String>();
		result.add("temperaturesensor");
		result.add("heatingonoff");
		return result;
	}

}
