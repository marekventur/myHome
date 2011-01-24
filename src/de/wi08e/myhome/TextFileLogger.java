/**
 * 
 */
package de.wi08e.myhome;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * This is adapted from http://www.javalobby.org/java/forums/t18515.html
 * 
 * @author Marek
 *
 */
/*
class BriefLogFormatter extends Formatter {
	
	private static final DateFormat format = new SimpleDateFormat("h:mm:ss");
	private static final String lineSep = System.getProperty("line.separator");
	/**
	 * A Custom format implementation that is designed for brevity.
	 */
/*
	public String format(LogRecord record) {
		String loggerName = record.getLoggerName();
		if(loggerName == null) {
			loggerName = "root";
		}
		StringBuilder output = new StringBuilder()
			.append(loggerName)
			.append("[")
			.append(record.getLevel()).append('|')
			.append(Thread.currentThread().getName()).append('|')
			.append(format.format(new Date(record.getMillis())))
			.append("]: ")
			.append(record.getMessage()).append(' ')
			.append(lineSep);
		return output.toString();		
	}
 
}
*/
/**
 * The project-wide logger class. 
 * 
 * Concept from: http://www.vogella.de/articles/Logging/article.html
 * 
 * @author Marek
 * 
 */
public class TextFileLogger {
	static private FileHandler fileTxt;
	static private Formatter formatterTxt;


	static public void setup() {
		
		/* Filter ExceptionBeanClass logs */
		Logger loggerInfo = Logger.getLogger("com.sun.xml.internal.ws.model.RuntimeModeler");
		loggerInfo.setLevel(Level.WARNING);
		loggerInfo.setFilter(new Filter() {

			@Override
			public boolean isLoggable(LogRecord l) {
				System.out.println(l.getMessage());
				if (l.getMessage().startsWith("Dynamically creating exception bean Class"))
					return false;
				return true;
			}
			
		});
		
		// Create Logger
		Logger logger = Logger.getLogger("");
		logger.setLevel(Level.INFO);
				
		
		try {
			fileTxt = new FileHandler(Config.getLogFile());
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create txt Formatter
		formatterTxt = /*new BriefLogFormatter(); // */new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);
		
		logger.addHandler(fileTxt);
	}
}
