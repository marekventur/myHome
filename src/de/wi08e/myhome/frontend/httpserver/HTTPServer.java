package de.wi08e.myhome.frontend.httpserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Logger;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.ws.Endpoint;

import de.wi08e.myhome.Config;
import de.wi08e.myhome.ConfigSOAPInterface;
import de.wi08e.myhome.frontend.FrontendInterface;

import com.sun.net.httpserver.*;

/**
 * This starts the webserver at the configured port and host (see config.properties)
 */

public class HTTPServer {
	
	private final static Logger LOGGER = Logger.getLogger(HTTPServer.class.getName());
	
	public HTTPServer(FrontendInterface frontendInterface)  {
		
		
		
		/* Only the first one will be used at the moment */
		ConfigSOAPInterface config = Config.getSoapInterfaces().get(0); 
		
		/* HTTP */
		if (config.getProtocol() == ConfigSOAPInterface.Protocol.HTTP) {
			Endpoint endpoint = Endpoint.create(frontendInterface);
			String uri = "http://"+config.getHost()+":"+config.getPort()+config.getPath();
			endpoint.publish(uri);
			
			LOGGER.info("HTTP for SOAP-communicationis insecure and should only be used during development and testing. Make sure that the network is safe!");	
			LOGGER.info("SOAP server has started. WSDL can be found at "+uri+"?wsdl");			
		}	
		
		/* HTTPS */
		if (config.getProtocol() == ConfigSOAPInterface.Protocol.HTTPS) {
			String uri = "https://"+config.getHost()+":"+config.getPort()+config.getPath();	
			
			try {
				
				/* Load from keyfile */
				KeyStore store = KeyStore.getInstance("JKS");
				store.load(new FileInputStream(new File(config.getKeyFile())),config.getKeyStorePassword().toCharArray());
		
				/* Load (first?) key-pair */
				KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				keyFactory.init(store, config.getKeyPassword().toCharArray());
		
				/* Get TrustManagerFactory */
				TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
				trustFactory.init(store);
		
				/* Create SSLContext */
				SSLContext ssl = SSLContext.getInstance("TLS");
				ssl.init(keyFactory.getKeyManagers(), trustFactory.getTrustManagers(), new SecureRandom());
		
				/* Start HTTPS server */
				HttpsConfigurator configurator = new HttpsConfigurator(ssl);
				HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress(config.getHost(), config.getPort()), config.getPort());
				httpsServer.setHttpsConfigurator(configurator);
				httpsServer.start();
		
				/* Publish Endpoint */
				HttpContext httpContext = httpsServer.createContext(config.getPath());
				Endpoint endpoint = Endpoint.create(frontendInterface);
				endpoint.publish(httpContext);
				
				LOGGER.info("SOAP server has started. WSDL can be found at "+uri+"?wsdl");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (KeyStoreException e) {
				LOGGER.warning("KeyStoreException: KeyStore corrupted or passwords wrong");
				e.printStackTrace();
			} catch (CertificateException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				LOGGER.warning("KeyStore not found");
				e.printStackTrace();
			} catch (IOException e) {
				LOGGER.warning("Can't load KeyStore");
				e.printStackTrace();
			} catch (UnrecoverableKeyException e) {
				e.printStackTrace();
			} catch (KeyManagementException e) {
				e.printStackTrace();
			}
		}
		
		
		
		
		
	}	
}
