
package de.wi08e.myhome.nodeplugins.camera.imageFtpServer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Converts data streams to and from IMAGE representation.
 */
public class ImageRepresentation extends Representation{
	public ImageRepresentation(){
		super("binary", 'I');
	}

	/**
	 * @return input stream to read data from the socket.
	 */
	public InputStream getInputStream(Socket socket) throws IOException{
		return socket.getInputStream();
	}

	/**
	 * @return output stream to write data to the socket.
	 */
	public OutputStream getOutputStream(Socket socket) throws IOException{
		return socket.getOutputStream();
	}

	/**
	 * @return size of file
	 */
	public long sizeOf(File file) throws IOException{
		return file.length();
	}
}
