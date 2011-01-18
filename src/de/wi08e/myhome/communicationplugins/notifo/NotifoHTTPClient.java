/**
 * 
 */
package de.wi08e.myhome.communicationplugins.notifo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.net.HttpURLConnection;
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
	private String username;
	private String apiSecret;
	
	public NotifoHTTPClient(String username, String apiSecret) {
		this.username = username;
		this.apiSecret = apiSecret;
	}
	
	public void subscribeUser(String subscriber) {
		try {
			URL url = new URL(apiRoot + "subscribe_user");
	
			URLConnection connection = url.openConnection();
	
			String authorizationString = "Basic "
					+ new String(Base64.encodeBase64((username + ":" + apiSecret).getBytes()));
			connection.setRequestProperty("Authorization", authorizationString);
	
			connection.setDoOutput(true);
			OutputStreamWriter output = new OutputStreamWriter(
					connection.getOutputStream());
			output.write("username="+subscriber);
	
			output.flush();
			output.close();
			
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			/*String inputLine;
	        while ((inputLine = inputStream.readLine()) != null) 
	            System.out.println(inputLine);*/
	        inputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendNotification(String to, String title, String msg, String uri)  {
		
		HttpURLConnection connection = null;
		
		try {
			
			URL url = new URL(apiRoot + "send_notification");
			connection = (HttpURLConnection)url.openConnection();
			
			String authorizationString = "Basic "
					+ new String(Base64.encodeBase64((username + ":" + apiSecret).getBytes()));
			connection.setRequestProperty("Authorization", authorizationString);
	
			connection.setDoOutput(true);
			OutputStreamWriter output = new OutputStreamWriter(
					connection.getOutputStream());
			output.write("to="+to);
			output.write("&msg="+URLEncoder.encode(msg,"UTF8"));
			output.write("&uri="+URLEncoder.encode(uri,"UTF8"));
			output.write("&title="+URLEncoder.encode(title,"UTF8"));
	
			output.flush();
			output.close();
			
			
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			/*String inputLine;
	        while ((inputLine = inputStream.readLine()) != null) 
	            System.out.println(inputLine);*/
	        inputStream.close();

		} catch (Exception e) {
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
			
	        try {
	        	String readLine;
	        	String result = "";
				while ((readLine = inputStream.readLine()) != null) 
				    result += readLine + "\n";

				if(result.contains("\"response_code\":1102")) {
					subscribeUser(to);
				}
			} catch (IOException e2) {
				e2.printStackTrace();
			}	
		}
	}
	

}
