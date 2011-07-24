/**
 * 
 */
package de.wi08e.myhome.nodeplugins.enoceansimulator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import de.wi08e.myhome.model.Node;

/**
 * @author Marek_Ventur
 */
public class GUI extends JFrame {
	
	private float temperature = 0;
	private static final float MINTEMPERATURE = 0;
	private static final float MAXTEMPERATURE = 40;
	private JProgressBar temperatureProgressBar;
	
	public void setTemperature(float temperature) {
		this.temperature = temperature;
		temperatureProgressBar.setValue(Math.round(temperature * 100));
		temperatureProgressBar.setString(String.format("%.1f °C", temperature));
	}

	public float getTemperature() {
		return temperature;
	}
	
	public GUI(String title, List<NodePanel> nodePanels, float temperature) {
		super(title);
		setLayout(new BorderLayout());
	
		/* Title */				
		getContentPane().add(new JLabel("Enocean Simulator: "+title), BorderLayout.NORTH);
		
		/* Temperature */	
		temperatureProgressBar = new JProgressBar(Math.round(MINTEMPERATURE*100), Math.round(MAXTEMPERATURE*100));
		setTemperature(temperature);
		temperatureProgressBar.setStringPainted(true);
		getContentPane().add(temperatureProgressBar, BorderLayout.SOUTH);
			
		/* Content */
		JPanel content = new JPanel(new GridLayout(0,2));
		getContentPane().add(content, BorderLayout.CENTER);	
		
		/* Add Nodes */
		for (NodePanel nodePanel: nodePanels) 
			content.add(nodePanel);	    

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	public void createAndShowGUI() {
	    
	    pack();
	    setVisible(true);
	}
}
