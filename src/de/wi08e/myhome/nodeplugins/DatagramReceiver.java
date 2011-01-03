package de.wi08e.myhome.nodeplugins;

import de.wi08e.myhome.model.datagram.Datagram;

public interface DatagramReceiver {
	public void receiveDatagram(Datagram datagram);
}
