
package de.wi08e.myhome.nodeplugins.camera;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Nico
 * some ideas and code snipes from http://www.ryanheise.com/software/jftpd/
 * and http://www.echo.nuee.nagoya-u.ac.jp/~tanaka/MyFtpd.java
 * Server data transfer. Separate data socket created.
 */
public class FtpServerData{
	
	private FtpServerProtocol protocol;

	/**
	 * host of the data socket.
	 */
	private String dataHost;

	/**
	 * port of the data socket.
	 */
	private int dataPort = -1;

	/**
	 * Transmission mode to be used. Only Stream possible on this server.
	 */
	private TransmissionMode transmissionMode = TransmissionMode.STREAM;

	/**
	 * Representation for transmission.
	 */
	private Representation representation = Representation.ASCII;

	/**
	 * Data transfer for server protocol.
	 */
	public FtpServerData(FtpServerProtocol protocol){
		this.protocol = protocol;
	}

	/**
	 * Sets transmission mode.
	 */
	public void setTransmissionMode(TransmissionMode transmissionMode){
		this.transmissionMode = transmissionMode;
	}

	/**
	 * @return representation type for transmission.
	 */
	public Representation getRepresentation(){
		return representation;
	}

	/**
	 * Sets representation type for transmission.
	 */
	public void setRepresentation(Representation representation){
		this.representation = representation;
	}

	/**
	 * Sets data port for transmission.
	 *
	 * @param host host name to connect to.
	 * @param port port number to connect to.
	 */
	public void setDataPort(String host, int port){
		dataHost = host;
		dataPort = port;
	}

	/**
	 * Opens data connection, reads data and write it into the local file "path".
	 */
	public int receiveFile(String path)throws FtpCommandException{
		int reply = 0;
		FileOutputStream fos = null;
		Socket dataSocket = null;
		try{
			File file = new File(path);
			if (file.exists())
				throw new FtpCommandException(550, "File exists in that location.");
			fos = new FileOutputStream(file);
			if (dataPort == -1)
				throw new FtpCommandException(500, "Can't establish data connection: no PORT specified.");
			dataSocket = new Socket(dataHost, dataPort);
			// Read contens
			protocol.reply(150, "Opening " + representation.getName() + " mode data connection.");
			//Image Handling
			BufferedImage image = transmissionMode.receiveFile(dataSocket, fos, representation);
			protocol.server.main.lastImage = image;
			//Abspeichern nur wenn Counter mit 1 Initialisiert wurde.
			//Es werden 18 Bilder gespeichert: Alle 10 Sekunden, 3 Minuten lang
			if (protocol.server.imageCounter > 0){
				protocol.server.main.event.storeImage(protocol.server.main.node, image);
				protocol.server.imageCounter = protocol.server.imageCounter + 1;
				if (protocol.server.imageCounter == 19){
					protocol.server.imageCounter = 0;
				}
			}
			reply = protocol.reply(226, "Transfer complete.");
		}
		catch (ConnectException e){
			throw new FtpCommandException(425, "Can't open data connection.");
		}
		catch (IOException e){
			throw new FtpCommandException(550, "Can't write to file");
		}
		finally{
			try{
				if (fos != null){
					fos.close();
				}
				if (dataSocket != null){
					dataSocket.close();
				}
			}
			catch (IOException e){
			}
		}
		return reply;
	}

	/**
	 * Opens data connection, reads file and writes to the data socket.
	 */
	public int sendFile(String path) throws FtpCommandException{
		int reply = 0;
		FileInputStream fis = null;
		Socket dataSocket = null;
		try{
			File file = new File(path);
			if (!file.isFile()){
				throw new FtpCommandException(550, "Not a plain file.");
			}
			fis = new FileInputStream(file);
			if (dataPort == -1){
				throw new FtpCommandException(500, "Can't establish data connection: no PORT specified.");
			}
			dataSocket = new Socket(dataHost, dataPort);
			// Send file contents.
			protocol.reply(150, "Opening " + representation.getName() + " mode data connection.");
			transmissionMode.sendFile(fis, dataSocket, representation);
			reply = protocol.reply(226, "Transfer complete.");
		}catch (FileNotFoundException e){
			throw new FtpCommandException(550, "No such file.");
		}
		catch (ConnectException e){
			throw new FtpCommandException(425, "Can't open data connection.");
		}
		catch (IOException e){
			throw new FtpCommandException(553, "Not a regular file.");
		}
		finally{
			try{
				if (fis != null)
					fis.close();
				if (dataSocket != null)
					dataSocket.close();
			}
			catch (IOException e){
			}
		}
		return reply;
	}

	/**
	 * List of file names to Client.
	 * @param path path of the directory to list.
	 */
	public int sendNameList(String path) throws FtpCommandException{
		int reply = 0;
		Socket dataSocket = null;
		try{
			File dir = new File(path);
			String fileNames[] = dir.list();
			dataSocket = new Socket(dataHost, dataPort);
			Representation representation = Representation.ASCII;
			PrintWriter writer = new PrintWriter(representation.getOutputStream(dataSocket));
			// Send file name list.
			protocol.reply(150, "Opening " + representation.getName() + " mode data connection.");
			for (int i = 0; i < fileNames.length; i++){
				writer.print(fileNames[i]);
				writer.print('\n');
			}
			writer.flush();
			reply = protocol.reply(226, "Transfer complete.");
		}
		catch (ConnectException e){
			throw new FtpCommandException(425, "Can't open data connection.");
		}
		catch (Exception e){
			throw new FtpCommandException(550, "No such directory.");
		}
		finally{
			try{
				if (dataSocket != null)
					dataSocket.close();
			}
			catch (IOException e){
			}
		}
		return reply;
	}

	/**
	 * List of files to Client.
	 * @param path path of the directory.
	 */
	public int sendList(String path) throws FtpCommandException{
		int reply = 0;
		Socket dataSocket = null;
		try{
			File dir = new File(path);
			String fileNames[] = dir.list();
			int numFiles = fileNames != null ? fileNames.length : 0;
			dataSocket = new Socket(dataHost, dataPort);
			Representation representation = Representation.ASCII;
			PrintWriter writer = new PrintWriter(representation.getOutputStream(dataSocket));
			// Send file list.
			protocol.reply(150, "Opening " + representation.getName() + " mode data connection.");
			// Print number of files.
			writer.print("total " + numFiles + "\n");
			// Loop through each file and print its name, size,
			for (int i = 0; i < numFiles; i++){
				String fileName = fileNames[i];
				File file = new File(dir, fileName);
				listFile(file, writer);
			}
			writer.flush();
			reply = protocol.reply(226, "Transfer complete.");
		}
		catch (ConnectException e){
			throw new FtpCommandException(425, "Can't open data connection.");
		}
		catch (Exception e){
			e.printStackTrace();
			throw new FtpCommandException(550, "No such directory.");
		}
		finally{
			try{
				if (dataSocket != null){
					dataSocket.close();
				}
			}
			catch (IOException e){
			}
		}
		return reply;
	}

	/**
	 * Lists file 
	 * @param file file to list.
	 * @param writer writer to print to.
	 */
	private void listFile(File file, PrintWriter writer){
		Date date = new Date(file.lastModified());
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd hh:mm");
		String dateStr = dateFormat.format(date);
		long size = file.length();
		String sizeStr = Long.toString(size);
		int sizePadLength = Math.max(8 - sizeStr.length(), 0);
		String sizeField = pad(sizePadLength) + sizeStr;
		writer.print(file.isDirectory() ? 'd' : '-');
		writer.print("rwxrwxrwx");
		writer.print(" ");
		writer.print("  1");
		writer.print(" ");
		writer.print("ftp     ");
		writer.print(" ");
		writer.print("ftp     ");
		writer.print(" ");
		writer.print(sizeField);
		writer.print(" ");
		writer.print(dateStr);
		writer.print(" ");
		writer.print(file.getName());
		writer.print('\n');
	}

	private static String pad(int length){
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < length; i++)
			buf.append((char)' ');
		return buf.toString();
	}
}
