/**
 * 
 */
package de.wi08e.myhome.model;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;

import de.wi08e.myhome.blueprintmanager.BlueprintManager;
import de.wi08e.myhome.database.Database;


/**
 * 
 * @author marek_ventur
 *
 */
public class Blueprint {

	private int databseId;
	private String name;
	private int width;
	private int height;
	private Image image = null;
	
	public Blueprint() {
		
	}
	/**
	 * 
	 * @param resultSet creates a node of resultset
	 * @throws SQLException
	 */
	public Blueprint(ResultSet resultSet) throws SQLException {
		databseId = resultSet.getInt("id");
		name = resultSet.getString("name");
		width = resultSet.getInt("width");
		height = resultSet.getInt("height");
		
		if (Database.columnExist(resultSet, "image")) {
		
			InputStream imageStream = resultSet.getBinaryStream("image");
			try { 
				image = ImageIO.read(imageStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * determining the fowlloing parameters
	 * @param databseId 
	 * @param name
	 * @param width
	 * @param height
	 * @param image
	 */
	public Blueprint(int databseId, String name, int width, int height,
			Image image) {
		super();
		this.databseId = databseId;
		this.name = name;
		this.width = width;
		this.height = height;
		this.image = image;
	}

	public int getDatabseId() {
		return databseId;
	}

	public void setDatabseId(int databseId) {
		this.databseId = databseId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	
	
	

}
