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
import de.wi08e.myhome.model.datagram.BroadcastDatagram;
import de.wi08e.myhome.model.datagram.Datagram;
import de.wi08e.myhome.myhomescript.ScriptingEngine;
import de.wi08e.myhome.nodeplugins.DatagramReceiver;
import de.wi08e.myhome.nodeplugins.NodePluginRunnable;

public class StatusManager implements DatagramReceiver{
	
	private Database database;
	private NodePluginRunnable nodePlugin;
	private ScriptingEngine scriptingEngine;
	
	private List<SpecializedStatusManager> specializedStatusManagers = new ArrayList<SpecializedStatusManager>();
	
	public StatusManager(Database database, NodePluginRunnable nodePlugin,
			ScriptingEngine scriptingEngine) {
		super();
		this.database = database;
		this.nodePlugin = nodePlugin;
		this.scriptingEngine = scriptingEngine;
		
		// Add StatusManager
		specializedStatusManagers.add(new RockerSwitchStatusManager(database));
		
	}

	public void receiveDatagram(Datagram datagram) {
		if (datagram instanceof BroadcastDatagram) {
			BroadcastDatagram broadcastDatagram = (BroadcastDatagram) datagram;
			Node sender = broadcastDatagram.getSender();
			
			int id;
	    	String type = null;
	    	String name = null;
			
			// Is this Node already in DB?
			try {
				PreparedStatement alreadyInDBStatement = database.getConnection().prepareStatement("SELECT id, type, name FROM node WHERE category=? AND manufacturer=? AND hardware_id=? LIMIT 1;"); 
				alreadyInDBStatement.setString(1, sender.getCategory());
				alreadyInDBStatement.setString(2, sender.getManufacturer());
				alreadyInDBStatement.setString(3, sender.getHardwareId());
				if (!alreadyInDBStatement.execute()) 
					throw new Exception("Can't SELECT from db.");
				ResultSet rs = alreadyInDBStatement.getResultSet();
				
				
				if (rs.next()) {
					// Found!
			    	id = rs.getInt(1);
			    	type = rs.getString(2);
			    	name = rs.getString(3);
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
				
				System.out.println(id);
				
				// Send to all specialized StatusManagers
				for (SpecializedStatusManager statusManager: specializedStatusManagers) 
					if (statusManager.handleDatagram(id, broadcastDatagram))
						break;
				
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private List<Node> generateNodeListFromSQLWhere(String sqlWhere) throws SQLException {
		List<Node> result = new ArrayList<Node>();
		
		Statement getNodes = database.getConnection().createStatement();
		if (getNodes.execute("SELECT id, category, manufacturer, hardware_id, type, name, pos_x, pos_y, blueprint_id FROM node WHERE "+sqlWhere+";")) {
			ResultSet rs = getNodes.getResultSet();
			while (rs.next()) {
				
				Node node;
				
				int databaseId = rs.getInt(1);
				
				if (rs.getString("name") == null) {
					// This node is not named until now
					node = new Node(rs.getString("category"), rs.getString("manufacturer"), rs.getString("hardware_id"));
	
				}	
				else
				{
					// It's already named
					NamedNode namedNode = new NamedNode(rs.getString("category"), rs.getString("manufacturer"), rs.getString("hardware_id"));
					namedNode.setName(rs.getString("name"));
					namedNode.setPositionX(rs.getFloat("pos_x"));
					namedNode.setPositionY(rs.getFloat("pos_y"));
					namedNode.setBlueprintId(rs.getInt("blueprint_id"));
					node = namedNode;
				}
				
				node.setType(rs.getString(5));
				node.setDatabaseId(databaseId);
				
				// Get all status fields 
				Statement getStatus = database.getConnection().createStatement();
				if (getStatus.execute("SELECT `key`, value FROM node_status WHERE node_id="+String.valueOf(databaseId)+";")) {
					ResultSet rs2 = getStatus.getResultSet();
					while (rs2.next()) 
						node.getStatus().put(rs2.getString("key"), rs2.getString("value"));
				}
				
				result.add(node);
			
			}
			
		}
	
		
		return result;
	}
	
	public synchronized List<Node> getAllNodes() {
		try {
			return generateNodeListFromSQLWhere("1=1");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
