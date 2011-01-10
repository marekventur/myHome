/**
 * 
 */
package de.wi08e.myhome.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Marek
 *
 */
public class NamedNode extends Node {

	private String name;
	
	private float positionX = 0;
	private float positionY = 0;
	private int blueprintId = -1;
	

	public NamedNode(String type, String manufacturer, String id) {
		super(type, manufacturer, id);
		// TODO Auto-generated constructor stub
	}
	
	public NamedNode(ResultSet resultSet) throws SQLException  {
		super(resultSet);
		setName(resultSet.getString("name"));
		setPositionX(resultSet.getFloat("pos_x"));
		setPositionY(resultSet.getFloat("pos_y"));
		setBlueprintId(resultSet.getInt("blueprint_id"));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
