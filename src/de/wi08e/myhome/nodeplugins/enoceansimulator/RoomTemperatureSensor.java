/**
 * 
 */
package de.wi08e.myhome.nodeplugins.enoceansimulator;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;

import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.datagram.RoomTemperatureAndSetPointDatagram;



/**
 * @author Marek
 *
 */
public class RoomTemperatureSensor extends NodePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private NodePanelEventHandler nodePanelEventHandler;
	private Node node;
	private int count1 = 0;
	private float lastRoomTemperature = -10;
	private int count2 = 0;

	private JSpinner setPointTemperatureSpinner;
	

	/**
	 * @param layout
	 * @param title
	 * @param node
	 * @param eventHandler
	 */
	public RoomTemperatureSensor(String title, Node node, NodePanelEventHandler nodePanelEventHandler, float setPoint) {
		super(new BorderLayout(), title, node, nodePanelEventHandler);
		this.nodePanelEventHandler = nodePanelEventHandler;
		this.node = node;
		
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), title));
		setSize(250, 150);
		
		SpinnerModel model = new SpinnerNumberModel(Math.round(setPoint*100), 0, 100, 1); 
		
		setPointTemperatureSpinner = new JSpinner(model);
		
		//setPointTemperatureSpinner.setEditor(new JSpinner.NumberEditor(setPointTemperatureSpinner, " %"));
		setPointTemperatureSpinner.setValue((Integer)Math.round(setPoint*100));
		
		add(setPointTemperatureSpinner, BorderLayout.CENTER);
	}

	public float temperatureStep(float roomTemperature) {
		
		float setPoint = new Float((Integer)setPointTemperatureSpinner.getValue()/100.0);
		/* Add innaccuracy */
		roomTemperature = roomTemperature - (float)0.4 + ((float)Math.random() * (float)0.8);
		setPoint = setPoint - (float)0.01 + ((float)Math.random() * (float)0.02);
		
		count1++;
		if (count1 == 6) { // 96 
			count2++;
			if ((count2 == 10) || 
					(Math.abs(lastRoomTemperature - roomTemperature) > 0.8)) {
				sendDatagram(roomTemperature, setPoint);
				lastRoomTemperature = roomTemperature;
			}

			count1= 0;
		}
		
		return (float) 0.0;
	}
	
	private void sendDatagram(float roomTemperature, float setPoint) {
		count1 = 0;
		count2 = 0;
		nodePanelEventHandler.receive(new RoomTemperatureAndSetPointDatagram(node, roomTemperature, setPoint));
	}
}
