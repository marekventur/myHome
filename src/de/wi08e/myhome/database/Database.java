/**
 * 
 */
package de.wi08e.myhome.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import de.wi08e.myhome.Config;


/**
 * @author Marek
 * 
 */

public class Database {

	private static Connection conn;

	public static void initiate() {
		
		if (Config.getDatabaseType() == Config.DatabaseType.MYSQL) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			try {
				conn = DriverManager.getConnection("jdbc:mysql://"+Config.getDatabaseHost()+":"+Config.getDatabasePort()+"/"+Config.getDatabaseName(), Config.getDatabaseUser(), Config.getDatabasePassword());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

	public static Connection getConn() {
		return conn;
	}

}
