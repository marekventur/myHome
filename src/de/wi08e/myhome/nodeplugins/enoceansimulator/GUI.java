/**
 * 
 */
package de.wi08e.myhome.nodeplugins.enoceansimulator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.w3c.dom.Node;

/**
 * @author Marek
 *
 */
public class GUI extends JFrame {
	
	public GUI(String title, Node data) {
		super(title);
		setLayout(new BorderLayout());
	
		/* Title */				
		getContentPane().add(new JLabel("Enocean Simulator: "+title), BorderLayout.NORTH);
			
		/* Content */
		JPanel content = new JPanel(new GridLayout(0,2));
		getContentPane().add(content, BorderLayout.CENTER);	
		
		content.add(new TwoRockerSwitch("Lichtschalter"));	    
		content.add(new Relais("Lampe 1"));
		content.add(new Relais("Lampe 2"));

	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    pack();
	    setVisible(true);
	}
	
	public static void main(String[] args) {
		new GUI("Wohnzimmer", null);
	}
}
