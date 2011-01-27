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
import javax.swing.SwingConstants;

import de.wi08e.myhome.model.Node;

/**
 * @author Marek_Ventur
 */
public class GUI extends JFrame {
	
	public GUI(String title, List<NodePanel> nodePanels) {
		super(title);
		setLayout(new BorderLayout());
	
		/* Title */				
		getContentPane().add(new JLabel("Enocean Simulator: "+title), BorderLayout.NORTH);
			
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
