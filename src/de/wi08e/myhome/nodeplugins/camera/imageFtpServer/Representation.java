
package de.wi08e.myhome.nodeplugins.camera.imageFtpServer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Hashtable;

/**
 * Representation type for the server to use with the TYPE command.
 */
public abstract class Representation{
	/**
	 * Representation map.
	 */
	private static Hashtable representations = new Hashtable();

	/**
	 * ASCII representation.
	 */
	public static final Representation ASCII = new AsciiRepresentation();

	/**
	 * IMAGE representation.
	 */
	public static final Representation IMAGE = new ImageRepresentation();

	/**
	 * @return epresentation indicated by the code argument.
	 */
	public static Representation get(char code){
		return (Representation)representations.get(new Character(code));
	}

	private String name;

	private char code;

	protected Representation(String name, char code){
		this.name = name;
		this.code = code;
		representations.put(new Character(code), this);
	}

	/**
	 * @return name of representation type.
	 */
	public final String getName(){
		return name;
	}

	/**
	 * @return character code for representation type.
	 */
	public final char getCode(){
		return code;
	}

	/**
	 * @return input stream to read data from socket. Data
	 * translated from this representation to the server's representation.
	 */
	public abstract InputStream getInputStream(Socket socket)
		throws IOException;

	/**
	 * @return output stream to write data to socket. Data
	 * translated from the server's representation to this representation.
	 */
	public abstract OutputStream getOutputStream(Socket socket)
		throws IOException;

	/**
	 * @return size that the specified file would have in this
	 * representation.
	 */
	public abstract long sizeOf(File file)
		throws IOException;
}
