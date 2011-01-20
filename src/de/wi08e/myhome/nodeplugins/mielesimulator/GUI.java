package de.wi08e.myhome.nodeplugins.mielesimulator;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUI extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JButton an = null;
	private JButton aus = null;
	private JCheckBox powerBox = null;
	private TextField gradSollArea = null;
	private TextField gradIstArea = null;
	private String gradIst = null;
	private JButton tempSetzen = null;
	private Main main=null;
	
	public GUI(Main main)
	{
		super();
		this.main=main;
		gradIst="0";
		
		this.setLayout(null);
		this.setTitle("Miele Dampfgarer");
		this.setSize(300, 300);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		
		
		JPanel powerPanel = new JPanel();
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
		
		JLabel ist = new JLabel("IST");
		ist.setBounds(20, 22, 20, 10);
		gradIstArea = new TextField(this.getTemperatur()+ " °C");
		gradIstArea.setBounds(12, 35, 50, 20);
		gradIstArea.setEditable(false);
		gradIstArea.setBackground(this.getBackground());
		
		JLabel soll = new JLabel("Soll");
		soll.setBounds(80, 22, 40, 10);
		gradSollArea = new TextField("150 °C");
		gradSollArea.setBounds(70, 35, 50, 20);
		gradSollArea.setBackground(this.getBackground());
	
		tempSetzen =  new JButton("Set");
		tempSetzen.setBounds(40, 65, 60, 25);
		
		JPanel tempAnzeige = new JPanel();
		tempAnzeige.setLayout(null);
		tempAnzeige.setBounds(150,0,140,100);
		tempAnzeige.setBorder(BorderFactory.createTitledBorder("Temperetur"));
		tempAnzeige.add(gradIstArea);
		tempAnzeige.add(gradSollArea);
		tempAnzeige.add(tempSetzen);
		tempAnzeige.add(ist);
		tempAnzeige.add(soll);
		
		this.add(tempAnzeige);
		this.add(powerPanel);
	}

	@Override
	public void actionPerformed(ActionEvent arg) 
	{
		if(arg.getSource()==aus) this.setPower("0");
		if(arg.getSource()==an) this.setPower("1");
		if(arg.getSource()==tempSetzen) this.setTemperatur(gradSollArea.getText());
	}

	public void setPower(String status)
	{
		powerBox.setSelected(status.contentEquals("1"));
		main.setStatus("power", status);
	}
	
	public void setTemperatur(String temperatur)
	{
		double temp = Double.parseDouble(temperatur);
		NumberFormat n = NumberFormat.getInstance();
		n.setMaximumFractionDigits(2);
		System.out.println(n.format(temp));
	}
	
	public String getTemperatur()
	{
		return this.gradIst;
	}
	
}
