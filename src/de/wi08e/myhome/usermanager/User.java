package benutzerverwaltung;

public class User {

	private int id;
	private String name;
	private boolean isAdmin;
	private String preferredCom;
	private int roleId;
	
	public User()
	{
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public void setUserName(String name)
	{
		this.name = name;
	}
	public void setRoleId(int role)
	{
		this.roleId = role;
	}
	public void setAdmin(boolean isAdmin)
	{
		this.isAdmin = isAdmin;
	}
	public void setPreferredCom(String prefCom)
	{
		this.preferredCom = prefCom;
	}
	
	public int getID()
	{
		return this.id;
	}
	public String getUserName()
	{
		return this.name;
	}
	public boolean getIsAdmin()
	{
		return this.isAdmin;
	}
	public String getPrefCom()
	{
		return this.preferredCom;
	}
	public int getRoleId()
	{
		return this.roleId;
	}

}
