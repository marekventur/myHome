/**
 * 
 */
package de.wi08e.myhome.scriptmanager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import de.wi08e.myhome.communicationplugins.CommunicationManager;
import de.wi08e.myhome.database.Database;
import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.datagram.BroadcastDatagram;
import de.wi08e.myhome.model.datagram.Datagram;
import de.wi08e.myhome.model.datagram.StatusDatagram;
import de.wi08e.myhome.nodemanager.DatagramReceiver;
import de.wi08e.myhome.nodemanager.NodeManager;
import de.wi08e.myhome.nodeplugins.NodePluginManager;
import de.wi08e.myhome.statusmanager.StatusManager;
import de.wi08e.myhome.usermanager.UserManager;

/**
 * @author Marek
 *
 */
public class ScriptManager implements Runnable, DatagramReceiver {

	private Database database;
	private NodeManager nodeManager;
	private UserManager userManager;
	private StatusManager statusManager;
	private CommunicationManager communicationManager;
	
	private ScriptingUsers scriptingUsers;
	private ScriptingNodes scriptingNodes;
	
	private BlockingQueue<TriggeringEvent> triggeringEvents = new LinkedBlockingQueue<TriggeringEvent>();

	private boolean running = true;
	
	
	public ScriptManager(Database database, NodeManager nodeManager, UserManager userManager, CommunicationManager communicationManager, StatusManager statusManager) {
		this.database = database;
		this.nodeManager = nodeManager;
		this.communicationManager = communicationManager;
		this.userManager = userManager;
		this.statusManager = statusManager;
		scriptingUsers = new ScriptingUsers(userManager, communicationManager);
		scriptingNodes = new ScriptingNodes(nodeManager, statusManager);
	}
	
	@Override
	public void run() {
		while(running) {
			try {
				TriggeringEvent triggeringEvent = triggeringEvents.take();
				
				// Is it a datagram event?
				if (triggeringEvent instanceof TriggeringEventDatagram) {
					Datagram datagram = ((TriggeringEventDatagram)triggeringEvent).getDatagram();
					if (datagram instanceof BroadcastDatagram) {
						Node sender = ((BroadcastDatagram)datagram).getSender();
						
						// Is there a script waiting for this trigger?
						List<Script> scripts = getScriptBySenderNode(sender.getDatabaseId());
						for (Script script: scripts) {
							
							ScriptEngine engine = createNewEngine();
							engine.put("sender", new ScriptingNode(sender, statusManager));
							engine.put("datagram", new ScriptingDatagram(datagram));
							
					        try {
								engine.eval(script.getScript());
							} catch (ScriptException e) {
								e.printStackTrace();
							}
						}
					}	
				}
				
				// Is it a status-change event?
				if (triggeringEvent instanceof TriggeringEventStatusChange) {
					TriggeringEventStatusChange event = ((TriggeringEventStatusChange)triggeringEvent);
					Node node = event.getNode();
					String key = event.getKey();
					String value = event.getValue();
	
					// Is there a script waiting for this trigger?
					List<Script> scripts = getScriptByStatusChangeNode(node.getDatabaseId());
					for (Script script: scripts) {
						
						ScriptEngine engine = createNewEngine();
						engine.put("node", new ScriptingNode(node, statusManager));
						engine.put("key", key);
						engine.put("value", value);
						
				        try {
							engine.eval(script.getScript());
						} catch (ScriptException e) {
							e.printStackTrace();
						}
					}
					
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}		
	}


	@Override
	public void receiveBroadcastDatagram(BroadcastDatagram datagram) {
		triggeringEvents.add(new TriggeringEventDatagram(datagram));		
	}
	
	public void receiveStatusChange(Node node, String key, String value) {
		triggeringEvents.add(new TriggeringEventStatusChange(node, key, value));	
	}
	
	private List<Script> getScriptBySenderNode(int node) {
		List<Script> result = new ArrayList<Script>();
		
		try {
			PreparedStatement getNodeStatus = database.getConnection().prepareStatement("SELECT id, name, script FROM script WHERE sender_node_id = ?"); 
			getNodeStatus.setInt(1, node);
			getNodeStatus.execute();
			
			ResultSet rs = getNodeStatus.getResultSet();
			while (rs.next()) 
				result.add(new Script(rs));

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	private List<Script> getScriptByStatusChangeNode(int node) {
		List<Script> result = new ArrayList<Script>();
		
		try {
			PreparedStatement getNodeStatus = database.getConnection().prepareStatement("SELECT id, name, script FROM script WHERE status_change_node_id = ?"); 
			getNodeStatus.setInt(1, node);
			getNodeStatus.execute();
			
			ResultSet rs = getNodeStatus.getResultSet();
			while (rs.next()) 
				result.add(new Script(rs));

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	private ScriptEngine createNewEngine() {
		ScriptEngineManager factory = new ScriptEngineManager();
	    ScriptEngine engine = factory.getEngineByName("JavaScript");
	    
	    engine.put("Users", scriptingUsers);
	    engine.put("Nodes", scriptingNodes);
	    engine.put("System", new ScriptingSystem(engine));
	    
		return engine;
	    
	}

	@Override
	public void receiveStatusDatagram(StatusDatagram datagram) {
		// Don't really know (now) what to call here... 		
	}
}
