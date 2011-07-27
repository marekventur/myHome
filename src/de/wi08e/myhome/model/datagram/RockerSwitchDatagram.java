package de.wi08e.myhome.model.datagram;

import de.wi08e.myhome.model.Node;

/**
 * @author Marek
 *
 */
public class RockerSwitchDatagram extends BroadcastDatagram {

	public enum Action {PRESSED, RELEASED}
	public enum Channel {
		A, B, C, D;
		
		public static Channel convertFromChar(char channel) {
			if (channel == 'a' || channel == 'A') return A;
			if (channel == 'b' || channel == 'B') return B;
			if (channel == 'c' || channel == 'C') return C;
			if (channel == 'd' || channel == 'D') return D;
			throw new IllegalArgumentException("Only chars from 'a' to 'd' are allowed.");
		}
		
		public char getChar() {
			if (this == A) return 'a';
			if (this == B) return 'b';
			if (this == C) return 'c';
			return 'd';
		}
	}
	public enum State {ON, OFF}
	private Channel channel;
	private State state;
	private Action action;
	
	/**
	 * @param sender
	 */
	public RockerSwitchDatagram(Node sender, Channel channel, State state, Action action) {
		super(sender);	
		this.channel = channel;
		this.action = action;
		this.state = state;

	}
	
	public Channel getChannel() {
		return channel;
	}
	
	public Action getAction() {
		return action;
	}

	public State getState() {
		return state;
	}
	
	public String toString() {
		return "Channel:"+getChannel()+ "  Action:"+getAction()+ "   State:"+getState()+ "   Node:"+getSender(); 
	}
	
	

}
