/**
 * 
 */
package de.wi08e.myhome.nodeplugins.enoceansimulator;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**
 * @author Marek
 *
 */
public class TwoRockerSwitch extends JPanel {

	/**
	 * 
	 */
	public TwoRockerSwitch(String title) {
		super(new GridLayout(2, 2));
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), title));
		setSize(250, 150);
		
		add(new JButton("A0"));
		add(new JButton("B0"));
		
		add(new JButton("A1"));
		add(new JButton("B1"));
	}
	

}
