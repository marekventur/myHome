package de.wi08e.myhome.statusmanager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.wi08e.myhome.database.Database;
import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.nodemanager.NodeManager;


public class TriggerManager {
	
	private Database database;
	private NodeManager nodeManager;
	
	public TriggerManager(Database database, NodeManager nodeManager) {
		super();
		this.nodeManager = nodeManager;
		this.database = database;
	}

	/**
	 * This returns all actors for one sensor
	 * @param senderId Sensor id
	 * @return Actor id
	 */
	public List<Node> getReceiver(int senderId) {
		List<Node> result = new ArrayList<Node>();
		
		try {
			Statement getTriggerNodes = database.getConnection().createStatement();
			if (getTriggerNodes.execute("SELECT receiver_node_id FROM node_triggers_node WHERE sender_node_id = "+String.valueOf(senderId)+";")) {
				ResultSet rs = getTriggerNodes.getResultSet();
				while (rs.next()) {
					result.add(nodeManager.getNode(rs.getInt("receiver_node_id"), false));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		return result;
	}
	
	public List<Node> getReceiver(Node sender) {
		return getReceiver(sender.getDatabaseId());
	}
	
	/**
	 * This returns all sensors for one actor
	 * @param triggerId Actor id
	 * @return Sensor id
	 */
	public List<Node> getSender(int receiverId) {
		List<Node> result = new ArrayList<Node>();
		
		try {
			Statement getTriggerNodes = database.getConnection().createStatement();
			if (getTriggerNodes.execute("SELECT sender_node_id FROM node_triggers_node WHERE receiver_node_id = "+String.valueOf(receiverId)+";")) {
				ResultSet rs = getTriggerNodes.getResultSet();
				while (rs.next()) 
					result.add(nodeManager.getNode(rs.getInt("sender_node_id"), false));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;	
	}
	
	public List<Node> getSender(Node receiver) {
		return getSender(receiver.getDatabaseId());
	}
	
	public void addSenderToReciver(int senderId, int receiverId) throws SQLException {
		PreparedStatement insertTrigger = database.getConnection().prepareStatement("INSERT INTO node_triggers_node (sender_node_id, receiver_node_id) VALUES (?, ?);");
		
		insertTrigger.setInt(1, senderId);
		insertTrigger.setInt(2, receiverId);
		
		insertTrigger.executeUpdate();
		
	}

	public void deleteTrigger(int senderId, int receiverId) {
		try {
			PreparedStatement deleteTrigger = database.getConnection().prepareStatement("DELETE FROM node_triggers_node WHERE sender_node_id = ? AND receiver_node_id = ?;");
			
			deleteTrigger.setInt(1, senderId);
			deleteTrigger.setInt(2, receiverId);
			
			deleteTrigger.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
