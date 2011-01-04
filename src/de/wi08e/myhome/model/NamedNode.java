/**
 * 
 */
package de.wi08e.myhome.model;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Marek
 *
 */
public class NamedNode extends Node {

	private String name;
	private final Set<String> tags = new HashSet<String>();
	
	private float positionX = 0;
	private float positionY = 0;
	private int blueprintId = -1;
	

	public NamedNode(String type, String manufacturer, String id) {
		super(type, manufacturer, id);
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getTags() {
		return tags;
	}

	public float getPositionX() {
		return positionX;
	}

	public void setPositionX(float positionX) {
		this.positionX = positionX;
	}

	public float getPositionY() {
		return positionY;
	}

	public void setPositionY(float positionY) {
		this.positionY = positionY;
	}

	public int getBlueprintId() {
		return blueprintId;
	}

	public void setBlueprintId(int blueprintId) {
		this.blueprintId = blueprintId;
	}
	
	
	
}
