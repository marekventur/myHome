package de.wi08e.myhome.statusmanager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.wi08e.myhome.database.Database;
import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.Trigger;
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
	public List<Trigger> getReceiver(int senderId) {
		List<Trigger> result = new ArrayList<Trigger>();
		
		try {
			Statement getTriggerNodes = database.getConnection().createStatement();
			if (getTriggerNodes.execute("SELECT receiver_node_id, channel FROM node_triggers_node WHERE sender_node_id = "+String.valueOf(senderId)+";")) {
				ResultSet rs = getTriggerNodes.getResultSet();
				while (rs.next()) {
					char channel = 0;
					if (rs.getString("channel")!=null && rs.getString("channel").length()>0)
						channel = rs.getString("channel").charAt(0);
					result.add(new Trigger(null, nodeManager.getNode(rs.getInt("receiver_node_id"), false), channel));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		return result;
	}
	
	public List<Trigger> getReceiver(Node sender) {
		return getReceiver(sender.getDatabaseId());
	}
	
	/**
	 * This returns all sensors for one actor
	 * @param triggerId Actor id
	 * @return Sensor id
	 */
	public List<Trigger> getSender(int receiverId) {
		List<Trigger> result = new ArrayList<Trigger>();
		
		try {
			Statement getTriggerNodes = database.getConnection().createStatement();
			if (getTriggerNodes.execute("SELECT sender_node_id, channel FROM node_triggers_node WHERE receiver_node_id = "+String.valueOf(receiverId)+";")) {
				ResultSet rs = getTriggerNodes.getResultSet();
				while (rs.next()) {
					
					char channel = 0;
					if (rs.getString("channel")!=null && rs.getString("channel").length()>0)
						channel = rs.getString("channel").charAt(0);
				
					result.add(new Trigger(nodeManager.getNode(rs.getInt("sender_node_id"), false), null, channel));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;	
	}
	
	public List<Trigger> getSender(Node receiver) {
		return getSender(receiver.getDatabaseId());
	}
	
	public void addSenderToReciver(int senderId, int receiverId) throws SQLException {
		deleteTrigger(senderId, receiverId);
		
		PreparedStatement insertTrigger = database.getConnection().prepareStatement("INSERT INTO node_triggers_node (sender_node_id, receiver_node_id) VALUES (?, ?);");
		
		insertTrigger.setInt(1, senderId);
		insertTrigger.setInt(2, receiverId);
		
		insertTrigger.executeUpdate();
		
	}
	
	public void addSenderToReciver(int senderId, int receiverId, char channel) throws SQLException {
		deleteTrigger(senderId, receiverId);
		
		PreparedStatement insertTrigger = database.getConnection().prepareStatement("INSERT INTO node_triggers_node (sender_node_id, receiver_node_id, channel) VALUES (?, ?, ?);");
		
		insertTrigger.setInt(1, senderId);
		insertTrigger.setInt(2, receiverId);
		insertTrigger.setString(3, (""+channel).toLowerCase()); // Is there a less dirty way to convert from char to String?
		
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
