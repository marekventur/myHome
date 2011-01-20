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

	

	public NamedNode(String type, String manufacturer, String id) {
		super(type, manufacturer, id);
	}
	
	public NamedNode(ResultSet resultSet) throws SQLException  {
		super(resultSet);
		setName(resultSet.getString("name"));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
}
