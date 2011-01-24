/**
 * 
 */
package de.wi08e.myhome.usermanager;

import de.wi08e.myhome.database.Database;

/**
 * Unterteam: Usermanager
 *
 */
public class UserManager {
	private Database database;
	
	public UserManager(Database database) {
		this.database = database;
	}
	
	public String getPreferedCommunicationIdentifier(String username) {
		return "marekventur::notifo";
	}
}
