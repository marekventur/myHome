package de.wi08e.myhome.model.datagram;

import de.wi08e.myhome.model.Node;

/**
 * This Datagram sends Information about a Triggered Alarm from Motion Detection Sensor
 * @author Nico
 *
 */

public class AlarmDatagram extends BroadcastDatagram{

	public AlarmDatagram(Node sender) {
		super(sender);
		// TODO Auto-generated constructor stub
	}
}
