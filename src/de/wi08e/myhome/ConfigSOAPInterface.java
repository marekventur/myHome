/**
 * 
 */
package de.wi08e.myhome;

/**
 * @author Marek
 */
public class ConfigSOAPInterface {

	public enum Protocol {HTTP, HTTPS};
	
	private Protocol protocol;
	private String host;
	private int port;
	private String path;
	private String keyFile;
	private String keyStorePassword;
	private String keyPassword;

	public ConfigSOAPInterface(Protocol protocol, String host, int port, String path) {
		super();
		this.protocol = protocol;
		this.host = host;
		this.port = port;
		this.path = path;
	}
	
	public ConfigSOAPInterface(Protocol protocol, String host, int port,
			String path, String keyFile, String keyStorePassword,
			String keyPassword) {
		super();
		this.protocol = protocol;
		this.host = host;
		this.port = port;
		this.path = path;
		this.keyFile = keyFile;
		this.keyStorePassword = keyStorePassword;
		this.keyPassword = keyPassword;
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

	public String getKeyFile() {
		return keyFile;
	}

	public String getKeyStorePassword() {
		return keyStorePassword;
	}

	public String getKeyPassword() {
		return keyPassword;
	}

	
	
}
