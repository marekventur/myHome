package de.wi08e.myhome.nodeplugins.camera;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * This Implements FTP Command Interpretation. For each Connection one is opened
 * as a new Thread.
 */
public class FtpServerProtocol implements Runnable{
	/**
	 * Client who opened the connection.
	 */
	private Socket clientSocket;

	/**
	 * Reader for client socket commands.
	 */
	private BufferedReader reader;

	/**
	 * Writer for replies to client socket.
	 */
	private PrintWriter writer;

	/**
	 * Data transfer invoked to transfer files to and from the user.
	 */
	private FtpServerData data;

	/**
	 * Reflection-API used to invoke a handler for a given string
	 * command.
	 */
	
	private Class commandHandlerArgTypes[] = { String.class, StringTokenizer.class };

	/**
	 * Username of client.
	 */
	private String username;

	/**
	 * Password of client.
	 */
	private String password;

	/**
	 * Root directory for client.
	 */
	private final String baseDir = System.getProperty("user.dir");

	/**
	 * The current directory for client.
	 */
	private String currentDir = "/";

	/**
	 * Creates ServerProtocol for client socket for reading and writing Commands.
	 */
	public FtpServerProtocol(Socket clientSocket)throws IOException{
		this.clientSocket = clientSocket;
		reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);

		data= new FtpServerData(this);
		}

	public void run(){
		try{
			clientLoop();
		}catch (Exception e){
			e.printStackTrace();
			System.out.println("Error in FtpServerProtocol.clientloop()");
			}
		finally{
			try{
				clientSocket.close();
			}catch (Exception e){
				e.printStackTrace();
				}
			}
		}

	/**
	 * Loop for input from the client socket. Reads commands using Reflection-API
	 * to invoke handler method for command.
	 */
	private void clientLoop()throws Exception{
		reply(220, "localhost Image FTP server ready to use.");
		String line = null;
		while ((line = reader.readLine()) != null){
			StringTokenizer st = new StringTokenizer(line);
			String command = st.nextToken().toLowerCase();
			Object args[] = { line, st };
			try{
				Method commandHandler = getClass().getMethod("ftp_" + command, commandHandlerArgTypes);
				/**
				 * Test if exit command received (Code 221)
				 */
				int code = ((Integer)commandHandler.invoke(this, args)).intValue();
				if (code == 221)
					return;
			}
			catch (InvocationTargetException e){
				try{
					throw (Exception)e.getTargetException();
				}
				catch (FtpCommandException ftpe){
					reply(ftpe.getCode(), ftpe.getText());
				}
				catch (NoSuchElementException e1){
					reply(500, "'" + line + "': command not understood.");
				}
			}
			catch (NoSuchMethodException e){
				reply(500, "'" + line + "': command not understood.");
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * USER command
	 */
	public int ftp_user(String line, StringTokenizer st)throws FtpCommandException{
		username = st.nextToken();
		return reply(331, "Enter Password for " + username + ".");
	}

	/**
	 * PASS command. Only after USER command 
	 */
	public int ftp_pass(String line, StringTokenizer st)throws FtpCommandException{
		if (username == null){
			throw new FtpCommandException(503, "Login with USER command first.");
		}
		String password = null;
		if (st.hasMoreTokens())
			password = st.nextToken();
		else
			password = "";
		this.password = password;
		return reply(230, "User " + username + " logged in.");
		}

	/**
	 * CWD command.
	 */
	public int ftp_cwd(String line, StringTokenizer st)throws FtpCommandException{
		checkLogin();
		String arg = st.nextToken();
		String newDir = arg;
		if (newDir.length() == 0){
			newDir = "/";
		}
		newDir = resolvePath(newDir);
		File file = new File(createAbsolutePath(newDir));
		if (!file.exists())
			throw new FtpCommandException(550, arg + ": no such directory");
		if (!file.isDirectory())
			throw new FtpCommandException(550, arg + ": not a directory");
		currentDir = newDir;
		return reply(250, "CWD command successful.");
		}

	/**
	 * CDUP command.
	 */
	public int ftp_cdup(String line, StringTokenizer st) throws FtpCommandException{
		return ftp_cwd(line, st);
	}

	/**
	 * QUIT command.
	 */
	public int ftp_quit(String line, StringTokenizer st)throws FtpCommandException{
		username = null;
		password = null;
		return reply(221, "Goodbye.");
	}

	/**
	 * PORT command.
	 */
	public int ftp_port(String line, StringTokenizer st)throws FtpCommandException{
		checkLogin();
		String portStr = st.nextToken();
		st = new StringTokenizer(portStr, ",");
		String h1 = st.nextToken();
		String h2 = st.nextToken();
		String h3 = st.nextToken();
		String h4 = st.nextToken();
		int p1 = Integer.parseInt(st.nextToken());
		int p2 = Integer.parseInt(st.nextToken());
		String dataHost = h1 + "." + h2 + "." + h3 + "." + h4;
		int dataPort = p1*16*16+p2;
		data.setDataPort(dataHost, dataPort);
		return reply(200, "PORT command successful.");
	}

	/**
	 * PASV command.
	 */
	public int ftp_pasv(String line, StringTokenizer st)throws FtpCommandException{
		checkLogin();
		throw new FtpCommandException(500, "'" + line + "': command not supported.");
	}

	/**
	 * TYPE command. 
	 * Supported arguments are 'A' for ASCII and 'I' for IMAGE.
	 */
	public int ftp_type(String line, StringTokenizer st)throws FtpCommandException{
		checkLogin();
		String arg = st.nextToken().toUpperCase();
		if (arg.length() != 1){
			throw new FtpCommandException(500, "TYPE: invalid argument '" + arg + "'");
		}
		char code = arg.charAt(0);
		Representation representation = Representation.get(code);
		if (representation == null){
			throw new FtpCommandException(500, "TYPE: invalid argument '" + arg + "'");
		}
		data.setRepresentation(representation);
		return reply(200, "Type set to " + arg);
	}

	/**
	 * RETR command.
	 */
	public int ftp_retr(String line, StringTokenizer st) throws FtpCommandException{
		checkLogin();
		String path = null;
		try{
			path = line.substring(5);
		}
		catch (Exception e){
			throw new NoSuchElementException(e.getMessage());
		}
		path = createAbsolutePath(path);
		return data.sendFile(path);
	}

	/**
	 * STOR command.
	 */
	public int ftp_stor(String line, StringTokenizer st)throws FtpCommandException{
		checkLogin();
		String path = null;
		try{
			path = line.substring(5);
		}
		catch (Exception e){
			throw new NoSuchElementException(e.getMessage());
		}
		path = createAbsolutePath(path);
		return data.receiveFile(path);
	}

	/**
	 * DELE command.
	 */
	public int ftp_dele(String line, StringTokenizer st) throws FtpCommandException{
		checkLogin();
		String arg = st.nextToken();
		String filePath = resolvePath(arg);
		File file = new File(createAbsolutePath(filePath));
		if (!file.exists()){
			throw new FtpCommandException(550, arg + ": file does not exist");
		}
		if (!file.delete()){
			throw new FtpCommandException(550, arg + ": could not delete file");
		}
		return reply(250, "DELE command successful.");
	}

	/**
	 * RMD command.
	 */
	public int ftp_rmd(String line, StringTokenizer st) throws FtpCommandException{
		checkLogin();
		String arg = st.nextToken();
		String dirPath = resolvePath(arg);
		File dir = new File(createAbsolutePath(dirPath));
		if (!dir.exists())
			throw new FtpCommandException(550, arg + ": directory does not exist");
		if (!dir.isDirectory())
			throw new FtpCommandException(550, arg + ": not a directory");
		if (!dir.delete())
			throw new FtpCommandException(550, arg + ": could not remove directory");
		return reply(250, "RMD command successful.");
	}

	/**
	 * MKD command.
	 */
	public int ftp_mkd(String line, StringTokenizer st) throws FtpCommandException{
		checkLogin();
		String arg = st.nextToken();
		String dirPath = resolvePath(arg);
		File dir = new File(createAbsolutePath(dirPath));
		if (dir.exists())
			throw new FtpCommandException(550, arg + ": file exists");
		if (!dir.mkdir())
			throw new FtpCommandException(550, arg + ": directory could not be created");
		return reply(257, "\"" + dirPath + "\" directory created");
	}

	/**
	 * PWD command.
	 */
	public int ftp_pwd(String line, StringTokenizer st) throws FtpCommandException{
		checkLogin();
		return reply(257, currentDir);
	}

	/**
	 * LIST command.
	 */
	public int ftp_list(String line, StringTokenizer st) throws FtpCommandException{
		checkLogin();
		String path = null;
		if (st.hasMoreTokens()){
			path = st.nextToken();
		}else{
			path = currentDir;
		}
		path = createAbsolutePath(path);
		return data.sendList(path);
	}

	/**
	 * NLST command.
	 */
	public int ftp_nlst(String line, StringTokenizer st) throws FtpCommandException{
		checkLogin();
		String path = null;
		if (st.hasMoreTokens()){
			path = st.nextToken();
		}else{
			path = currentDir;
		}
		path = createAbsolutePath(path);
		return data.sendNameList(path);
	}


	/**
	 * SYST command.
	 */
	public int ftp_syst(String line, StringTokenizer st) throws FtpCommandException{
		checkLogin();
		return reply(215, "Java My Home Image Ftp Server");
		}

	/**
	 * SIZE command.
	 */
	public int ftp_size(String line, StringTokenizer st) throws FtpCommandException{
		checkLogin();
		String arg = st.nextToken();
		String path = resolvePath(arg);
		File file = new File(createAbsolutePath(path));
		if (!file.exists())
			throw new FtpCommandException(550, arg + ": no such file");
		if (!file.isFile())
			throw new FtpCommandException(550, arg + ": not a file");

		Representation representation = data.getRepresentation();
		long size;
		try{
			size = representation.sizeOf(file);
		}
		catch (IOException e){
			throw new FtpCommandException(550, e.getMessage());
		}
		return reply(213, "" + size);
	}

	/**
	 * MDTM command.
	 */
	public int ftp_mdtm(String line, StringTokenizer st) throws FtpCommandException{
		checkLogin();
		String arg = st.nextToken();
		String path = resolvePath(arg);
		File file = new File(createAbsolutePath(path));
		if (!file.exists())
			throw new FtpCommandException(550, arg + ": no such file");
		Date date = new Date(file.lastModified());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		String dateStr = dateFormat.format(date);
		return reply(213, dateStr);
	}

	/**
	 * Writes reply to client socket.
	 * @param code reply code.
	 * @param text string message in reply.
	 * @return reply code for reply.
	 */
	protected int reply(int code, String text)
		{
		writer.println(code + " " + text);
		return code;
	}

	/**
	 * Creates absolute path from a path string sent from the
	 * client on the server. Root is baseDir.
	 */
	protected String createAbsolutePath(String ftpPath){
		String path = null;
		if (ftpPath.charAt(0) == '/'){
			path = baseDir + ftpPath;
		}else{
			path = baseDir + currentDir + "/" + ftpPath;
		}
		return path;
	}

	/**
	 * Resolves Path given by the client.'.' path segments are removed.
	 * '..' path segments stack the the previous segment of the path.
	 */
	protected String resolvePath(String path){
		if (path.charAt(0) != '/'){
			path = currentDir + "/" + path;
		}
		StringTokenizer pathSt = new StringTokenizer(path, "/");
		Stack segments = new Stack();
		while (pathSt.hasMoreTokens()){
			String segment = pathSt.nextToken();
			if(segment.equals("..")){
				if (!segments.empty()){
					segments.pop();
				}
			}
			else if (segment.equals(".")){
				// do nothing
				}else{
					segments.push(segment);
				}
		}
		StringBuffer pathBuf = new StringBuffer("/");
		Enumeration segmentsEn = segments.elements();
		while (segmentsEn.hasMoreElements()){
			pathBuf.append(segmentsEn.nextElement());
			if(segmentsEn.hasMoreElements()){
				pathBuf.append("/");
			}
		}
		return pathBuf.toString();
	}

	void checkLogin()throws FtpCommandException{
		if (password == null){
			throw new FtpCommandException(530, "Please login with USER and PASS.");
		}
	}
	
	/**
	 * 
	 */
	public String getBaseDir(){
		return this.baseDir;
	}
	public String getCurrentDir(){
		return this.currentDir;
	}
}
