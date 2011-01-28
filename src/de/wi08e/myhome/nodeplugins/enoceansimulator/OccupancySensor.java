/**
 * 
 */
package de.wi08e.myhome.nodeplugins.enoceansimulator;

import java.awt.GridLayout;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.datagram.OccupancySensorDatagram;

/**
 * @author Marek
 *
 */
public class OccupancySensor extends NodePanel {

	private static final long serialVersionUID = 1L;

	private JCheckBox checkBox = new JCheckBox("Movement");
	
	private boolean occupied = false;
	private Node node;
	
	private NodePanelEventHandler nodePanelEventHandler;
	
	private class OccupancySensorChangeListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			if (!occupied && checkBox.isSelected()) {
				occupied = checkBox.isSelected();
				sendDatagram();
			}
			else
				occupied = checkBox.isSelected();
		}
	}
	
	private class OccupancySensorTimer extends TimerTask {

		private int count = 0;
		
		@Override
		public void run() {
			count++;
			if (((count > 0) && occupied) || (count > 9)){
				count = 0;
				sendDatagram();
			}
			
		}
		
	}
	
	private void sendDatagram() {
		nodePanelEventHandler.receive(new OccupancySensorDatagram(node, occupied));
	}
	
	/**
	 * @param layout
	 * @param title
	 * @param node
	 * @param eventHandler
	 */
	public OccupancySensor(String title, Node node, NodePanelEventHandler nodePanelEventHandler) {
		super(new GridLayout(1, 1), title, node, nodePanelEventHandler);
		this.nodePanelEventHandler = nodePanelEventHandler;
		this.node = node;
		
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), title));
		setSize(250, 150);
		
		checkBox.addChangeListener(new OccupancySensorChangeListener());
		add(checkBox);
		
		Timer timer = new Timer();
		timer.schedule(new OccupancySensorTimer(), 1000, 1000*100);
	}

}
