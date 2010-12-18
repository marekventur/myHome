package de.wi08e.myhome.model.datagram;

import de.wi08e.myhome.model.Node;

/**
 * @author Marek
 *
 */
public class RockerSwitchDatagram extends BroadcastDatagram {

	private String button;
	private boolean pressed;
	
	/**
	 * @param sender
	 */
	public RockerSwitchDatagram(Node sender, String button, boolean pressed) {
		super(sender);
		
		button = button.toLowerCase();
		if (button.length()!=2 || !button.matches("[abcd][10]"))
			throw new IllegalArgumentException("button has to match [abcd][10]");
		
		this.button = button;
		this.pressed = pressed;
	}
	
	public String getButton() {
		return button;
	}
	
	public boolean isPressed() {
		return pressed;
	}
	
	

}
