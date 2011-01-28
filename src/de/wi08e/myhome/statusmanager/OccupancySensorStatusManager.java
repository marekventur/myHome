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
import de.wi08e.myhome.model.datagram.OccupancySensorDatagram;
import de.wi08e.myhome.model.datagram.RockerSwitchDatagram;
import de.wi08e.myhome.model.datagram.StartSnapshottingDatagram;

/**
 * @author Marek
 *
 */
public class OccupancySensorStatusManager implements SpecializedStatusManager {

	private StatusManager statusManager;
	
	public OccupancySensorStatusManager(StatusManager statusManager) {
		this.statusManager = statusManager;
	}

	
	@Override
	public String handleBroadcastDatagram(BroadcastDatagram datagram) {
		if (datagram instanceof OccupancySensorDatagram) {			
			OccupancySensorDatagram occupancySensorDatagram = (OccupancySensorDatagram) datagram;
	
			// Find all triggered nodes
			List<Trigger> receivers = statusManager.getTriggerManager().getReceiver(datagram.getSender());
			
			for (Trigger trigger: receivers) {
				if (trigger.getReceiver().getCategory().equalsIgnoreCase("camera")) {
					statusManager.getNodeManager().sendDatagram(new StartSnapshottingDatagram(trigger.getReceiver()));
				}
			}
			
			statusManager.writeStatusChangeToDatabase(occupancySensorDatagram.getSender(), "occupied", occupancySensorDatagram.isOccupied()?"1":"0");
		
			return "occupancysensor";
		}
		
		return null;
	}


	@Override
	public Datagram findDatagramForStatusChange(String key, String value,
			Trigger trigger, int[] receiverIds) throws InvalidStatusValue {
		return null;
	}


	@Override
	public Set<String> getAllTypes() {
		Set<String> result = new HashSet<String>();
		result.add("occupancysensor");
		return result;
	}

}
