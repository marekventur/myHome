package de.wi08e.myhome.statusmanager;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.wi08e.myhome.database.Database;
import de.wi08e.myhome.model.NamedNode;
import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.Trigger;
import de.wi08e.myhome.model.datagram.BroadcastDatagram;
import de.wi08e.myhome.model.datagram.Datagram;
import de.wi08e.myhome.myhomescript.ScriptingEngine;
import de.wi08e.myhome.nodemanager.DatagramReceiver;
import de.wi08e.myhome.nodemanager.NodeManager;
import de.wi08e.myhome.nodeplugins.NodePluginManager;

public class StatusManager implements DatagramReceiver{
	
	private Database database;
	private ScriptingEngine scriptingEngine;
	
	private TriggerManager triggerManager;
	private NodeManager nodeManager;
	
	private List<SpecializedStatusManager> specializedStatusManagers = new ArrayList<SpecializedStatusManager>();
	
	public StatusManager(Database database, NodeManager nodeManager,
			ScriptingEngine scriptingEngine) {
		super();
		this.database = database;
		this.nodeManager = nodeManager;
		this.scriptingEngine = scriptingEngine;
		
		// Add StatusManager
		specializedStatusManagers.add(new RockerSwitchStatusManager(this));
		
		triggerManager = new TriggerManager(database, nodeManager);
		
	}

	public void receiveBroadcastDatagram(BroadcastDatagram broadcastDatagram) {
		
		
		String type = null;
		
		try {
			// Send to all specialized StatusManagers
			for (SpecializedStatusManager statusManager: specializedStatusManagers) {
				String returnType = statusManager.handleBroadcastDatagram(broadcastDatagram);
				if (returnType != null) {
					
					if (type == null) {
						type = returnType;
						
						// Write type to DB
						PreparedStatement insertNode = database.getConnection().prepareStatement("UPDATE node SET type = ? WHERE id = ?;");
						
						insertNode.setInt(2, broadcastDatagram.getSender().getDatabaseId());
						insertNode.setString(1, type);
						
						insertNode.executeUpdate();
					}
					break;
				}
			}
			
			

			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	protected void writeStatusChangeToDatabase(int id, String key, String value) {
		try {
			// is there already a status?
			PreparedStatement getNodeStatus = database.getConnection().prepareStatement("SELECT value FROM node_status WHERE node_id=? and `key`=?;"); 
			getNodeStatus.setInt(1, id);
			getNodeStatus.setString(2, key);
			getNodeStatus.execute();
			
			ResultSet rs = getNodeStatus.getResultSet();
			if (rs.next()) {
				// Yep, it does
				String oldValue = rs.getString("value");
				
				// is there a change?
				if(!oldValue.contentEquals(value)) {
					PreparedStatement updateNode = database.getConnection().prepareStatement("UPDATE node_status SET value = ? WHERE node_id = ? AND `key` = ?;");
					
					updateNode.setString(1, value);
					updateNode.setInt(2, id);
					updateNode.setString(3, key);
					
					updateNode.executeUpdate();
				}
				
			}
			else
			{
				// No. Create it!
				PreparedStatement insertNode = database.getConnection().prepareStatement("INSERT INTO node_status (value, node_id, `key`) VALUES (?, ?, ?);");
				
				insertNode.setString(1, value);
				insertNode.setInt(2, id);
				insertNode.setString(3, key);
				
				insertNode.executeUpdate();
			}
			
			// Create an history
			PreparedStatement insertHistoryNode = database.getConnection().prepareStatement("INSERT INTO node_status_history (value, node_id, `key`) VALUES (?, ?, ?);");
			
			insertHistoryNode.setString(1, value);
			insertHistoryNode.setInt(2, id);
			insertHistoryNode.setString(3, key);
			
			insertHistoryNode.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
	}

	public TriggerManager getTriggerManager() {
		return triggerManager;
	}

	public NodeManager getNodeManager() {
		return nodeManager;
	}

	/**
	 * Change status of this node by sending an datagram that triggers the least other nodes
	 * @param receiver
	 * @param value
	 * @param key
	 * @return all changed nodes, null when not successfull
	 * @throws InvalidStatusValueException 
	 */
	public List<Node> setStatus(Node receiver, String key, String value) throws InvalidStatusValueException {
		
		// Is the node already in the right status
		if (receiver.getStatus().containsKey(key) && receiver.getStatus().get(key).contentEquals(value))
			return new ArrayList<Node>();
		
		try {
			Statement getBestSender = database.getConnection().createStatement();
			String getBestSenderSQL = "SELECT	" +
				"t1.sender_node_id as sender_node_id, " +
				"t1.receiver_node_id as receiver_node_id, " +
				"t1.channel, " +
				"n.id, n.hardware_id, n.manufacturer, n.category, n.`type`, " +
				"COUNT(t2.receiver_node_id) as count, " +
				"GROUP_CONCAT(t2.receiver_node_id SEPARATOR ',') as receiver " +
			"FROM " +
				"node_triggers_node t1 " +
				 	"LEFT JOIN " +
				"node n "+
					"ON "+
				"t1.sender_node_id = n.id " +
					"LEFT JOIN "+
				"node_triggers_node t2 " +
				 	"ON " +
				"(t1.sender_node_id = t2.sender_node_id AND t1.channel = t2.channel) " +
			"WHERE " +
				"t1.receiver_node_id = "+String.valueOf(receiver.getDatabaseId())+" "+
			"GROUP BY " +
				"t1.sender_node_id, t1.channel " +
			"ORDER BY " +
				"count ASC " +
			"LIMIT 1;";
			
			
			if (getBestSender.execute(getBestSenderSQL)) {
				ResultSet rs = getBestSender.getResultSet(); 
				if (rs.first()) {
					// There might be a way to change this status
					Node sender = nodeManager.createNodeFromResultSet(rs, false);
					
					String[] receiverString = rs.getString("receiver").split(",");
					int[] receiverIds = new int[receiverString.length];
					for (int i=0; i<receiverString.length; i++)
						receiverIds[i] = Integer.parseInt(receiverString[i]);
					
					// Loop through all specialized Status Manager
					for (SpecializedStatusManager statusManager: specializedStatusManagers) {
						Datagram datagram = statusManager.findDatagramForStatusChange(key, value, new Trigger(rs, nodeManager), receiverIds);
						if (datagram != null) {

							// Send datagram to plugin manager
							nodeManager.sendDatagram(datagram);
							
							
							// Change status and return changed Nodes
							List<Node> result = new ArrayList<Node>();
							for (int i=0; i<receiverIds.length; i++) {
								writeStatusChangeToDatabase(receiverIds[i], key, value);
								result.add(nodeManager.getNode(receiverIds[i], true));
							}
							
							// Leave this methode
							return result;
						}
					}
				}
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<Node>();
	}
	
	
}
