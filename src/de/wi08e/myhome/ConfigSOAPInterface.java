/**
 * 
 */
package de.wi08e.myhome;

/**
 * @author Marek
 *
 */
public class ConfigSOAPInterface {

	public enum Protocol {HTTP, HTTPS};
	
	private Protocol protocol;
	private String host;
	private int port;
	private String path;
	
	public ConfigSOAPInterface(Protocol protocol, String host, int port, String path) {
		super();
		this.protocol = protocol;
		this.host = host;
		this.port = port;
		this.path = path;
	}

	public Protocol getProtocol() {
		return protocol;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getPath() {
		return path;
	}

	
}
