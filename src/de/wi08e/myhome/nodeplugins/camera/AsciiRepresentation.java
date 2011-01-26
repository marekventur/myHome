
package de.wi08e.myhome.nodeplugins.camera;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;



/**
 * @author Nico
 * some ideas and code snipes from http://www.ryanheise.com/software/jftpd/
 * and http://www.echo.nuee.nagoya-u.ac.jp/~tanaka/MyFtpd.java
 * Convert data streams to and from ASCII representation.
 */
public class AsciiRepresentation extends Representation{
	protected AsciiRepresentation(){
		super("ascii", 'A');
	}

	/**
	 * @return input stream to read data from socket.
	 */
	public InputStream getInputStream(Socket socket)throws IOException{
		return new AsciiInputStream(socket.getInputStream());
	}

	/**
	 * @return output stream to write data to socket.
	 */
	public OutputStream getOutputStream(Socket socket)throws IOException{
		return new AsciiOutputStream(socket.getOutputStream());
	}

	/**
	 * @return size of File
	 */
	public long sizeOf(File file)throws IOException{
		InputStream in = new FileInputStream(file);
		long count = 0;
		try{
			int c;
			while ((c = in.read()) != -1){
				if (c == '\r'){
					continue;
				}
				if (c == '\n'){
					count++;
				}
				count++;
			}
		}
		finally{
			in.close();
		}
		return count;
	}
}

/**
 * Converts "\r\n" to "\n" before returning from read().
 */
class AsciiInputStream extends FilterInputStream{
	public AsciiInputStream(InputStream in){
		super(in);
	}
	public int read()throws IOException{
		int c;
		if ((c = in.read()) == -1){
			return c;
		}
		if (c == '\r'){
			if ((c = in.read()) == -1){
				return c;
			}
		}
		return c;
	}

	public int read(byte data[], int off, int len) throws IOException{
		if (len <= 0){
			return 0;
		}
		int c;
		if ((c = read()) == -1){
			return -1;
		}else{
			data[off] = (byte)c;
		}
		int i = 1;
		try{
			for (; i < len; i++){
				if ((c = read()) == -1){
					break;
				}
				if (c == '\r'){
					if ((c = in.read()) == -1){
						break;
					}
				}
				data[off + i] = (byte)c;
			}
		}
		catch (IOException e){
		}
		return i;
	}
}

/**
 * This output stream converts "\n" to "\r\n" before writing the data.
 */
class AsciiOutputStream extends FilterOutputStream{
	public AsciiOutputStream(OutputStream out){
		super(out);
	}
	public void write(int b)throws IOException{
		if (b == '\r'){
			return;
		}
		if (b == '\n'){
			out.write('\r');
		}
		out.write(b);
	}
	public void write(byte data[], int off, int len) throws IOException{
		for (int i = 0; i < len; i++){
			byte b = data[off + i];
			if (b == '\n'){
				out.write('\r');
			}
			out.write(b);
		}
	}
}
