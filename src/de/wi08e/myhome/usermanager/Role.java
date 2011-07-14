package benutzerverwaltung;

import java.util.List;


public class Role {
	private int roleId;
	private String roleName;
	private List<Right> listRight;
	
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public List<Right> getListRight() {
		return listRight;
	}
	public void setListRight(List<Right> listRight) {
		this.listRight = listRight;
	}
}