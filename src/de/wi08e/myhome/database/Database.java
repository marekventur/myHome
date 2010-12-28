/**
 * 
 */
package de.wi08e.myhome.database;

import java.sql.Connection;

/**
 * @author Marek
 * 
 */

public abstract class Database {
	public abstract Connection getConnection() ;
}
