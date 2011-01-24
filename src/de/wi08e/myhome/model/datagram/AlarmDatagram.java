package de.wi08e.myhome.model.datagram;

import de.wi08e.myhome.model.Node;

/**
 * This Datagram sends Information about a Triggered Alarm from Motion Detection Sensor
 * @author Nico
 *
 */
public class AlarmDatagram extends BroadcastDatagram{
	
	public enum State {ON, OFF};
	public enum Channel {AN, AUS};
	private Channel channel;
	private State state;

	public AlarmDatagram(Node sender, Channel channel, State state) {
		super(sender);
		this.channel=channel;
		this.state=state;
		// TODO Auto-generated constructor stub
	}
	
	public Channel getChannel() {
		return channel;
	}

	public State getState() {
		return state;
	}
}
