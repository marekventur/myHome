package de.wi08e.myhome.scriptmanager;

import de.wi08e.myhome.nodeplugins.NodePluginManager;
import de.wi08e.myhome.usermanager.UserManager;

public class ScriptingUsers {
	private UserManager userManager;
	private NodePluginManager nodePluginManager;
	
	public ScriptingUsers(UserManager userManager, NodePluginManager nodePluginManager ) {
		this.userManager = userManager;
		this.nodePluginManager = nodePluginManager;
	}
	
	public ScriptingUser getUser(String username) {
		return new ScriptingUser(username, userManager, nodePluginManager);
	}
}
