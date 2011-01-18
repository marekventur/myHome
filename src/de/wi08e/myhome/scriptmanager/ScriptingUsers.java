package de.wi08e.myhome.scriptmanager;

import de.wi08e.myhome.communicationplugins.CommunicationManager;
import de.wi08e.myhome.nodeplugins.NodePluginManager;
import de.wi08e.myhome.usermanager.UserManager;

public class ScriptingUsers {
	private UserManager userManager;
	private CommunicationManager communicationManager;
	
	public ScriptingUsers(UserManager userManager, CommunicationManager communicationManager ) {
		this.userManager = userManager;
		this.communicationManager = communicationManager;
	}
	
	public ScriptingUser getUser(String username) {
		return new ScriptingUser(username, userManager, communicationManager);
	}
}
