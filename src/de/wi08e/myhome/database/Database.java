/**
 * 
 */
package de.wi08e.myhome.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author Marek
 * 
 */

public abstract class Database {
	public abstract Connection getConnection() ;
	
	/**
	 * Checks if one column exists in this result set
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static boolean columnExist(ResultSet rs, String name) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		 int numCol = meta.getColumnCount();
	
		for (int i = 1; i < numCol+1; i++) 
		{
		    if(meta.getColumnName(i).equals(name))
		    {return true;}
	
		}
		return false;
	}
}
