package de.wi08e.myhome.nodeplugins.mielesimulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JPanel powerPanel = null;
	private JButton an = null;
	private JButton aus = null;
	private JCheckBox powerBox = null;
	
	public GUI()
	{
		super();
		this.setLayout(null);
		this.setTitle("Miele Dampfgarer");
		this.setSize(300, 300);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		
		
		powerPanel = new JPanel();
		powerPanel.setSize(150, 100);
		powerPanel.setLayout(null);
		powerPanel.setVisible(true);
		powerPanel.setBorder(BorderFactory.createTitledBorder("AN / AUS"));
		
		aus = new JButton("AUS");
		aus.setBounds(75, 60, 60, 30);
		aus.setVisible(true);
		aus.addActionListener(this);
		
		powerBox = new JCheckBox();
		powerBox.setText("Activated");
		powerBox.setVisible(true);
		powerBox.setBounds(30, 20, 100, 30);
		powerBox.setEnabled(false);
		
		an = new JButton("AN");
		an.setBounds(10, 60, 60, 30);
		an.addActionListener(this);
		
		powerPanel.add(an);
		powerPanel.add(aus);
		powerPanel.add(powerBox);
		
		this.add(powerPanel);
	}

	@Override
	public void actionPerformed(ActionEvent arg) 
	{
		if(arg.getSource()==aus) powerBox.setSelected(false);
		if(arg.getSource()==an) powerBox.setSelected(true);
	}


}
