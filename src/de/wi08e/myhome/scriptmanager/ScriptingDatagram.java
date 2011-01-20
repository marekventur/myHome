/**
 * 
 */
package de.wi08e.myhome.scriptmanager;

import de.wi08e.myhome.model.datagram.Datagram;
import de.wi08e.myhome.model.datagram.RockerSwitchDatagram;

/**
 * @author Marek
 *
 */
public class ScriptingDatagram {
	
	private Datagram datagram;
	
	public ScriptingDatagram(Datagram datagram) {
		this.datagram = datagram;
	}
	
	public String getType() {
		if (datagram instanceof RockerSwitchDatagram)
			return "RockerSwitchDatagram";
		return "";	
	}
	
	public String getChannel() {
		if (datagram instanceof RockerSwitchDatagram)
			return ((RockerSwitchDatagram)datagram).getChannel().toString();
		return null;
	}

	public String getAction() {
		if (datagram instanceof RockerSwitchDatagram)
			return ((RockerSwitchDatagram)datagram).getAction().toString();
		return null;
	}
	
	public String getState() {
		if (datagram instanceof RockerSwitchDatagram)
			return ((RockerSwitchDatagram)datagram).getState().toString();
		return null;
	}

}
