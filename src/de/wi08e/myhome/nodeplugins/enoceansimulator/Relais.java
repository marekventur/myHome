/**
 * 
 */
package de.wi08e.myhome.nodeplugins.enoceansimulator;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.border.EtchedBorder;

import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.datagram.BroadcastDatagram;
import de.wi08e.myhome.model.datagram.Datagram;
import de.wi08e.myhome.model.datagram.RockerSwitchDatagram;

/**
 * @author Marek_Ventur
 */
public class Relais extends NodePanel {
	
	
	private static final long serialVersionUID = 1L;
	private final JCheckBox checkBox = new JCheckBox("Activated");
	private final List<RelaisTrigger> triggers;
	
	public Relais(String title, Node node, NodePanelEventHandler eventHandler, List<RelaisTrigger> triggers) {
		super(new GridLayout(1, 1), title, node, eventHandler);
		
		this.triggers = triggers;
		
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), title));
		setSize(250, 150);
		
		checkBox.setEnabled(false);
		checkBox.setSelected(false);
		add(checkBox);
	}
	
	public void switchOn() {
		checkBox.setSelected(true);
	}
	
	public void switchOff() {
		checkBox.setSelected(false);
	}

	@Override
	public void handleDatagram(Datagram datagram) {
		if (datagram instanceof BroadcastDatagram) {
			
			/* Should we react on this datagram? */
			BroadcastDatagram broadcastDatagram = (BroadcastDatagram)datagram;
			for (RelaisTrigger trigger: triggers) 
				if (trigger.getNode().equals(broadcastDatagram.getSender()))
					if (broadcastDatagram instanceof RockerSwitchDatagram) {
						RockerSwitchDatagram rockerSwitchDatagram = (RockerSwitchDatagram)broadcastDatagram;
						if (rockerSwitchDatagram.getChannel() == trigger.getChannel()) 
							if (rockerSwitchDatagram.getAction() == RockerSwitchDatagram.Action.RELEASED) {
								if (rockerSwitchDatagram.getState() == RockerSwitchDatagram.State.OFF)
									switchOff();
								else
									switchOn();
							}
					}
			}				
	}	
	
}
