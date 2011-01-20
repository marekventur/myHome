package de.wi08e.myhome.model.datagram;

import de.wi08e.myhome.model.Node;

/**
 * This Datagram sends Information about the Stream-Url
 * @author Nico
 *
 */

public class StreamDatagram extends BroadcastDatagram{
	private String camera;
	private String streamUrl;
	
	public StreamDatagram(Node sender, String camera, String streamUrl) {
		super(sender);	
		this.camera = camera;
		this.streamUrl = streamUrl;
	}

	public String getCamera() {
		return camera;
	}

	public String getStreamUrl() {
		return streamUrl;
	}
}
