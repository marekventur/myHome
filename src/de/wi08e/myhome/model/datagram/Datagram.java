package de.wi08e.myhome.model.datagram;

import java.util.Date;

/**
 * A datagram acts as a superclass for all communication-packets
 * 
 * A datagram and all subclasses are (and should be) immutable, besides the processed-flag.
 * 
 * @author Marek_Ventur
 *
 */
public abstract class Datagram {
	private Date timestamp = new Date();
	private boolean processed = false;

	public synchronized boolean isProcessed() {
		return processed;
	}

	public synchronized void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public Date getTimestamp() {
		return timestamp;
	}
}
