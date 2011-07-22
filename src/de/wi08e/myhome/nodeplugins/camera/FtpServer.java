package de.wi08e.myhome.nodeplugins.camera;


import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Nico
 * some ideas and code snipes from http://www.ryanheise.com/software/jftpd/
 * and http://www.echo.nuee.nagoya-u.ac.jp/~tanaka/MyFtpd.java
 * FTP Commands according to Common Commands on"http://www.nsftools.com/tips/RawFTP.htm"
 * and rfc959.
 * Create Server Socket and listens to incomming
 * connections. Creates new Thread for Connection.
 */
public class FtpServer{
	/**
	 * Default Server Port
	 */
	public static final int SERVER_PORT = 1201;

	/**
	 * Default Data Port
	 */
	public static final int SERVER_DATA_PORT = 1200;

	/**
	 * Default Listening Port
	 */
	private int port;
	protected Main main;
	
	public int imageCounter = 0;

	/**
	 * Main Method for using FtpServer, args[] may be a specific port
	 */
	public static void main(String[] args) throws Exception{
		int port = SERVER_PORT;
		if (args.length == 1){
			port = Integer.parseInt(args[0]);
		}
		FtpServer server = new FtpServer(port);
		server.start();
		}

	/**
	 * FTP server on default port 21.
	 */
	public FtpServer(){
		this.port = SERVER_PORT;
		}
	
	public FtpServer(Main main){
		this.port = SERVER_PORT;
		this.main = main;
		}

	/**
	 * FTP server on  specified port.
	 */
	public FtpServer(int port){
		this.port = port;
		}
	
	/**
	 * Start FTP server and open new Thread for connection handling.
	 */
	protected void start()throws Exception
		{
		ServerSocket serverSocket = new ServerSocket(port);
		while (true)
			{
			Socket clientSocket = serverSocket.accept();
			FtpServerProtocol serverprotocol = new FtpServerProtocol(this, clientSocket);
			new Thread(serverprotocol).start();
			}
		}

	/**
	 * Handle a client connection. May be called by a PASV Command.
	 *
	 * @param socket the client socket.
	 */
	public void service(Socket socket)throws Exception{
		FtpServerProtocol protocol = new FtpServerProtocol(this, socket);
		protocol.run();
	}
}
