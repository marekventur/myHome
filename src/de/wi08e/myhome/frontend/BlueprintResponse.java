package de.wi08e.myhome.frontend;

import javax.xml.bind.annotation.XmlRootElement;

import de.wi08e.myhome.model.Blueprint;

@XmlRootElement(name="blueprintResponse")
public class BlueprintResponse {
	private int id;
	private String name;
	private int width;
	private int height;
	private java.awt.Image image = null;
	
	public BlueprintResponse() {
		
	}
	
	public BlueprintResponse(Blueprint blueprint) {
		id = blueprint.getDatabseId();
		name = blueprint.getName();
		width = blueprint.getWidth();
		height = blueprint.getHeight();
		image = blueprint.getImage();
	}

	public String getName() {
		return name;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public java.awt.Image getImage() {
		return image;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setImage(java.awt.Image image) {
		this.image = image;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
