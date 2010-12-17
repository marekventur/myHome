package de.wi08e.myhome;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Config {
	
	public enum DatabaseType {MYSQL};
	
	private static String logFile = "log.txt";
	
	private static DatabaseType databaseType = DatabaseType.MYSQL;
	private static String databaseHost = "localhost";
	private static int databasePort = 3306;
	private static String databaseUser;
	private static String databasePassword;
	private static String databaseName;
	
	private static ArrayList<ConfigSOAPInterface> soapInterfaces = new ArrayList<ConfigSOAPInterface>();
	private static ArrayList<ConfigPlugin> plugins = new ArrayList<ConfigPlugin>();
	
	/**
	 * Loads config data from a xml file. 
	 * This should be the first method called in the whole program.
	 * @param filename
	 * @return 
	 * @return true when successful, false otherwise
	 */
	public static void initiate(String filename) throws Exception {
		
		File file = new File(filename);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();
		
		/* The xml-file _should_ be well formated, but checking first can't harm */
		if (!doc.getDocumentElement().getNodeName().contentEquals("config")) throw new Exception("Invalid config XML");
		
		/* Logger output file */
		NodeList logings = doc.getElementsByTagName("loging");
		if (logings.getLength() > 0) {
			Node loging = logings.item(0);
			if (loging.getAttributes().getNamedItem("filename") != null) 
				logFile = loging.getAttributes().getNamedItem("filename").getNodeValue();
		}
		
		
		/* Database */
		NodeList databases = doc.getElementsByTagName("database");
		if (databases.getLength() != 1) throw new Exception("There has to be exactly 1 database tag");
		Node database = databases.item(0);
		if (database.getAttributes().getNamedItem("host") != null) 
			databaseHost = database.getAttributes().getNamedItem("host").getNodeValue();
		if (database.getAttributes().getNamedItem("port") != null) 
			databasePort = Integer.parseInt(database.getAttributes().getNamedItem("port").getNodeValue());
		if (database.getAttributes().getNamedItem("user") == null) 
			throw new Exception("No database user found");
		databaseUser = database.getAttributes().getNamedItem("user").getNodeValue();
		if (database.getAttributes().getNamedItem("password") != null) 
			databasePassword = database.getAttributes().getNamedItem("password").getNodeValue();
		if (database.getAttributes().getNamedItem("database") == null) 
			throw new Exception("No database name found");
		databaseName = database.getAttributes().getNamedItem("database").getNodeValue();
		
		
		/* SOAP Interface */
		NodeList soapInterfacesXML = doc.getElementsByTagName("soapinterface");
		if (soapInterfacesXML.getLength() != 1) throw new Exception("There has to be exactly 1 soapinterface tag");
		Node soapInterface = soapInterfacesXML.item(0);
		if (soapInterface.getNodeType() == Node.ELEMENT_NODE) {
			NodeList bindings =  ((Element)soapInterface).getElementsByTagName("binding");
			for (int i=0; i<bindings.getLength(); i++ ) {
				Node binding = bindings.item(i);
				String host = "localhost";
				int port = 8888;
				if (binding.getAttributes().getNamedItem("host") != null) 
					host = binding.getAttributes().getNamedItem("host").getNodeValue();
				if (binding.getAttributes().getNamedItem("port") != null) 
					port = Integer.parseInt(binding.getAttributes().getNamedItem("port").getNodeValue());
				soapInterfaces.add(new ConfigSOAPInterface(ConfigSOAPInterface.Protocol.HTTP, host, port));
			}
		}
		if (soapInterfaces.size() < 1) throw new Exception("There has to be at least one SOAPInterface binding");
		
		
		/* Plugins */
		NodeList nodePluginsXML = doc.getElementsByTagName("nodeplugins");
		if (nodePluginsXML.getLength() > 0) {
			Node nodePluginTop = nodePluginsXML.item(0);
			if (nodePluginTop.getNodeType() == Node.ELEMENT_NODE) {
				NodeList nodePlugins =  ((Element)nodePluginTop).getElementsByTagName("nodeplugin");
				for (int i=0; i<nodePlugins.getLength(); i++ ) {
					Node nodePlugin = nodePlugins.item(i);
					String namespace;
					String classname;
					Node data = null;
					HashMap<String, String> properties = new HashMap<String, String>();
					
					/* namespace & classname */
					if (nodePlugin.getAttributes().getNamedItem("namespace") == null)
						throw new Exception("No namespace found for a plugin");
					namespace = nodePlugin.getAttributes().getNamedItem("namespace").getNodeValue();
					if (nodePlugin.getAttributes().getNamedItem("classname") == null) 
						throw new Exception("No classname found for a plugin");
					classname = nodePlugin.getAttributes().getNamedItem("classname").getNodeValue();
					
					/* parameters */
					NodeList parameters = ((Element)nodePluginTop).getElementsByTagName("parameter");
					for (int j=0; j<parameters.getLength(); j++ ) {
						Node parameter = parameters.item(j);
						
						if (parameter.getNodeType() == Node.ELEMENT_NODE) {
							NodeList keyNodes = ((Element) parameter).getElementsByTagName("key");
							NodeList valueNodes = ((Element) parameter).getElementsByTagName("value");
							if ((keyNodes.getLength() == 1) && (valueNodes.getLength() == 1)) 
								properties.put(keyNodes.item(0).getChildNodes().item(0).getNodeValue().toString(), valueNodes.item(0).getChildNodes().item(0).getNodeValue().toString());
						}
						
					}
					
					/* data tag */
					NodeList datas = ((Element)nodePluginTop).getElementsByTagName("data");
					if (datas.getLength() > 1)
						throw new Exception("Too much data tags for one plugin found");	
					if (datas.getLength() == 1)
						data = datas.item(0);
										
					plugins.add(new ConfigPlugin(namespace, classname, properties, data));
				}
			}
		}	
		
		
	}

	public static String getLogFile() {
		return logFile;
	}

	public static DatabaseType getDatabaseType() {
		return databaseType;
	}

	public static String getDatabaseHost() {
		return databaseHost;
	}

	public static int getDatabasePort() {
		return databasePort;
	}

	public static String getDatabaseUser() {
		return databaseUser;
	}

	public static String getDatabasePassword() {
		return databasePassword;
	}

	public static String getDatabaseName() {
		return databaseName;
	}

	public static ArrayList<ConfigSOAPInterface> getSoapInterfaces() {
		return soapInterfaces;
	}

	public static ArrayList<ConfigPlugin> getPlugins() {
		return plugins;
	}

	
	
}
