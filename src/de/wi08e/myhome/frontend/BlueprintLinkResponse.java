/**
 * 
 */
package de.wi08e.myhome.frontend;

import javax.xml.bind.annotation.XmlRootElement;

import de.wi08e.myhome.model.BlueprintLink;

/**
 * @author Marek
 *
 */
@XmlRootElement(name="blueprintLinkResponse")
public class BlueprintLinkResponse {
	private float x;
	private float y;
	private String name;
	private int id;
	private int referringBlueprintId;
	private boolean primary;
	
	public BlueprintLinkResponse() {
		
	}
	
	public BlueprintLinkResponse(BlueprintLink blueprintLink) {
		this.x = blueprintLink.getX();
		this.y = blueprintLink.getY();
		this.name = blueprintLink.getName();
		this.id = blueprintLink.getId();
		this.referringBlueprintId = blueprintLink.getReferringBlueprintId();
		this.primary = blueprintLink.isPrimary();
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getReferringBlueprintId() {
		return referringBlueprintId;
	}

	public void setReferringBlueprintId(int referringBlueprintId) {
		this.referringBlueprintId = referringBlueprintId;
	}

	public boolean isPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}
	
	
}
