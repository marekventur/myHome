
package de.wi08e.myhome.nodeplugins.camera;

/**
 * @author Nico
 * some ideas and code snipes from http://www.ryanheise.com/software/jftpd/
 * and http://www.echo.nuee.nagoya-u.ac.jp/~tanaka/MyFtpd.java
 * FTP Commands according to Common Commands on"http://www.nsftools.com/tips/RawFTP.htm"
 * and rfc959.
 * Exception thrown by error in command handling.
 */
public class FtpCommandException extends Exception{

	private static final long serialVersionUID = 1L;

	/**
	 * Reply code to send to the user.
	 */
	private int code;

	/**
	 * Text message with the reply code.
	 */
	private String text;

	/**
	 * Constructor for new CommandException.
	 *
	 * @param code reply code.
	 * @param text error message.
	 */
	public FtpCommandException(int code, String text){
		super(code + " " + text);
		this.code = code;
		this.text = text;
	}

	/**
	 * Code getter
	 * @return reply code.
	 */
	public int getCode(){
		return code;
	}

	/**
	 * Text getter
	 * @return reply text.
	 */
	public String getText(){
		return text;
	}

	/**
	 * Message in Contructor may be returned by Throwable.getMessage().
	 */
	public String getMessage(){
		return code + " " + text;
	}
}
