package de.wi08e.myhome.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.wi08e.myhome.nodemanager.NodeManager;

public class Trigger {
	private Node sender;
	private Node receiver;
	private char channel = 0;
	
	public Trigger(Node sender, Node receiver, char channel) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.channel = channel;
	}
	
	public Trigger(Node sender, Node receiver) {
		super();
		this.sender = sender;
		this.receiver = receiver;
	}
	
	public Trigger(ResultSet resultSet, NodeManager nodeManager) throws SQLException {
		this((resultSet.findColumn("sender_node_id")==0)?null:nodeManager.getNode(resultSet.getInt("sender_node_id"), false), 
				(resultSet.findColumn("receiver_node_id")==0)?null:nodeManager.getNode(resultSet.getInt("receiver_node_id"), false), 
				(char)0);
		if (resultSet.getString("channel")!=null && resultSet.getString("channel").length()>0)
			this.channel = resultSet.getString("channel").charAt(0);	
	}

	public Node getSender() {
		return sender;
	}

	public Node getReceiver() {
		return receiver;
	}

	public char getChannel() {
		return channel;
	}
	
	
}
