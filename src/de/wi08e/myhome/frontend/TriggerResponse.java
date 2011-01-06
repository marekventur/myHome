package de.wi08e.myhome.frontend;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="triggerResponse")
public class TriggerResponse {
	public NodeResponse trigger;
	public String channel;
	
	public TriggerResponse() {
		
	}
	
	public TriggerResponse(NodeResponse trigger, String channel) {
		super();
		this.trigger = trigger;
		this.channel = channel;
	}
	
	
}
