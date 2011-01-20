/**
 * 
 */
package de.wi08e.myhome.scriptmanager;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Marek
 *
 */
public class Script {
	
	private String name;
	private String script;
	/*
	private int senderNodeId = 0;
	private int statusChangeNodeId = 0;*/
	
	public Script(ResultSet resultSet) throws SQLException {
		name = resultSet.getString("name");
		script = resultSet.getString("script");
		/*
		if (Database.columnExist(resultSet, "sender_node_id"))
			senderNodeId = resultSet.getInt("sender_node_id");
		if (Database.columnExist(resultSet, "status_change_node_id"))
			statusChangeNodeId = resultSet.getInt("status_change_node_id");
			*/
		
	}

	public String getName() {
		return name;
	}

	public String getScript() {
		return script;
	}

	/*
	public int getSenderNodeId() {
		return senderNodeId;
	}

	public int getStatusChangeNodeId() {
		return statusChangeNodeId;
	}
*/
	
}
