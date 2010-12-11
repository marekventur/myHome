package de.wi08e.myhome;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Properties;

public class Config {
	
	private static Properties properties;
	
	static {
		properties = new Properties();
		BufferedInputStream stream;
		try {
			stream = new BufferedInputStream(new FileInputStream("config.properties"));
			properties.load(stream);
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 	
	}
	
	public static String getProperty(String key) {
		return properties.getProperty(key, "");
	}
	
	public static boolean hasProperty(String key) {
		return properties.containsKey(key);
	}
}
