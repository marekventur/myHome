package de.wi08e.myhome.scriptmanager;

import de.wi08e.myhome.model.datagram.TextMessageDatagram;
import de.wi08e.myhome.nodeplugins.NodePluginManager;
import de.wi08e.myhome.usermanager.UserManager;

public class ScriptingUser {
	
	private UserManager userManager;
	private NodePluginManager nodePluginManager;
	private String username;
	
	public ScriptingUser(String username, UserManager userManager, NodePluginManager nodePluginManager) {
		this.userManager = userManager;
		this.nodePluginManager = nodePluginManager;
		this.username = username;
	}
	
	public void sendMessage(String text) {
		nodePluginManager.sendDatagram(new TextMessageDatagram(userManager.getPreferedCommunicationNode(username),text));
	}
}
