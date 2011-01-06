package de.wi08e.myhome.model;

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
