package de.wi08e.myhome.scriptmanager;

import de.wi08e.myhome.communicationplugins.CommunicationManager;
import de.wi08e.myhome.usermanager.UserManager;

public class ScriptingUser {
	
	private UserManager userManager;
	private String username;
	private CommunicationManager communicationManager;
	
	public ScriptingUser(String username, UserManager userManager, CommunicationManager communicationManager) {
		this.username = username;
		this.userManager = userManager;
		this.communicationManager = communicationManager;
	}
	
	public void sendMessage(String text) {
		communicationManager.sendMessage(username, text);
	}
}
