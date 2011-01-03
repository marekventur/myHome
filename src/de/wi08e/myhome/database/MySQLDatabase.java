package de.wi08e.myhome.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDatabase extends Database {
	
	private Connection connection;
	
	public MySQLDatabase(String host, int port, String databaseName, String username, String password) {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://"+host+":"+String.valueOf(port)+"/"+databaseName, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}	

	@Override
	public Connection getConnection() {
		return connection;
	}

}
