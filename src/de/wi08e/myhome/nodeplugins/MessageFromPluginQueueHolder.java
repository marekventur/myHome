/**
 * 
 */
package de.wi08e.myhome.nodeplugins;


import de.wi08e.myhome.model.Snapshot;
import de.wi08e.myhome.model.datagram.Datagram;

/**
 * @author Marek
 *
 */
public class MessageFromPluginQueueHolder {
	public enum Type {RECEIVED_DATAGRAM, SNAPSHOT};
	
	private Type type;
	
	private Datagram datagram = null;
	
	private Snapshot snapshot= null;
	
	public MessageFromPluginQueueHolder(Datagram datagram) {
		type = Type.RECEIVED_DATAGRAM;
		this.datagram = datagram;
	}
	
	public MessageFromPluginQueueHolder(Snapshot snapshot) {
		type = Type.SNAPSHOT;
		this.snapshot = snapshot;
	}

	public Type getType() {
		return type;
	}

	public Datagram getDatagram() {
		return datagram;
	}

	public Snapshot getSnapshot() {
		return snapshot;
	}
}
