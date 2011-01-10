package de.wi08e.myhome.nodemanager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.wi08e.myhome.database.Database;
import de.wi08e.myhome.model.NamedNode;
import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.datagram.BroadcastDatagram;
import de.wi08e.myhome.model.datagram.Datagram;
import de.wi08e.myhome.nodeplugins.NodePluginManager;

public class NodeManager {
	private Database database;
	private NodePluginManager nodePlugin;
	private List<DatagramReceiver> receivers = new ArrayList<DatagramReceiver>();

	public NodeManager(Database database, NodePluginManager nodePlugin) {
		super();
		this.database = database;
		this.nodePlugin = nodePlugin;
		nodePlugin.setNodeManager(this);
	}
	
	public void addReceiver(DatagramReceiver receiver) {
		receivers.add(receiver);
	}
	
	public void receiveDatagram(Datagram datagram) {
		
		if (datagram instanceof BroadcastDatagram) {
			
			// Update sender node to most accurate node. Insert to db if not found 
			BroadcastDatagram broadcastDatagram = (BroadcastDatagram) datagram;
			Node sender = broadcastDatagram.getSender();
			
			int id;
	    	String type = null;
	    	String name = null;
			
			// Is this Node already in DB?
			try {
				PreparedStatement getNodeStatus = database.getConnection().prepareStatement("SELECT id, category, manufacturer, hardware_id, type, name, pos_x, pos_y, blueprint_id FROM node WHERE hardware_id=? AND manufacturer=? AND category=?;"); 
				getNodeStatus.setString(1, sender.getHardwareId());
				getNodeStatus.setString(2, sender.getManufacturer());
				getNodeStatus.setString(3, sender.getCategory());
				getNodeStatus.execute();
				
				ResultSet rs = getNodeStatus.getResultSet();
				if (rs.next()) { 
					// Found!
					sender = createNodeFromResultSet(rs, false);	
				}
				else
				{
					// Not found, insert node
					PreparedStatement insertNode = database.getConnection().prepareStatement("INSERT INTO node (category, manufacturer, hardware_id) VALUES (?, ?, ?);");
					insertNode.setString(1, sender.getCategory());
					insertNode.setString(2, sender.getManufacturer());
					insertNode.setString(3, sender.getHardwareId());
					insertNode.executeUpdate();
					
					// Get this id
					Statement getId = database.getConnection().createStatement();
					if (getId.execute("SELECT LAST_INSERT_ID()")) {
						ResultSet rs2 = getId.getResultSet();
						rs2.first();
						id = rs2.getInt(1);
					}
					else
					{
						throw new Exception("Can't get LAST_INSERT_ID");
					}	
				}
				
	
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// Update Sender (It might now be a named node)
			broadcastDatagram.setSender(sender);
			
			
			
			// Forward to all receiver	
			for (DatagramReceiver receiver: receivers) 
				receiver.receiveBroadcastDatagram(broadcastDatagram);
			
		}

		
	}
	
	public Node createNodeFromResultSet(ResultSet resultSet, boolean withStatus) throws SQLException {
		Node result;
		
		if (!Database.columnExist(resultSet, "name") || resultSet.getString("name") == null) 
			result = new Node(resultSet);	
		else
			result = new NamedNode(resultSet);
		
		if (withStatus) 
			result.loadStatus(database);
		
		return result;
	}
	
	private List<Node> generateNodeListFromSQLWhere(String sqlWhere, boolean withStatus) throws SQLException {
		List<Node> result = new ArrayList<Node>();
		
		Statement getNodes = database.getConnection().createStatement();
		
		if (getNodes.execute("SELECT node.id, category, manufacturer, hardware_id, type, name, pos_x, pos_y, blueprint_id, GROUP_CONCAT(tag) as tags FROM node LEFT JOIN node_tag ON node.id = node_tag.node_id WHERE "+sqlWhere+" GROUP BY node.id;")) {
			ResultSet rs = getNodes.getResultSet();
			while (rs.next()) 
				result.add(createNodeFromResultSet(rs, withStatus));	
		}
	
		
		return result;
	}
	
	public synchronized List<Node> getAllNodes() {
		try {
			return generateNodeListFromSQLWhere("1=1", true);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public synchronized List<Node> getAllNodesFilteredByBlueprint(int blueprintId) {
		try {
			return generateNodeListFromSQLWhere("blueprint_id="+String.valueOf(blueprintId), true);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public synchronized List<Node> getUnnamedNodes() {
		try {
			return generateNodeListFromSQLWhere("name IS NULL", true);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public synchronized List<Node> getUserdefinedNodes() {
		try {
			return generateNodeListFromSQLWhere("manufacturer = 'userdefined'", true);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public synchronized List<Node> getTaggedNodes(String tag) {
		try {
			List<Node> result = new ArrayList<Node>();
			
			PreparedStatement getNodes = database.getConnection().prepareStatement("SELECT node.id, category, manufacturer, hardware_id, type, name, pos_x, pos_y, blueprint_id, GROUP_CONCAT(tag) as tags FROM node LEFT JOIN node_tag ON node.id = node_tag.node_id WHERE node.id IN (SELECT node_id FROM node_tag WHERE tag=? GROUP BY node_id) GROUP BY node.id;"); 
			getNodes.setString(1, tag);
			getNodes.execute();
			
			if (getNodes.execute()) {
				ResultSet rs = getNodes.getResultSet();
				while (rs.next()) 
					result.add(createNodeFromResultSet(rs, true));	
			}
		
			
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public synchronized Node getNode(int nodeId, boolean withStatus) {
		try {
			Statement getNodes = database.getConnection().createStatement();
			if (getNodes.execute("SELECT node.id, category, manufacturer, hardware_id, type, name, pos_x, pos_y, blueprint_id, GROUP_CONCAT(tag) as tags FROM node LEFT JOIN node_tag ON node.id = node_tag.node_id WHERE node.id="+String.valueOf(nodeId)+";")) {
				ResultSet rs = getNodes.getResultSet();
				if (rs.next()) 
					return createNodeFromResultSet(rs, withStatus);	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public synchronized Node getNode(String hardwareId, String manufacturer, String category, boolean withStatus) {
		try {
			PreparedStatement getNodeStatus = database.getConnection().prepareStatement("SELECT id, category, manufacturer, hardware_id, type, name, pos_x, pos_y, blueprint_id FROM node WHERE hardware_id=? AND manufacture=? AND category=?;"); 
			getNodeStatus.setString(1, hardwareId);
			getNodeStatus.setString(2, manufacturer);
			getNodeStatus.setString(3, category);
			getNodeStatus.execute();
			
			ResultSet rs = getNodeStatus.getResultSet();
			if (rs.next()) 
				return createNodeFromResultSet(rs, withStatus);	
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public synchronized Node getNode(Node node, boolean withStatus) {
		return getNode(node.getHardwareId(), node.getManufacturer(), node.getCategory(), withStatus);
	}
	
	public synchronized int addUserDefinedNode(String name, String category, String type) {
		
		int nodeId = -1;
		
		try {
			// Get next id for "userdefined" nodes
			int nextId = 1;
			Statement getHighestId = database.getConnection().createStatement();
			if (getHighestId.execute("SELECT max(CONVERT(hardware_id, SIGNED INTEGER)) as max FROM node WHERE manufacturer='userdefined';")) {
				ResultSet rs = getHighestId.getResultSet();
				if (rs.next()) 
					nextId = rs.getInt("max") + 1;
					
			}
			getHighestId.close();
			
			// Add new Node
			PreparedStatement insertNode = database.getConnection().prepareStatement("INSERT INTO node (category, manufacturer, hardware_id, type, name) VALUES (?, 'userdefined', ?, ?, ?);");
			insertNode.setString(1, category);
			insertNode.setString(2, String.valueOf(nextId));
			insertNode.setString(3, type);
			insertNode.setString(4, name);
			insertNode.executeUpdate();
			insertNode.close();
			
			// Get this id
			Statement getId = database.getConnection().createStatement();
			getId.execute("SELECT LAST_INSERT_ID()");
			ResultSet rs2 = getId.getResultSet();
			rs2.first();
			nodeId = rs2.getInt(1);
			getId.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 
		return nodeId;
	}

	public void sendDatagram(Datagram datagram) {
		nodePlugin.sendDatagram(datagram);
	}
	
	public boolean addTag(int nodeId, String tag) {
		try {
			PreparedStatement insertTag = database.getConnection().prepareStatement("INSERT into node_tag (node_id, tag) VALUES (?, ?);");
			insertTag.setInt(1, nodeId);
			insertTag.setString(2, tag);
			insertTag.executeUpdate();
			insertTag.close();
			return true;
			
		} catch (SQLException e) {
		}
		return false;
	}
	
	public boolean deleteTag(int nodeId, String tag) {
		try {
			PreparedStatement deleteTagStatement = database.getConnection().prepareStatement("DELETE FROM node_tag WHERE node_id = ? AND tag = ?;");
			deleteTagStatement.setInt(1, nodeId);
			deleteTagStatement.setString(2, tag);
			return (deleteTagStatement.executeUpdate() == 1);

		} catch (SQLException e) {
		}
		return false;
	}
	
	
	
}
