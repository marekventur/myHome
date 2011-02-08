/**
 * 
 */
package de.wi08e.myhome.immutablestatusmanager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import de.wi08e.myhome.database.Database;
import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.statusmanager.StatusChangeReceiver;
import de.wi08e.myhome.statusmanager.StatusManager;

/**
 * @author Marek
 *
 */
public class ImmutableStatusManager implements StatusChangeReceiver {
	
	private Database database;
	private StatusManager statusManager;
	
	public ImmutableStatusManager(Database database, StatusManager statusManager) {
		this.database = database;
		this.statusManager = statusManager;
	}

	@Override
	public void statusChanged(Node node, String key, String value) {
		try {
			PreparedStatement getImmutableStatus = database.getConnection().prepareStatement("SELECT value FROM node_status_immutable WHERE node_id=? AND `key`=? AND (`from` < now() OR `from` IS NULL) AND	(`to` > now()) ORDER BY id LIMIT 1"); 
			getImmutableStatus.setInt(1, node.getDatabaseId());
			getImmutableStatus.setString(2, key);
			getImmutableStatus.execute();
			
			ResultSet rs = getImmutableStatus.getResultSet();
			if (rs.next()) {
				if(!value.contentEquals(rs.getString("value"))) {
					statusManager.setStatus(node, key, rs.getString("value"));
				}
			}
			
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
