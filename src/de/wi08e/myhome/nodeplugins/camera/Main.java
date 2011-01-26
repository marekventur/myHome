package de.wi08e.myhome.nodeplugins.camera;

/**
 * @author Nico
 *
 */


import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Map;

import de.wi08e.myhome.Config;
import de.wi08e.myhome.database.Database;
import de.wi08e.myhome.database.MySQLDatabase;
import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.Snapshot;

import de.wi08e.myhome.model.datagram.AlarmDatagram;
import de.wi08e.myhome.model.datagram.Datagram;
import de.wi08e.myhome.model.datagram.NodeInformDatagram;
import de.wi08e.myhome.model.datagram.StreamDatagram;
import de.wi08e.myhome.nodeplugins.NodePlugin;
import de.wi08e.myhome.nodeplugins.NodePluginEvent;
import de.wi08e.myhome.nodeplugins.NodePluginException;
import de.wi08e.myhome.snapshotmanager.SnapshotManager;


public class Main implements NodePlugin{

	/**
	 * @param args
	 */
	private Node node;
	private SnapshotManager snapManager;
	private NodePluginEvent event;
	protected static final int IMAGE_WIDTH = 300;
	protected static final int IMAGE_HEIGHT = 300;
	private int lastID;
	
	public Node getNode() {
		return node;
	}

	public int getLastID() {
		return lastID;
	}

	public void setLastID(Snapshot snapshot) {
		int lastID = snapManager.storeSnapshot(snapshot);
		this.lastID = lastID;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FtpServer server = new FtpServer();
		try {
			server.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void initiate(NodePluginEvent event, Map<String, String> properties,
			org.w3c.dom.Node data) throws NodePluginException {
		// TODO Auto-generated method stub
		if (!properties.containsKey("id"))
			throw new NodePluginException("cameraplugin", "Parameter 'id' not found!");
		node = new Node(getCategory(), "camera", properties.get("id"));
		Database database = new MySQLDatabase(Config.getDatabaseHost(), Config.getDatabasePort(), Config.getDatabaseName(), Config.getDatabaseUser(), Config.getDatabasePassword());
		snapManager = new SnapshotManager(database);
		this.event = event;
		if (properties.containsKey("name")) 
			event.datagrammReceived(new NodeInformDatagram(node, properties.get("name")));
		else
			event.datagrammReceived(new NodeInformDatagram(node));
		FtpServer server = new FtpServer(this);
		try {
			server.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new NodePluginException("cameraplugin", "Error with starting the " +
					"ImageFTPServer");
		}
		
	}

	@Override
	public void chainReceiveDatagram(Datagram datagram) {
		if (datagram instanceof AlarmDatagram) {
			}				
	}

	@Override
	public void chainSendDatagramm(Datagram datagram) {
		// TODO Auto-generated method stub
		if (datagram instanceof StreamDatagram){
			
		}
	}

	@Override
	public String getName() {
		return "IP Kamera";
	}

	@Override
	public String getCategory() {
		return "camera";
	}

	@Override
	public Image getLastSnapshot(Node node) {
		// TODO Auto-generated method stub
		if (this.node.equals(node)) {
			Snapshot snapshot = snapManager.getSnapshot(this.getLastID());
			return snapshot.getImage();
		}
		return null;
	}
}
