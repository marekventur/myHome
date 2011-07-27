/**
 * 
 */
package de.wi08e.myhome.nodeplugins.enoceangateway;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.datagram.Datagram;
import de.wi08e.myhome.model.datagram.NodeInformDatagram;
import de.wi08e.myhome.model.datagram.RockerSwitchDatagram;
import de.wi08e.myhome.nodeplugins.NodePluginEvent;

/**
 * @author Marek
 *
 */
public class NetworkRunnable implements Runnable {

	private String idBase;
	public PrintWriter out;
	private BufferedReader in;
	private NodePluginEvent event;
	
	String ip;
	int port;
	
	public NetworkRunnable(String ip, int port, NodePluginEvent event) {
		this.ip = ip;
		this.port = port;
		this.event = event;
	}

	@Override
	public void run() {
		try {
			InetAddress addr = InetAddress.getByName(ip);
		
		    SocketAddress sockaddr = new InetSocketAddress(addr, port);
		    
		    // Create an unbound socket
		    Socket socket = new Socket();
	
		    // This method will block no more than timeoutMs.
		    // If the timeout occurs, SocketTimeoutException is thrown.
		    int timeoutMs = 4000;   // 4 seconds
		    
		    boolean connected = false;
		    int tries = 0;
		    while((!connected) && (tries < 3)) {
		    	try {
		    		System.out.println("ENOCEANGATEWAY: Connecting to gateway at "+ip+":"+String.valueOf(port)+" ("+String.valueOf(tries+1)+")");
		    		
		    		socket.connect(sockaddr, timeoutMs);
		    		connected = true;
		    	} catch (IOException e) {
		    		socket.close();
		    		tries++;
		    	}
		    }
		    
		    if (connected) {
				out = new PrintWriter(socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out.write(GatewayUtilities.constructTelegram("9F02030405060708"));
				out.flush();
				
				char[] buffer = new char[28];
				in.read(buffer);
				
				//out.write("A55A9E01010203040506        ");
				out.write(GatewayUtilities.constructTelegram("9E01010203040506"));
				out.flush();
				
				in.read(buffer);
				
				//out.write("A55A6B0508080808FF6F28000F7 ");
				out.write(GatewayUtilities.constructTelegram("AB58"));
				out.flush();
				in.read(buffer);
				System.out.println("ID_Base:     "+GatewayUtilities.unpackTelegramReadable(buffer));
				idBase = GatewayUtilities.unpackTelegramReadable(buffer);
				
				/*out.write(GatewayUtilities.constructTelegram("AB48"));
				out.flush();
				in.read(buffer);
				System.out.println("Sensitivity: "+GatewayUtilities.unpackTelegramReadable(buffer));
				*/
				
				out.write(GatewayUtilities.constructTelegram("AB4B"));
				out.flush();
				in.read(buffer);
				int[] version = GatewayUtilities.fromString(GatewayUtilities.unpackTelegram(buffer));
				System.out.print("ENOCEANGATEWAY: Gateway version: ");
				System.out.print(version[2]);
				System.out.print(".");
				System.out.print(version[3]);
				System.out.print(".");
				System.out.print(version[4]);
				System.out.print(".");
				System.out.println(version[5]);
				
				GUI gui = new GUI(idBase, this);
				gui.createAndShowGUI();
				
				/* Sending datagram within ID-Range */
				//sendTelegram("FFF6F280", "07070707");
				out.write(GatewayUtilities.constructTelegram(0x05, "07070707", "FFF6F280"));
				out.flush();
				in.read(buffer);
				System.out.println("ENOCEANGATEWAY: Should work: "+GatewayUtilities.unpackTelegramReadable(buffer));
				
				
				/* Sending datagram out of ID-Range */
				
				out.write(GatewayUtilities.constructTelegram  (0x05, "50000000", "001EB85A"));
				out.flush();
				in.read(buffer);
				System.out.println("ENOCEANGATEWAY: Won't work:  "+GatewayUtilities.unpackTelegramReadable(buffer));
		    	
				
				/* Receive datagrams */
				while (in.read(buffer) != -1) {
					
					
					System.out.println(buffer);
					String hseq = ""+buffer[4]+buffer[5];
					String org  = ""+buffer[6]+buffer[7];
					String dataFull = ""+buffer[8]+buffer[9]+buffer[10]+buffer[11]+buffer[12]+buffer[13]+buffer[14]+buffer[15];
					String id = ""+buffer[16]+buffer[17]+buffer[18]+buffer[19]+buffer[20]+buffer[21]+buffer[22]+buffer[23];
					String status = ""+buffer[24]+buffer[25];
					String channel = ""+buffer[26]+buffer[27];
					System.out.println("HSEQ   : "+hseq);
					System.out.println("ORG    : "+org);
					System.out.println("         xx 1234 5678");
					System.out.println("DATA3  : "+buffer[8]+buffer[9] + " "+ GatewayUtilities.charToBit(buffer[8])+" "+GatewayUtilities.charToBit(buffer[9]));
					System.out.println("DATA2  : "+buffer[10]+buffer[11] + " "+ GatewayUtilities.charToBit(buffer[10])+" "+GatewayUtilities.charToBit(buffer[11]));
					System.out.println("DATA1  : "+buffer[12]+buffer[13] + " "+ GatewayUtilities.charToBit(buffer[12])+" "+GatewayUtilities.charToBit(buffer[13]));
					System.out.println("DATA0  : "+buffer[14]+buffer[15] + " "+ GatewayUtilities.charToBit(buffer[14])+" "+GatewayUtilities.charToBit(buffer[15]));
					System.out.println("ID     : "+buffer[16]+buffer[17]+buffer[18]+buffer[19]+buffer[20]+buffer[21]+buffer[22]+buffer[23]);
					System.out.println("STATUS : "+buffer[24]+buffer[25] + " "+ GatewayUtilities.charToBit(buffer[24])+" "+GatewayUtilities.charToBit(buffer[25]));
					System.out.println("CHANNEL: "+buffer[26]+buffer[27] + " "+ GatewayUtilities.charToBit(buffer[26])+" "+GatewayUtilities.charToBit(buffer[27]));
					
					if (hseq.contentEquals("0B") && org.contentEquals("05")) {
						Node sender = new Node("enocean", "Thermokon", id);
						RockerSwitchDatagram.Channel rsChannel = null;
						RockerSwitchDatagram.State rsState = null;
						
						if (buffer[8] == '5') {
							rsChannel = RockerSwitchDatagram.Channel.A;
							rsState = RockerSwitchDatagram.State.ON;
						}
						if (buffer[8] == '7') {
							rsChannel = RockerSwitchDatagram.Channel.A;
							rsState = RockerSwitchDatagram.State.OFF;
						}
						if (buffer[8] == '1') {
							rsChannel = RockerSwitchDatagram.Channel.B;
							rsState = RockerSwitchDatagram.State.ON;
						}
						if (buffer[8] == '3') {
							rsChannel = RockerSwitchDatagram.Channel.B;
							rsState = RockerSwitchDatagram.State.OFF;
						}
							
						RockerSwitchDatagram.Action rsAction = GatewayUtilities.charToBit(buffer[8]).charAt(3)=='1'?RockerSwitchDatagram.Action.PRESSED:RockerSwitchDatagram.Action.RELEASED;
						
						if (rsAction == RockerSwitchDatagram.Action.PRESSED) {
							Datagram datagram = new RockerSwitchDatagram(sender, rsChannel, rsState, rsAction); 
							event.datagrammReceived(datagram);
							
						}
					}
				}
		    }
		    else
		    {
		    	System.err.println("ENOCEANGATEWAY: Can't connect to Enocean Gateway. Terminating process.");
		    	System.exit(0);		    	
		    }
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	boolean sendTelegram(String id, String payload) {
		out.write(GatewayUtilities.constructTelegram(0x05, payload, id));
		out.flush();
		char[] buffer = new char[28];
		try {
			in.read(buffer);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println("ENOCEANGATEWAY: Sended Telegram. Received: "+GatewayUtilities.unpackTelegramReadable(buffer));
		return false;
	}
	
	boolean sendTelegram2(String id, String payload, String status) {
		out.write(GatewayUtilities.constructTelegram(0x05, payload, id, status));
		out.flush();
		return true;
	}
	
	void registerNode(String id, String name) {
		Node node = new Node("enocean", "Thermokon", id);
		Datagram datagram = new NodeInformDatagram(node, name); 
		event.datagrammReceived(datagram);
	}
}
