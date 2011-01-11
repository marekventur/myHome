/**
 * 
 */
package de.wi08e.myhome.usermanager;

import de.wi08e.myhome.database.Database;
import de.wi08e.myhome.model.Node;

/**
 * Unterteam: Usermanager
 *
 */
public class UserManager {
	private Database database;
	
	public UserManager(Database database) {
		this.database = database;
	}
	
	public Node getPreferedCommunicationNode(int id) {
		return new Node("marekventur", "notifo", "notifo");
	}
}
