package de.wi08e.myhome.frontend;

import javax.xml.bind.annotation.XmlRootElement;

import de.wi08e.myhome.model.Trigger;

/**
 * @author Thilo_Gerheim
 */

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
/**
 * @param trigger trigger connects node with node
 */	
	public TriggerResponse(Trigger trigger) {
		super();
		this.sender = new NodeResponse(trigger.getSender());
		this.channel = trigger.getChannel()+"";
	}
}
