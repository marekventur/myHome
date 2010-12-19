/**
 * 
 */
package de.wi08e.myhome.nodeplugins.enoceansimulator;

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;

import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.datagram.RockerSwitchDatagram;

/**
 * @author Marek
 *
 */


public class TwoRockerSwitch extends NodePanel {

	private final NodePanelEventHandler nodePanelEventHandler;
	
	private class RockerSwitchMouseListener implements MouseListener {
		
		private RockerSwitchDatagram.Button button ;
		private RockerSwitchDatagram.OnOff onOff ;
		private Node node;
		
		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {
			nodePanelEventHandler.receive(new RockerSwitchDatagram(node, button, onOff, RockerSwitchDatagram.State.PRESSED));
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			nodePanelEventHandler.receive(new RockerSwitchDatagram(node, button, onOff, RockerSwitchDatagram.State.RELEASED));
		}
		
		public RockerSwitchMouseListener(Node node, RockerSwitchDatagram.Button button, RockerSwitchDatagram.OnOff onOff) {
			super();
			this.button = button;
			this.node = node;
			this.onOff = onOff;
		}
	}
	
	private final JButton a0 = new JButton("A0");
	private final JButton a1 = new JButton("A1");
	private final JButton b0 = new JButton("B0");
	private final JButton b1 = new JButton("B1");
	
	/**
	 * @param nodePanelEventHandler 
	 * 
	 */
	public TwoRockerSwitch(String title, Node node, NodePanelEventHandler nodePanelEventHandler) {
		super(new GridLayout(2, 2), title, node, nodePanelEventHandler);
		this.nodePanelEventHandler = nodePanelEventHandler;
		
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), title));
		setSize(250, 150);
		
		a1.addMouseListener(new RockerSwitchMouseListener(node, RockerSwitchDatagram.Button.A, RockerSwitchDatagram.OnOff.ON));
		a0.addMouseListener(new RockerSwitchMouseListener(node, RockerSwitchDatagram.Button.A, RockerSwitchDatagram.OnOff.OFF));
		b1.addMouseListener(new RockerSwitchMouseListener(node, RockerSwitchDatagram.Button.B, RockerSwitchDatagram.OnOff.ON));
		b0.addMouseListener(new RockerSwitchMouseListener(node, RockerSwitchDatagram.Button.B, RockerSwitchDatagram.OnOff.OFF));
		
		add(a1);
		add(b1);
		add(a0);
		add(b0);
	}


}
