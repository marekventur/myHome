package de.wi08e.myhome.model.datagram;

import de.wi08e.myhome.model.Node;

/**
 * @author Marek
 *
 */
public class RockerSwitchDatagram extends BroadcastDatagram {

	public enum State {PRESSED, RELEASED}
	public enum Button {A, B}
	public enum OnOff {ON, OFF}
	private Button button;
	private OnOff onOff;
	private State state;
	
	/**
	 * @param sender
	 */
	public RockerSwitchDatagram(Node sender, Button button, OnOff onOff, State state) {
		super(sender);	
		this.button = button;
		this.state = state;
		this.onOff = onOff;

	}
	
	public Button getButton() {
		return button;
	}
	
	public State getState() {
		return state;
	}

	public OnOff getOnOff() {
		return onOff;
	}
	
	

}
