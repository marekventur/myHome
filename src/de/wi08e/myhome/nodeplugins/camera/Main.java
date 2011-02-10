package de.wi08e.myhome.nodeplugins.camera;

/**
 * @author Nico
 * Main Class for CameraPlugin with ImageFtpServer
 * Change Rate of Pictures in seconds in Class FTPServerData.java
 * in Function receiveFile(String path).
 */



import java.awt.Image;
import java.util.Map;
import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.datagram.AlarmDatagram;
import de.wi08e.myhome.model.datagram.Datagram;
import de.wi08e.myhome.model.datagram.NodeInformDatagram;
import de.wi08e.myhome.model.datagram.StartSnapshottingDatagram;
import de.wi08e.myhome.nodeplugins.NodePlugin;
import de.wi08e.myhome.nodeplugins.NodePluginEvent;
import de.wi08e.myhome.nodeplugins.NodePluginException;


public class Main implements NodePlugin{
		
	protected Image lastImage = null;
	protected Node node;
	protected NodePluginEvent event;
	protected static final int IMAGE_WIDTH = 300;
	protected static final int IMAGE_HEIGHT = 300;
	private FtpServer server = null;

	public Node getNode() {
		return node;
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
		node.setType("cameraplugin");
		this.event = event;
		if (properties.containsKey("name")) 
			event.datagrammReceived(new NodeInformDatagram(node, properties.get("name")));
		else
			event.datagrammReceived(new NodeInformDatagram(node));
		FtpServer server = new FtpServer(this);
		this.server = server;
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
		if (datagram instanceof StartSnapshottingDatagram) {
			
			StartSnapshottingDatagram startSnapshotingDatagram = (StartSnapshottingDatagram)datagram;
			
			if (startSnapshotingDatagram.getReceiver().equals(node)) {
					server.imageCounter = 1;
			}
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
			return this.lastImage;
		}
		return null;
	}
}
