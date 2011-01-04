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
import de.wi08e.myhome.nodeplugins.DatagramReceiver;
import de.wi08e.myhome.nodeplugins.NodePluginRunnable;

public class NodeManager {
	private Database database;
	private NodePluginRunnable nodePlugin;
	private List<DatagramReceiver> receivers = new ArrayList<DatagramReceiver>();

	public NodeManager(Database database, NodePluginRunnable nodePlugin) {
		super();
		this.database = database;
	}
	
	public void addReceiver(DatagramReceiver receiver) {
		receivers.add(receiver);
	}
	
	public void receiveDatagram(Datagram datagram) {
		
		if (datagram instanceof BroadcastDatagram) {
			Node sender = ((BroadcastDatagram)datagram).getSender();
			
			// Find Node in db and replace with named node (or so)
			
			((BroadcastDatagram)datagram).setSender(sender);
		}
			
		for (DatagramReceiver receiver: receivers) 
			receiver.receiveDatagram(datagram);
	}
	
	private Node createNodeFromResultSet(ResultSet resultSet, boolean withStatus) throws SQLException {
		
		Node result;
		
		int databaseId = resultSet.getInt(1);
		
		if (resultSet.getString("name") == null) {
			// This node is not named until now
			result = new Node(resultSet.getString("category"), resultSet.getString("manufacturer"), resultSet.getString("hardware_id"));

		}	
		else
		{
			// It's already named
			NamedNode namedNode = new NamedNode(resultSet.getString("category"), resultSet.getString("manufacturer"), resultSet.getString("hardware_id"));
			namedNode.setName(resultSet.getString("name"));
			namedNode.setPositionX(resultSet.getFloat("pos_x"));
			namedNode.setPositionY(resultSet.getFloat("pos_y"));
			namedNode.setBlueprintId(resultSet.getInt("blueprint_id"));
			result = namedNode;
		}
		
		result.setType(resultSet.getString(5));
		result.setDatabaseId(databaseId);
		
		if (withStatus) {
			// Get all status fields 
			Statement getStatus = database.getConnection().createStatement();
			if (getStatus.execute("SELECT `key`, value FROM node_status WHERE node_id="+String.valueOf(databaseId)+";")) {
				ResultSet rs2 = getStatus.getResultSet();
				while (rs2.next()) 
					result.getStatus().put(rs2.getString("key"), rs2.getString("value"));
			}
		}
		
		return result;
	}
	
	private List<Node> generateNodeListFromSQLWhere(String sqlWhere, boolean withStatus) throws SQLException {
		List<Node> result = new ArrayList<Node>();
		
		Statement getNodes = database.getConnection().createStatement();
		if (getNodes.execute("SELECT id, category, manufacturer, hardware_id, type, name, pos_x, pos_y, blueprint_id FROM node WHERE "+sqlWhere+";")) {
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
	
	public synchronized Node getNode(int nodeId, boolean withStatus) {
		try {
			Statement getNodes = database.getConnection().createStatement();
			if (getNodes.execute("SELECT id, category, manufacturer, hardware_id, type, name, pos_x, pos_y, blueprint_id FROM node WHERE id="+String.valueOf(nodeId)+";")) {
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
	
}
