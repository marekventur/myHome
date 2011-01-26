/**
 * 
 */
package de.wi08e.myhome.frontend;

import java.awt.Image;
import java.sql.Time;
import java.util.Date;

import de.wi08e.myhome.model.Snapshot;

/**
 * @author Marek
 *
 */
public class SnapshotResponse {
	private Image image;
	private Date date;
	private NodeResponse node;
	private String title;
	private int id = 0;
	
	public SnapshotResponse() {	}
	
	public SnapshotResponse(Snapshot snapshot) {
		image = snapshot.getImage();
		date = snapshot.getDate();
		node = new NodeResponse(snapshot.getNode());
		title = snapshot.getTitle();
		id = snapshot.getId();		
	}
	
	public Image getImage() {
		return image;
	}
	
	public void setImage(Image image) {
		this.image = image;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public NodeResponse getNode() {
		return node;
	}
	public void setNode(NodeResponse node) {
		this.node = node;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	
}
