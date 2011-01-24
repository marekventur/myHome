package de.wi08e.myhome.nodemanager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.wi08e.myhome.exceptions.BlueprintNotFound;
import de.wi08e.myhome.exceptions.NodeNotFound;
import de.wi08e.myhome.database.Database;
import de.wi08e.myhome.model.NamedNode;
import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.NodeWithPosition;
import de.wi08e.myhome.model.datagram.BroadcastDatagram;
import de.wi08e.myhome.model.datagram.Datagram;
import de.wi08e.myhome.model.datagram.NodeInformDatagram;
import de.wi08e.myhome.model.datagram.StatusDatagram;
import de.wi08e.myhome.nodeplugins.NodePluginManager;

/**
 * @author Marek_Ventur
 */

public class NodeManager {
	private Database database;
	private NodePluginManager nodePlugin;
	private List<DatagramReceiver> receivers = new ArrayList<DatagramReceiver>();
	
	private Set<String> types = new HashSet<String>();

	public NodeManager(Database database, NodePluginManager nodePlugin) {
		super();
		this.database = database;
		this.nodePlugin = nodePlugin;
		nodePlugin.setNodeManager(this);
	}
	
	/**
	 * @param receiver receiver from receiver node
	 */
	
	public void addReceiver(DatagramReceiver receiver) {
		receivers.add(receiver);
	}
	
	/**
	 * @param datagram is an instance of the superclass Datagram
	 */
	
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
				PreparedStatement getNodeStatus = database.getConnection().prepareStatement("SELECT id, category, manufacturer, hardware_id, type, name FROM node WHERE hardware_id=? AND manufacturer=? AND category=?;"); 
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
						sender.setDatabaseId(id);
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

		if (datagram instanceof StatusDatagram) {
			
			// Update sender node to most accurate node. Insert to db if not found 
			StatusDatagram statusDatagram = (StatusDatagram) datagram;
			Node node = statusDatagram.getNode();
			
			int id;
	    	String type = null;
	    	String name = null;
			
			// Is this Node already in DB?
			try {
				PreparedStatement getNodeStatus = database.getConnection().prepareStatement("SELECT id, category, manufacturer, hardware_id, type, name FROM node WHERE hardware_id=? AND manufacturer=? AND category=?;"); 
				getNodeStatus.setString(1, node.getHardwareId());
				getNodeStatus.setString(2, node.getManufacturer());
				getNodeStatus.setString(3, node.getCategory());
				getNodeStatus.execute();
				
				ResultSet rs = getNodeStatus.getResultSet();
				if (rs.next()) { 
					// Found!
					node = createNodeFromResultSet(rs, false);	
				}
				else
				{
					// Not found, insert node
					PreparedStatement insertNode = database.getConnection().prepareStatement("INSERT INTO node (category, manufacturer, hardware_id) VALUES (?, ?, ?);");
					insertNode.setString(1, node.getCategory());
					insertNode.setString(2, node.getManufacturer());
					insertNode.setString(3, node.getHardwareId());
					insertNode.executeUpdate();
					
					// Get this id
					Statement getId = database.getConnection().createStatement();
					if (getId.execute("SELECT LAST_INSERT_ID()")) {
						ResultSet rs2 = getId.getResultSet();
						rs2.first();
						id = rs2.getInt(1);
						node.setDatabaseId(id);
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
			statusDatagram = new StatusDatagram(node, statusDatagram.getKey(), statusDatagram.getValue());
			
			// Forward to all receiver	
			for (DatagramReceiver receiver: receivers) 
				receiver.receiveStatusDatagram(statusDatagram);
			
		}
		
		if (datagram instanceof NodeInformDatagram) {
			
			// Update sender node to most accurate node. Insert to db if not found 
			NodeInformDatagram nodeInformDatagram = (NodeInformDatagram) datagram;
			Node node = nodeInformDatagram.getNode();
			
			int id;
	    	String type = "";
	    	if (node.getType() != null) {
	    		type = node.getType();
	    		types.add(type);
	    	}
	    		   	
	    	String name = "";
	    	if (node instanceof NamedNode)
	    		name = ((NamedNode)node).getName();
			
			// Is this Node already in DB?
			try {
				PreparedStatement getNodeStatus = database.getConnection().prepareStatement("SELECT id, category, manufacturer, hardware_id, type, name FROM node WHERE hardware_id=? AND manufacturer=? AND category=?;"); 
				getNodeStatus.setString(1, node.getHardwareId());
				getNodeStatus.setString(2, node.getManufacturer());
				getNodeStatus.setString(3, node.getCategory());
				getNodeStatus.execute();
				
				ResultSet rs = getNodeStatus.getResultSet();
				if (rs.next()) { 
					// Found!
					node = createNodeFromResultSet(rs, false);	
				}
				else
				{
					// Not found, insert node
					PreparedStatement insertNode = database.getConnection().prepareStatement("INSERT INTO node (category, manufacturer, hardware_id, type) VALUES (?, ?, ?, ?);");
					insertNode.setString(1, node.getCategory());
					insertNode.setString(2, node.getManufacturer());
					insertNode.setString(3, node.getHardwareId());
					insertNode.setString(4, type);
					insertNode.setString(5, name);
					insertNode.executeUpdate();
					
					// Get this id
					Statement getId = database.getConnection().createStatement();
					if (getId.execute("SELECT LAST_INSERT_ID()")) {
						ResultSet rs2 = getId.getResultSet();
						rs2.first();
						id = rs2.getInt(1);
						node.setDatabaseId(id);
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
			
		}
		
	}
	
	/**
	 * @param resultSet creates a node from resultset
	 * @param withStatus allocates a status to the node
	 * @return return result
	 * @throws SQLException an exception that provides information on a database access or other errors
	 */
	
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
	
	/**
	 * @param sqlWhere searchs the database for nodes and excepts restrictions
	 * @param withStatus status of the nodes may or may be not set
	 * @return result of the query
	 * @throws SQLException an exception that provides information on a database access or other errors
	 */
	
	private List<Node> generateNodeListFromSQLWhere(String sqlWhere, boolean withStatus) throws SQLException {
		List<Node> result = new ArrayList<Node>();
		
		Statement getNodes = database.getConnection().createStatement();
		
		if (getNodes.execute("SELECT node.id, category, manufacturer, hardware_id, type, name, GROUP_CONCAT(tag) as tags FROM node LEFT JOIN node_tag ON node.id = node_tag.node_id WHERE "+sqlWhere+" GROUP BY node.id;")) {
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
	
	/**
	 * @param blueprintId defines a specific blueprint
	 * @return null
	 */
	
	public synchronized List<NodeWithPosition> getAllNodesFilteredByBlueprint(int blueprintId) {
		try {
			String sql = "SELECT node.id, category, manufacturer, hardware_id, type, name, pos_x, pos_y, GROUP_CONCAT(tag) as tags " +
					"FROM node_on_blueprint LEFT JOIN node ON node_on_blueprint.node_id=node.id LEFT JOIN node_tag ON node.id = node_tag.node_id " +
					"WHERE node_on_blueprint.blueprint_id=? GROUP BY node.id;";
			
			PreparedStatement getNodes = database.getConnection().prepareStatement(sql);
			getNodes.setInt(1, blueprintId);
			
			List<NodeWithPosition> result = new ArrayList<NodeWithPosition>();
			
			if (getNodes.execute()) {
				ResultSet rs = getNodes.getResultSet();
				while (rs.next()) 
					result.add(new NodeWithPosition(createNodeFromResultSet(rs, true), rs.getFloat("pos_x"), rs.getFloat("pos_y")));	
			}
			
			return result;
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
	
	public synchronized List<Node> getNodesByType(String type, boolean withStatus) {
		try {
			PreparedStatement getNodeStatus = database.getConnection().prepareStatement("SELECT id, category, manufacturer, hardware_id, type, name FROM node WHERE type=?;"); 
			getNodeStatus.setString(1, type);
			getNodeStatus.execute();
			
			ResultSet rs = getNodeStatus.getResultSet();
			
			List<Node> results = new ArrayList<Node>();
			
			if (rs.next()) 
				results.add(createNodeFromResultSet(rs, withStatus));	
			
			return results;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
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
			
			PreparedStatement getNodes = database.getConnection().prepareStatement("SELECT node.id, category, manufacturer, hardware_id, type, name, GROUP_CONCAT(tag) as tags FROM node LEFT JOIN node_tag ON node.id = node_tag.node_id WHERE node.id IN (SELECT node_id FROM node_tag WHERE tag=? GROUP BY node_id) GROUP BY node.id;"); 
			getNodes.setString(1, tag);
			
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
			if (getNodes.execute("SELECT node.id, category, manufacturer, hardware_id, type, name, GROUP_CONCAT(tag) as tags FROM node LEFT JOIN node_tag ON node.id = node_tag.node_id WHERE node.id="+String.valueOf(nodeId)+";")) {
				ResultSet rs = getNodes.getResultSet();
				if (rs.next()) 
					return createNodeFromResultSet(rs, withStatus);	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * A Node contains the parameters hardwareId, manufacturer and category.
	 * @param hardwareId
	 * @param manufacturer
	 * @param category
	 * @param withStatus
	 * @return
	 */
	
	public synchronized Node getNode(String hardwareId, String manufacturer, String category, boolean withStatus) {
		try {
			PreparedStatement getNodeStatus = database.getConnection().prepareStatement("SELECT id, category, manufacturer, hardware_id, type, name FROM node WHERE hardware_id=? AND manufacture=? AND category=?;"); 
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

	/**
	 * 
	 * @param tag a node gets a tag, for searching the node if necessary
	 * @param withStatus
	 * @return
	 */
	public List<Node> getNodesByTag(String tag, boolean withStatus) {
		try {
			PreparedStatement getNodeStatus = database.getConnection().prepareStatement("SELECT id, category, manufacturer, hardware_id, type, name FROM node WHERE id IN (SELECT node_id FROM node_tag WHERE tag=?);"); 
			getNodeStatus.setString(1, tag);
			getNodeStatus.execute();
			
			ResultSet rs = getNodeStatus.getResultSet();
			
			List<Node> results = new ArrayList<Node>();
			
			if (rs.next()) 
				results.add(createNodeFromResultSet(rs, withStatus));	
			
			return results;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public synchronized Node getNode(String name, boolean withStatus) {
		try {
			PreparedStatement getNodeStatus = database.getConnection().prepareStatement("SELECT id, category, manufacturer, hardware_id, type, name FROM node WHERE name=?;"); 
			getNodeStatus.setString(1, name);
			getNodeStatus.execute();
			
			ResultSet rs = getNodeStatus.getResultSet();
			if (rs.next()) 
				return createNodeFromResultSet(rs, withStatus);	
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public synchronized void nameNode(int nodeId, String name) throws NodeNotFound {
		try {
			PreparedStatement updateNode = database.getConnection().prepareStatement("UPDATE node SET name=? WHERE id=?;");
			updateNode.setString(1, name);
			updateNode.setInt(2, nodeId);
			if (updateNode.executeUpdate() == 0)
				throw new NodeNotFound();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * A User defined Node contains the parameters name, category and type
	 * @param name
	 * @param category
	 * @param type
	 * @return
	 */
	
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
	
	/**
	 * The function addTag needs a nodeId and a tag for allocation a tag to a node
	 * @param nodeId
	 * @param tag a node gets a tag, for searching the node if necessary
	 * @return
	 */
	
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
	
	/**
	 * The function addTag needs a nodeId and a tag for allocation a tag to a node
	 * @param nodeId
	 * @param tag a node gets a tag, for searching the node is necessary
	 * @return
	 */
	
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

	public void positionNodeOnBlueprint(int nodeId, int blueprintId, float x, float y) throws BlueprintNotFound, NodeNotFound {
		try {
			
			PreparedStatement getNodePosition = database.getConnection().prepareStatement("SELECT node_id FROM node_on_blueprint WHERE node_id=? AND blueprint_id=?;"); 
			getNodePosition.setInt(1, nodeId);
			getNodePosition.setInt(2, blueprintId);
			getNodePosition.execute();
			
			ResultSet rs = getNodePosition.getResultSet();
			if (rs.next()) { 
				PreparedStatement updateNodePosition = database.getConnection().prepareStatement(
				"UPDATE node_on_blueprint SET pos_x=? , pos_y=? WHERE node_id=? AND blueprint_id=?;");
				updateNodePosition.setFloat(1, x);
				updateNodePosition.setFloat(2, y);
				updateNodePosition.setInt(3, nodeId);
				updateNodePosition.setInt(4, blueprintId);
				updateNodePosition.executeUpdate();
			}
			else
			{

				PreparedStatement insertNodeOnBlueprint = database.getConnection().prepareStatement(
								"INSERT INTO node_on_blueprint (pos_x, pos_y, node_id, blueprint_id) VALUES (?, ?, ?, ?);");
				insertNodeOnBlueprint.setFloat(1, x);
				insertNodeOnBlueprint.setFloat(2, y);
				insertNodeOnBlueprint.setInt(3, nodeId);
				insertNodeOnBlueprint.setInt(4, blueprintId);
				insertNodeOnBlueprint.executeUpdate();
				
			}	
			rs.close();

		} catch (SQLException e) {
			if (e.getMessage().contains("a foreign key constraint fails")) {
				if (e.getMessage().contains("REFERENCES `node` (`id`)"))
					throw new NodeNotFound();
				if (e.getMessage().contains("REFERENCES `blueprint` (`id`)"))
					throw new BlueprintNotFound();
			}
			else
			{
				e.printStackTrace();
			}
		}
		
		
	}

	public Set<String> getTypes() {
		return types;
	}
	
}
