/**
 * 
 */
package de.wi08e.myhome.nodeplugins.enoceansimulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import de.wi08e.myhome.model.Node;

public class WindowHandle extends NodePanel {
	
	private class InnerPanel extends JPanel{
		
		public InnerPanel() {
			super();
		}

		@Override
		protected void paintChildren(Graphics g) {
			Dimension size = getSize();
			

	        // draw circle (color already set to foreground)
			g.setColor(Color.BLUE);
	        g.fillRect(0, 0, size.width, size.height);
		}
	}

	public enum Status {CLOSED, OPENED, TILTED};
	
	private LayoutManager layout;
	private String title;
	private Node node;
	private NodePanelEventHandler eventHandler;
	
	private Status status = Status.CLOSED;
	
	public WindowHandle(String title, Node node, NodePanelEventHandler eventHandler) {
		super(new GridLayout(1, 1), title, node, eventHandler);
		this.eventHandler = eventHandler;
		
		this.add(new InnerPanel());
			
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), title));
		setSize(250, 150);
	}

	

	
}
