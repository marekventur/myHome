package de.wi08e.myhome.model.datagram;

import de.wi08e.myhome.model.Node;

public class MieleSteamDatagram extends BroadcastDatagram
{
	public enum State {ON, OFF};
	public enum Channel {AN, AUS};
	private Channel channel;
	private State state;
	
	public MieleSteamDatagram(Node sender, Channel channel, State state) {
		super(sender);
		this.channel=channel;
		this.state=state;
	}
	
	public Channel getChannel() {
		return channel;
	}
	
	public State getState() {
		return state;
	}
}