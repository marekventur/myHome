/**
 * 
 */
package de.wi08e.myhome.scriptmanager;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.wi08e.myhome.model.Node;

/**
 * @author Marek
 *
 */
public class Script {
	
	private String name;
	private String script;
	private int triggeringNodeId;
	
	public Script(ResultSet resultSet) throws SQLException {
		name = resultSet.getString("name");
		script = resultSet.getString("script");
		triggeringNodeId = resultSet.getInt("triggering_node_id");
	}

	public String getName() {
		return name;
	}

	public String getScript() {
		return script;
	}

	public int getTriggeringNodeId() {
		return triggeringNodeId;
	}
}
