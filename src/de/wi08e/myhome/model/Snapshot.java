package de.wi08e.myhome.model;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.imageio.ImageIO;

import de.wi08e.myhome.database.Database;

public class Snapshot {
	private Image image;
	private Date date;
	private Node node;
	private String title;
	private int id = 0;
	
	public Snapshot(Image image, Node node, String title) {
		super();
		this.image = image;
		date = null;
		this.node = node;
		this.title = title;
	}
	
	public Snapshot(ResultSet resultSet) throws SQLException {
		id = resultSet.getInt("snapshot_id");
		node = new NamedNode(resultSet);
		date = resultSet.getDate("time");
		title = resultSet.getString("title");
		if (Database.columnExist(resultSet, "image")) {
			InputStream imageStream = resultSet.getBinaryStream("image");
			try { 
				image = ImageIO.read(imageStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Image getImage() {
		return image;
	}
	
	/*
	public Image getImageResized(int width, int height) {
		return image;
	}
	*/

	public Date getDate() {
		return date;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public String getTitle() {
		return title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	
}
