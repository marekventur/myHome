/**
 * 
 */
package de.wi08e.myhome.nodeplugins.enoceangateway;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;


/**
 * @author Marek
 *
 */
public class GUI extends JFrame {	

	
	
	public GUI(String idBase, NetworkRunnable nr) {
		super("Enocean Gateway");
		
		final NetworkRunnable nr2 = nr;
		
		setLayout(new BorderLayout());
	
		idBase = idBase.replaceAll(" ", "").substring(4, 12);
		
		/* Title */				
		long idBaseLong = Long.parseLong(idBase, 16);
		getContentPane().add(new JLabel("Enocean Gateway: "+idBase+" / "+idBaseLong), BorderLayout.NORTH);
			
		/* Content */
		JPanel content = new JPanel(new GridLayout(0,2));
		getContentPane().add(content, BorderLayout.CENTER);	
		
		/* Add Nodes */
		
		
		for (int i=0; i < 32; i++) {
			
			final String momId = Long.toHexString(i+idBaseLong).toUpperCase();
			JButton jbutton = new JButton(momId);
			content.add(jbutton);	
			nr.registerNode(momId, "Gateway Sender "+momId);
			
			jbutton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println(momId);
					nr2.sendTelegram2(momId, "50000000", "30");
					try {
						wait(300l);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					nr2.sendTelegram2(momId, "00000000", "20");
				}
			});
		}
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	public void createAndShowGUI() {
	    pack();
	    setVisible(true);
	}
}
