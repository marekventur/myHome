/**
 * 
 */
package de.wi08e.myhome.statusmanager;

import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
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
	
			statusManager.attemptDatabaseStatusChangeFromDatagram(roomTemperatureDatagram.getSender(), "setpoint", String.valueOf(roomTemperatureDatagram.getSetPoint()));
			statusManager.attemptDatabaseStatusChangeFromDatagram(roomTemperatureDatagram.getSender(), "roomtemperature", String.valueOf(roomTemperatureDatagram.getRoomTemperature()));
			
			// Find all triggered nodes
			List<Trigger> receivers = statusManager.getTriggerManager().getReceiver(datagram.getSender());
			for (Trigger receiver: receivers) {
				
				// Receiver is heatingonoff
				if (receiver.getReceiver().getType().equalsIgnoreCase("heatingonoff")) {
					
					float setPointAdjustment = 5f;
					float basicSetPoint = 21f;
					//float loweringTemperature = 4f;
										
					if (receiver.getReceiver().getStatus().containsKey("setpointadjustment"))
						setPointAdjustment = Float.parseFloat(receiver.getReceiver().getStatus().get("setpointadjustment"));
					else
						statusManager.writeStatusChangeToDatabase(receiver.getReceiver(), "setpointadjustment", "5");
					
					if (receiver.getReceiver().getStatus().containsKey("basicsetpoint"))
						basicSetPoint = Float.parseFloat(receiver.getReceiver().getStatus().get("basicsetpoint"));
					else
						statusManager.writeStatusChangeToDatabase(receiver.getReceiver(), "basicsetpoint", "21");
					
					/*
					if (receiver.getReceiver().getStatus().containsKey("loweringtemperature"))
						loweringTemperature = Float.parseFloat(receiver.getReceiver().getStatus().get("loweringtemperature"));
					else
						statusManager.writeStatusChangeToDatabase(receiver.getReceiver(), "loweringtemperature", "4");
					*/
					
					float setPointTemperature = basicSetPoint + (roomTemperatureDatagram.getSetPoint() - 0.5f) * setPointAdjustment * 2f; // - (roomTemperatureDatagram.isLoweringMode()?loweringTemperature:0.0f);
					
					statusManager.writeStatusChangeToDatabase(receiver.getReceiver(), "setpointtemperature", String.valueOf(setPointTemperature));
				}
			
			}
		
			return "temperaturesensor";
		}
		return null;
	}

	@Override
	public Datagram findDatagramForStatusChange(String key, String value,
			Trigger trigger, int[] receiverIds) throws InvalidStatusValue {
		
		
		
		if (trigger.getSender().getType().contentEquals("temperaturesensor")) {
			if (trigger.getReceiver().getType().contentEquals("heatingonoff")) {
				if (key.contentEquals("setpointtemperature")) {
					//if (!value.matches("[0-9\.]{1,2}")) 
					//	throw new InvalidStatusValue();
					
					trigger.getReceiver().loadStatus(statusManager.getDatabase());
					
					float roomTemperature = 0;
					float setPointTemperature = Float.parseFloat(value);
					
					// Send last known room temperature
					if (trigger.getSender().getStatus().containsKey("roomTemperature"))
						roomTemperature = Float.parseFloat(trigger.getSender().getStatus().get("roomTemperature"));
					
					float setPointAdjustment = 5f;
					float basicSetPoint = 21f;
					//float loweringTemperature = 4f;
										
					if (trigger.getReceiver().getStatus().containsKey("setpointadjustment"))
						setPointAdjustment = Float.parseFloat(trigger.getReceiver().getStatus().get("setpointadjustment"));
					else
						statusManager.writeStatusChangeToDatabase(trigger.getReceiver(), "setpointadjustment", "5");
					
					if (trigger.getReceiver().getStatus().containsKey("basicsetpoint"))
						basicSetPoint = Float.parseFloat(trigger.getReceiver().getStatus().get("basicsetpoint"));
					else {
						System.out.println("ok");
						statusManager.writeStatusChangeToDatabase(trigger.getReceiver(), "basicsetpoint", "21");
					}
					
					/*
					if (trigger.getReceiver().getStatus().containsKey("loweringtemperature"))
						loweringTemperature = Float.parseFloat(trigger.getReceiver().getStatus().get("loweringtemperature"));
					else
						statusManager.writeStatusChangeToDatabase(trigger.getReceiver(), "loweringtemperature", "4");
					*/

					float setPoint = (setPointTemperature - basicSetPoint) / (2 * setPointAdjustment) + 0.5f;
					// using writeStatusChangeToDatabase() in here might lead to really nasty loops
					statusManager.writeStatusChangeToDatabase(trigger.getSender(), "setPoint", String.valueOf(setPoint));
					
					return new RoomTemperatureAndSetPointDatagram(trigger.getSender(), roomTemperature, setPoint);
				}
			}
		}
		
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
