package de.wi08e.myhome.model;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

import javax.imageio.ImageIO;

import de.wi08e.myhome.database.Database;

public class Snapshot {
	private Image image;
	private Time time;
	private Node node;
	private String title;
	private int id = 0;
	
	public Snapshot(Image image, Node node, String title) {
		super();
		this.image = image;
		time = null;
		this.node = node;
		this.title = title;
	}
	
	public Snapshot(ResultSet resultSet) throws SQLException {
		id = resultSet.getInt("snapshot_id");
		node = new NamedNode(resultSet);
		time = resultSet.getTime("time");
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

	public Time getTime() {
		return time;
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
