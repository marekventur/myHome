/**
 * 
 */
package de.wi08e.myhome.model.datagram;

import de.wi08e.myhome.model.Node;

/**
 * @author Marek
 *
 */
public class RoomTemperatureAndSetPointDatagram extends BroadcastDatagram {
	private float roomTemperature;
	private float setPoint;
	private boolean loweringMode = false;
	
	public RoomTemperatureAndSetPointDatagram(Node sender, float roomTemperature, float setPoint) {
		super(sender);
		this.roomTemperature = roomTemperature;
		this.setPoint = setPoint;
	}
	
	public RoomTemperatureAndSetPointDatagram(Node sender,
			float roomTemperature, float setPoint, boolean loweringMode) {
		super(sender);
		this.roomTemperature = roomTemperature;
		this.setPoint = setPoint;
		this.loweringMode = loweringMode;
	}

	public float getRoomTemperature() {
		return roomTemperature;
	}
	
	public float getSetPoint() {
		return setPoint;
	}

	public boolean isLoweringMode() {
		return loweringMode;
	}
	
	
}
