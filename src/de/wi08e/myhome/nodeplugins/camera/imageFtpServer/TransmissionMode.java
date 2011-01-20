
package de.wi08e.myhome.nodeplugins.camera.imageFtpServer;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Handle sending or receiving of files.
 */
public class TransmissionMode{
	/**
	 * Transmission mode map.
	 */
	private static Hashtable transmissionModes = new Hashtable();

	private static final int BUFSIZ = 1024;

	/**
	 * STREAM transmission mode.
	 */
	
	public static final TransmissionMode STREAM = new TransmissionMode();


	private char code;

	protected TransmissionMode(char code){
		this.code = code;
		transmissionModes.put(new Character(code), this);
	}

	TransmissionMode(){
		this('S');
	}

	
	/**
	 * @return TransmissionMode indicated by code argument.
	 */
	public static TransmissionMode get(char code){
		return (TransmissionMode)transmissionModes.get(new Character(code));
	}

	/**
	 * @return code for transmission mode.
	 */
	public final char getCode(){
		return code;
	}
	
	/**
	 * Write data to socket.
	 */
	public void sendFile(InputStream in, Socket s, Representation representation) throws IOException{
		OutputStream out = representation.getOutputStream(s);
		byte buf[] = new byte[BUFSIZ];
		int nread;
		while ((nread = in.read(buf)) > 0){
			out.write(buf, 0, nread);
		}
		out.close();
	}

	/**
	 * Reads data from socket, write result to file.
	 * Shows Image in a JFrame for Testing
	 */
	public void receiveFile(Socket s, OutputStream out, Representation representation) throws IOException{
		InputStream in = representation.getInputStream(s);
		Image image = null;
        image = ImageIO.read(in);
        preview(image);
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_BINARY); 
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", bs);
        preview(bi);
		byte buf[] = new byte[BUFSIZ];
		int nread;
		while ((nread = in.read(buf, 0, BUFSIZ)) > 0){
			out.write(buf, 0, nread);
		}
		in.close();
	}
	public static void preview(Image image) {
		JFrame frame = new JFrame();
		JLabel label = new JLabel(new ImageIcon(image));
		frame.getContentPane().add(label, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
}


