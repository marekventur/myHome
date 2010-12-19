/**
 * 
 */
package de.wi08e.myhome.nodeplugins.enoceansimulator;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**
 * @author Marek
 *
 */
public class Relais extends JPanel {
	/**
	 * 
	 */
	public Relais(String title) {
		super(new GridLayout(1, 1));
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), title));
		setSize(250, 150);
		
		JCheckBox checkBox = new JCheckBox("Activated");
		checkBox.setEnabled(false);
		checkBox.setSelected(true);
		add(checkBox);
	}
}
