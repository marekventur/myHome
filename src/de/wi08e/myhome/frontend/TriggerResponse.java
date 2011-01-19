package de.wi08e.myhome.frontend;

import javax.xml.bind.annotation.XmlRootElement;

import de.wi08e.myhome.model.Trigger;

@XmlRootElement(name="triggerResponse")
public class TriggerResponse {
	public NodeResponse sender;
	public String channel;
	
	public TriggerResponse() { }
	
	public TriggerResponse(NodeResponse trigger, String channel) {
		super();
		this.sender = trigger;
		this.channel = channel;
	}
	
	public TriggerResponse(Trigger trigger) {
		super();
		this.sender = new NodeResponse(trigger.getSender());
		this.channel = trigger.getChannel()+"";
	}
	
	
}
