package de.wi08e.myhome.nodeplugins;

import de.wi08e.myhome.model.datagram.Datagram;

/**
 * @author Marek_Ventur
 */

public class MessageToPluginQueueHolder {
	
	public enum Type {SEND, RECEIVED};
	
	private Datagram datagram;
	private Type type;
	
	public MessageToPluginQueueHolder(Datagram datagram, Type type) {
		super();
		this.datagram = datagram;
		this.type = type;
	}

	public Datagram getDatagram() {
		return datagram;
	}

	public Type getType() {
		return type;
	}
}
