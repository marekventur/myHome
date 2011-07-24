package benutzerverwaltung;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class ClassBenutzerverwaltung {

	private Connection con = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private String db_name;
	private String db_user;
	private String db_pswd;
	
	private void connect_db()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch( ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		try {
		con = DriverManager.getConnection("jdbc:mysql://vmax2.marekventur.de:110/" + this.db_name,this.db_user,this.db_pswd);
		//con = DriverManager.getConnection("jdbc:mysql://localhost/" + this.db_name,this.db_user,this.db_pswd);
		stmt = con.createStatement();
		}
		catch( SQLException e)
		{
			e.printStackTrace();
			return;
		}
	}
	
	private void disconnect_db()
	{
		try {
			stmt.close();
			con.close();
		} 
		catch(SQLException e)
		{
			e.printStackTrace();
			return;
		}
	}
	public ClassBenutzerverwaltung(String db_name, String user, String pswd)
	{
		this.db_name = db_name;
		this.db_user = user;
		this.db_pswd = pswd;
		
		try {
			connect_db();
			//stmt.executeUpdate("CREATE TABLE node (id INT NOT NULL, PRIMARY KEY ( id))");
			stmt.executeUpdate("CREATE TABLE role (id INT NOT NULL AUTO_INCREMENT, roleName TEXT NOT NULL, PRIMARY KEY ( `id` ))");
			stmt.executeUpdate("CREATE TABLE consistsOf (roleId INT NOT NULL , nodeId INT NOT NULL, writable BOOLEAN NOT NULL, PRIMARY KEY ( roleId,nodeId ), FOREIGN KEY (roleId) REFERENCES role(id), FOREIGN KEY (nodeId) REFERENCES node(id) )");
			stmt.executeUpdate("CREATE TABLE user (id INT NOT NULL AUTO_INCREMENT,username TEXT NOT NULL,pswd CHAR(64) NOT NULL,isAdmin BOOLEAN NOT NULL,preferred_communication TEXT NOT NULL, fk_roleId INT NOT NULL, PRIMARY KEY ( `id` ), FOREIGN KEY (fk_roleId) REFERENCES  roles(id))");


			disconnect_db();
		}
		catch( SQLException e)
		{
			e.printStackTrace();
			return;
		}
	}

	public void setDefaultRoles()
	{
		connect_db();
		try {
			stmt.executeUpdate("INSERT INTO role (roleName) VALUES ('Admin')");
			stmt.executeUpdate("INSERT INTO role (roleName) VALUES ('Hauptmieter')");
			stmt.executeUpdate("INSERT INTO role (roleName) VALUES ('Untermieter')");
			stmt.executeUpdate("INSERT INTO role (roleName) VALUES ('Gast')");
		}
		catch( SQLException e)
		{
			e.printStackTrace();
			return;
		}
		disconnect_db();
	}
	public void addUser(String username, String pswd,boolean isAdmin, String preferred_communication, int roleId)
	{
		connect_db();
		try {
			stmt.executeUpdate("INSERT INTO user (username, pswd, isAdmin, preferred_communication, fk_roleId) VALUES ('" + username + "',SHA1('" + pswd + "')," + isAdmin + ",'" + preferred_communication+ "',"+ roleId + ")");
		}
		catch( SQLException e)
		{
			e.printStackTrace();
			return;
		}
		disconnect_db();
	}
	public void deleteUser(int user_id)
	{
		connect_db();
		try {
			rs = stmt.executeQuery("DELETE FROM user WHERE id=" + user_id);
		}
		catch( SQLException e)
		{
			e.printStackTrace();
			return;
		}
		disconnect_db();
	}
	public void updateUser(int user_id, String username, String pswd, boolean isAdmin, String preferred_communication)
	{
		connect_db();
		try {
			rs = stmt.executeQuery("UPDATE user SET username=" + username + ", pswd=SHA1('" + pswd + "'), isAdmin=" + isAdmin + ", preferred_communication=" + preferred_communication + "WHERE user_id=" + user_id);
		}
		catch( SQLException e)
		{
			e.printStackTrace();
			return;
		}
		disconnect_db();
	}
	public void updateUsername(int user_id, String username)
	{
		connect_db();
		try {
			rs = stmt.executeQuery("UPDATE user SET username=" + username + "WHERE id=" + user_id);
		}
		catch( SQLException e)
		{
			e.printStackTrace();
			return;
		}
		disconnect_db();
	}
	public void updatePswd(int user_id, String pswd)
	{
		connect_db();
		try {
			rs = stmt.executeQuery("UPDATE user SET pswd=SHA1('" + pswd + "') WHERE id=" + user_id);
		}
		catch( SQLException e)
		{
			e.printStackTrace();
			return;
		}
		disconnect_db();
	}
	public void updateIsAdmin(int user_id, boolean isAdmin)
	{
		connect_db();
		try {
			rs = stmt.executeQuery("UPDATE user SET isAdmin=" + isAdmin + "WHERE id=" + user_id);
		}
		catch( SQLException e)
		{
			e.printStackTrace();
			return;
		}
		disconnect_db();
	}
	public void updatePreferredCom(int userId, String preferredCom)
	{
		connect_db();
		try {
			rs = stmt.executeQuery("UPDATE user SET preferred_communication=" + preferredCom + "WHERE id=" + userId);
		}
		catch( SQLException e)
		{
			e.printStackTrace();
			return;
		}
		disconnect_db();
	}
	public void updateAccess(int userId, int roleId)
	{
		connect_db();
		try {
			rs = stmt.executeQuery("UPDATE user SET fk_roleId=" + roleId + "WHERE id=" + userId);
		}
		catch( SQLException e)
		{
			e.printStackTrace();
			return;
		}
		disconnect_db();
	}
	public User readUser(int user_id)
	{
		connect_db();
		User getUser = new User();
		try {
			rs = stmt.executeQuery("SELECT * FROM user WHERE id =" + user_id);
	        while(rs.next())
	        	{
	        			getUser.setId(rs.getInt( 1 )) ;
	        			getUser.setUserName(rs.getString(2));
	        			getUser.setAdmin(rs.getBoolean(4));
	        			getUser.setPreferredCom(rs.getString(5));
	        			getUser.setRoleId(rs.getInt(6));
	        	}
			disconnect_db();
		}
		catch( SQLException e)
		{
			e.printStackTrace();
		}
		return getUser;
	}
    
	public List<User> readAllUserID()
	{
        List<User> listUser = new ArrayList<User>();
		try {
			connect_db();
			rs = stmt.executeQuery("SELECT * FROM user ORDER BY id ASC");
	        while(rs.next())
        	{	
	        		User bufferUser = new User();
        			bufferUser.setId(rs.getInt(1 )) ;
        			bufferUser.setUserName(rs.getString(2));
        			bufferUser.setAdmin(rs.getBoolean(4));
        			bufferUser.setPreferredCom(rs.getString(5));
        			bufferUser.setRoleId(rs.getInt(6));
        			listUser.add(bufferUser);
        	}
	        disconnect_db();
		}
		catch( SQLException e)
		{
			e.printStackTrace();
		}
		return listUser;
	}
	
	public boolean login(String username, String pswd)
	{
		int count = 0;
		connect_db();
		try {
			rs = stmt.executeQuery("SELECT id FROM user WHERE username='" + username + "' AND pswd=SHA1('" + pswd + "')" );
			if (!rs.next() ) {
			    System.out.println("no data");
			    count = 0;
			}
			else
				count = 1;
		}
		catch( SQLException e)
		{
			e.printStackTrace();
		}
		disconnect_db();
		if (count == 0) 
		    return false;
		else
			return true;
	}
	public List<Right> hasRights(int userId)
	{
		//Rolle des Users in die jeweiligen Rechte übersetzen.
		List<Right> rightsUser = new ArrayList<Right>();
		connect_db();
		try {
			rs = stmt.executeQuery("SELECT node.id, consistsof.writable FROM node JOIN consistsof ON consistsof.nodeid = node.id JOIN role ON role.id = consistsof.roleId JOIN user ON user.fk_roleId = role.id WHERE user.id =" + userId);
			while (rs.next() ) {
				Right bufferRight = new Right();
			    bufferRight.setNodeId(rs.getInt(1));
			    bufferRight.setWritable(rs.getBoolean(2));
			    rightsUser.add(bufferRight);
			}
		}
		catch( SQLException e)
		{
			e.printStackTrace();
		}
		disconnect_db();
		return rightsUser;
	}
	public boolean addRole(String roleName, List<Right> rights)
	{	//Rolle definieren und zugehörige Rechte hinzufügen. bei misserfolg, wird false zurück gegeben.
		Right bufferRight = new Right();
		try {
			connect_db();
				stmt.executeUpdate("INSERT INTO role (roleName) VALUES ('" + roleName + "')");
				rs = stmt.executeQuery("SELECT id FROM role WHERE roleName = '" + roleName + "'");
				if(rs.next())
				{
					int idBuffer = rs.getInt(1);
					for(int i = 0; i < rights.size(); i++)
					{
						bufferRight = rights.get(i);
						boolean bufferBool = bufferRight.isWritable();
						int BufferWrite = 0;
						if(bufferBool == true)
							BufferWrite = 1;
						stmt.executeUpdate("INSERT INTO consistsof (roleId, nodeId, writable) VALUES (" + idBuffer + ",'" + bufferRight.getNodeId() + "','" + BufferWrite + "')");
					}
				}
				disconnect_db();
				return true;
		}
		catch( SQLException e)
		{
			e.printStackTrace();
			return false;
		}		
		
	}
	public void deleteRole(int roleId)
	{
		try {
			connect_db();
			rs = stmt.executeQuery("DELETE FROM roles WHERE id=" + roleId);
			disconnect_db();
		}
		catch( SQLException e)
		{
			e.printStackTrace();
			return;
		}	
	}
	public boolean addRightToRole(int roleId, int nodeId, boolean writable)
	{ //Funktion gibt false zurück falls Eintrag bereits existiert. True wenn geklappt.
		try {
			connect_db();
			int writeInt = 0;
			if (writable == true)
				writeInt = 1;
			rs = stmt.executeQuery("SELECT roleId, nodeId, writable FROM consistsof WHERE roleId = '" + roleId + "' AND nodeId = '" + nodeId + "' AND writable = '" + writeInt + "'");
			if (rs.next()) {
				disconnect_db();
				return false;
			}
			else
			{
				stmt.executeUpdate("INSERT INTO consistsof (roleId, nodeId, writable) VALUES (" + roleId + "," + nodeId + "," + writable + ")");
				disconnect_db();
				return true;
			}
			
		}
		catch( SQLException e)
		{
			e.printStackTrace();
			return false;
		}		
	}
	public boolean deleteRightFromRole(int roleId, int nodeId)
	{	//Funktion löscht das übergebene Recht in der Rolle. Gibt bei Erfolg true zurück bei nicht existenz oder misserfolg false.
		try {
			connect_db();
			rs = stmt.executeQuery("SELECT roleId, nodeId, writable FROM consistsof WHERE roleId = '" + roleId + "' AND nodeId = '" + nodeId + "'");
			if (rs.next())
			{
				stmt.executeUpdate("DELETE FROM consistsof WHERE roleId=" + roleId + " AND nodeId = " + nodeId );
				disconnect_db();
				return true;
			}
			else
			{
				disconnect_db();
				return false;
			}
		}
		catch( SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	public void renameRole(int roleId, String newRoleName)
	{
		try {
			connect_db();
			rs = stmt.executeQuery("UPDATE roles SET roleName=" + newRoleName + "WHERE id=" + roleId);
			disconnect_db();
		}
		catch( SQLException e)
		{
			e.printStackTrace();
			return;
		}		
	}
}
