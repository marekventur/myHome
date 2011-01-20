/**
 * @author Marek_Ventur
 */

package de.wi08e.myhome.nodemanager;

import de.wi08e.myhome.model.datagram.BroadcastDatagram;
import de.wi08e.myhome.model.datagram.StatusDatagram;

public interface DatagramReceiver {
	public void receiveBroadcastDatagram(BroadcastDatagram datagram);
	public void receiveStatusDatagram(StatusDatagram datagram);
}
