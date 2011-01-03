package de.wi08e.myhome.statusmanager;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.wi08e.myhome.database.Database;
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
				alreadyInDBStatement.setString(3, sender.getId());
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
					insertNode.setString(3, sender.getId());
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

}
