/**
 * 
 */
package de.wi08e.myhome.nodeplugins.notifo;

import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


import org.apache.commons.codec.binary.Base64;

/**
 * @author Marek
 * 
 */

public class NotifoHTTPClient {
	private static final String apiRoot = "https://api.notifo.com/v1/";
	private static final String method = "send_notification";

	public void sendMessage(String username, String apiSecret, String label, String title, String msg, String uri) {
		try {
			URL url = new URL(apiRoot + method);
	
			URLConnection connection = url.openConnection();
	
			String authorizationString = "Basic "
					+ new String(Base64.encodeBase64((username + ":" + apiSecret).getBytes()));
			connection.setRequestProperty("Authorization", authorizationString);
	
			connection.setDoOutput(true);
			OutputStreamWriter output = new OutputStreamWriter(
					connection.getOutputStream());
			output.write("to=m");
			output.write("&msg="+URLEncoder.encode(msg,"UTF8"));
			output.write("&uri="+URLEncoder.encode(uri,"UTF8"));
			output.write("&label="+URLEncoder.encode(label,"UTF8"));
			output.write("&title="+URLEncoder.encode(title,"UTF8"));
	
			output.flush();
			output.close();
	
			connection.getInputStream().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {

		String label = "label";
		String title = "title";
		String msg = "msg";
		String uri = "http://www.google.com";
		
		new NotifoHTTPClient().sendMessage("user", "password", label, title, msg, uri);

	}

}
