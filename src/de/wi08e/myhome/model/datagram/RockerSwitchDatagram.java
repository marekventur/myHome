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
		public char getChar() {
			if (this == A) return 'a';
			if (this == B) return 'b';
			if (this == C) return 'c';
			return 'd';
		}
	}
	public enum State {ON, OFF}
	private Channel button;
	private State onOff;
	private Action state;
	
	/**
	 * @param sender
	 */
	public RockerSwitchDatagram(Node sender, Channel button, State onOff, Action state) {
		super(sender);	
		this.button = button;
		this.state = state;
		this.onOff = onOff;

	}
	
	public Channel getChannel() {
		return button;
	}
	
	public Action getAction() {
		return state;
	}

	public State getState() {
		return onOff;
	}
	
	

}
