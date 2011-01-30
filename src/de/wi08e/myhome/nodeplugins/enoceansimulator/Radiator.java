/**
 * 
 */
package de.wi08e.myhome.nodeplugins.enoceansimulator;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;

import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.datagram.Datagram;
import de.wi08e.myhome.model.datagram.RockerSwitchDatagram;
import de.wi08e.myhome.model.datagram.RoomTemperatureAndSetPointDatagram;

/**
 * @author Marek
 *
 */
public class Radiator extends NodePanel {
	
	private float temperature;
	private boolean heating = false;
	
	private JLabel temperatureLabel = new JLabel();
	private JCheckBox heatingCheckBox = new JCheckBox("heating");
	private float setPointAdjustment; 
	private float basicSetPoint;
	private float loweringTemperature;
	private float efficency;
	private float lastSetPointTemperature = 0;
	private boolean loweringMode = false;
	private Node node;
	private List<RelaisTrigger> triggers;
	
	public Radiator(String title, Node node, NodePanelEventHandler nodePanelEventHandler, float startTemperature, List<RelaisTrigger> triggers, float setPointAdjustment, float basicSetPoint, float loweringTemperature, float efficency) {
		super(new GridLayout(2, 1), title, node, nodePanelEventHandler);
		
		setTemperature(startTemperature);
		
		this.setPointAdjustment = setPointAdjustment;
		this.basicSetPoint = basicSetPoint;
		this.loweringTemperature = loweringTemperature;
		this.efficency = efficency;
		this.node = node;
		this.triggers = triggers;
		
		add(temperatureLabel);
		add(heatingCheckBox);
		heatingCheckBox.setEnabled(false);
		
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), title));
		setSize(250, 150);
	}

	public float getTemperature() {
		return temperature;
	}

	private void setTemperature(float temperature) {
		if (temperature > 35f) 
			temperature = 35f;
		this.temperature = temperature;
		
		temperatureLabel.setText(String.format("%.1f °C / %.1f °C", temperature, lastSetPointTemperature));
	}

	public boolean isHeating() {
		return heating;
	}

	private void setHeating(boolean heating) {
		this.heating = heating;
		heatingCheckBox.setSelected(heating);
	}
	
	public void refreshSetPointTemperature(float setPoint) {
		lastSetPointTemperature = basicSetPoint + (setPoint - 0.5f) * setPointAdjustment * 2f - (loweringMode?loweringTemperature:0.0f);
		temperatureLabel.setText(String.format("%.1f °C / %.1f °C", temperature, lastSetPointTemperature));
	}

	@Override
	public void handleDatagram(Datagram datagram) {
		
		if (datagram instanceof RoomTemperatureAndSetPointDatagram) {
			RoomTemperatureAndSetPointDatagram temperatureDatagram = (RoomTemperatureAndSetPointDatagram)datagram;
			
			for (RelaisTrigger trigger: triggers) {
				if (temperatureDatagram.getSender().equals(trigger.getNode())) {
					refreshSetPointTemperature(temperatureDatagram.getSetPoint());
					
					setHeating(lastSetPointTemperature>temperatureDatagram.getRoomTemperature());
				}
			}
			
		}
	}

	@Override
	public float temperatureStep(float roomTemperature) {
		if (isHeating()) {
			setTemperature(temperature + 0.1f);
		}
		else
		{
			setTemperature((temperature*30f + roomTemperature)/31f);
		}
		
		
		if (roomTemperature < temperature)
			return (temperature - roomTemperature)*efficency;
		
		return 0;
	}
	
	
}
