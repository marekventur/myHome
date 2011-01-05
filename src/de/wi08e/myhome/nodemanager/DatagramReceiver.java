package de.wi08e.myhome.nodemanager;

import de.wi08e.myhome.model.datagram.BroadcastDatagram;

public interface DatagramReceiver {
	public void receiveBroadcastDatagram(BroadcastDatagram datagram);
}
