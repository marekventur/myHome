/**
 * 
 */
package de.wi08e.myhome;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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
	static private SimpleFormatter formatterTxt;


	static public void setup() {
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
		formatterTxt = new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);
		logger.addHandler(fileTxt);
	}
}
