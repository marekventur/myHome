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
public class BlueprintLink {
	private float x;
	private float y;
	private String name;
	private int id;
	private int referringBlueprintId;
	
	public BlueprintLink(float x, float y, String name, int id,
			int referringBlueprintId) {
		super();
		this.x = x;
		this.y = y;
		this.name = name;
		this.id = id;
		this.referringBlueprintId = referringBlueprintId;
	}
	
	public BlueprintLink(ResultSet rs) throws SQLException {
		super();
		this.x = rs.getFloat("pos_x");
		this.y = rs.getFloat("pos_y");
		this.name = rs.getString("name");
		this.id = rs.getInt("id");
		this.referringBlueprintId = rs.getInt("referring_blueprint_id");
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public int getReferringBlueprintId() {
		return referringBlueprintId;
	}
	
	
}
