package de.wi08e.myhome.blueprintmanager;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import de.wi08e.myhome.ImageHelper;
import de.wi08e.myhome.database.Database;
import de.wi08e.myhome.exceptions.BlueprintNotFound;
import de.wi08e.myhome.exceptions.NodeNotFound;
import de.wi08e.myhome.model.Blueprint;
import de.wi08e.myhome.model.BlueprintLink;

/**
 * @author Marek
 * 
 */
public class BlueprintManager {

	private Database database;

	public BlueprintManager(Database database) {
		this.database = database;
	}

	public int addBlueprint(String name, Image image) {

		try {

			PreparedStatement insertBlueprint = database

					.getConnection()
					.prepareStatement(
							"INSERT INTO blueprint (name, width, height, image) VALUES (?, ?, ?, ?);");

			insertBlueprint.setString(1, name);
			insertBlueprint.setInt(2, image.getWidth(null));
			insertBlueprint.setInt(3, image.getHeight(null));

			Blob imageBlob = database.getConnection().createBlob();
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			ImageIO.write(ImageHelper.toBufferedImage(image), "png", bs);
			imageBlob.setBytes(1, bs.toByteArray());
			bs.flush();
			bs.close();

			insertBlueprint.setBlob(4, imageBlob);

			insertBlueprint.executeUpdate();
			
			// Get this id
			Statement getId = database.getConnection().createStatement();
			getId.execute("SELECT LAST_INSERT_ID()");
			ResultSet rs2 = getId.getResultSet();
			rs2.first();
			int blueprintId = rs2.getInt(1);
			getId.close();
			return blueprintId;

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}


	public List<Blueprint> getAllBlueprints() {
		List<Blueprint> result = new ArrayList<Blueprint>();

		try {
			Statement getBlueprints = database.getConnection()
					.createStatement();
			if (getBlueprints
					.execute("SELECT id, name, width, height FROM blueprint;")) {
				ResultSet rs = getBlueprints.getResultSet();
				while (rs.next())
					result.add(new Blueprint(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public Blueprint getBlueprint() {
		try {
			Statement getBlueprints = database.getConnection()
					.createStatement();
			if (getBlueprints
					.execute("SELECT id, name, width, height, image FROM blueprint;")) {
				ResultSet rs = getBlueprints.getResultSet();
				if (rs.next())
					return new Blueprint(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @return true if successfull, false if not (id not found)
	 * @throws BlueprintNotFound 
	 */
	public void renameBlueprint(int blueprintId, String name) throws BlueprintNotFound {
		try {
			PreparedStatement updateBlueprint = database
				.getConnection()
				.prepareStatement("UPDATE blueprint SET name = ? WHERE id = ?;");

			updateBlueprint.setString(1, name);
			updateBlueprint.setInt(2, blueprintId);
			if (updateBlueprint.executeUpdate() == 0)
				throw new BlueprintNotFound();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new BlueprintNotFound();
		}
	}
	
	public void deleteBlueprint(int blueprintId) throws BlueprintNotFound {
		try {
			PreparedStatement deleteBlueprint = database
				.getConnection()
				.prepareStatement("DELETE FROM blueprint WHERE id = ?;");

			deleteBlueprint.setInt(1, blueprintId);
			if (deleteBlueprint.executeUpdate() == 0)
				throw new BlueprintNotFound();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	public Blueprint getBlueprint(int blueprintId, int height, int width) {
		
		try {
			Statement getBlueprints = database.getConnection()
					.createStatement();
			if (getBlueprints
					.execute("SELECT id, name, width, height, image FROM blueprint WHERE id="
							+ String.valueOf(blueprintId) + ";")) {
				ResultSet rs = getBlueprints.getResultSet();
				if (rs.next()) {
					Blueprint blueprint = new Blueprint(rs);
					rs.close();

					int newHeight = height;
					int newWidth = width;

					double xscale = (double)blueprint.getWidth() / (double)width;
					double yscale = (double)blueprint.getHeight() / (double)height;
					if (yscale > xscale) {
						newWidth = (int) Math.round(blueprint.getWidth()
								* (1 / yscale));
						newHeight = (int) Math.round(blueprint.getHeight()
								* (1 / yscale));
					} else {
						newWidth = (int) Math.round(blueprint.getWidth()
								* (1 / xscale));
						newHeight = (int) Math.round(blueprint.getHeight()
								* (1 / xscale));
					}
					
					Image resizedImage = ImageHelper.getScaledInstance(ImageHelper.toBufferedImage(blueprint.getImage()), newWidth, newHeight, RenderingHints.VALUE_INTERPOLATION_BICUBIC, true);
					
					blueprint.setImage(resizedImage);
					blueprint.setHeight(newHeight);
					blueprint.setWidth(newWidth);
					
					/* Get links */
					String getLinksSQL = "SELECT " +
								"l.id, l.pos_x, l.pos_y, " +
								"b2.id as 'referring_blueprint_id', b2.name " +
							"FROM " +
								"blueprint b " +
									"LEFT JOIN " +
								"blueprint_links_blueprint l " +
									"ON " +
								"b.id = l.drawn_on_blueprint_id " +
									"LEFT JOIN " +
								"blueprint b2 " +
									"ON " +
								"l.referring_blueprint_id = b2.id " +
							"WHERE b.id="+String.valueOf(blueprintId) + ";";
					
					Statement getLinks = database.getConnection().createStatement();
					if (getLinks.execute(getLinksSQL)) {
						ResultSet rs2 = getLinks.getResultSet();
						while (rs2.next()) {
							blueprint.getBlueprintLinks().add(new BlueprintLink(rs2));
						}
						rs.close();
					}
					
					
					//blueprint.preview();
					
					
					return blueprint;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int addLink(int blueprintId, int linkingBlueprintId, float x, float y) throws BlueprintNotFound {
		try {

			PreparedStatement insertBlueprintLink = database.getConnection().prepareStatement(
							"INSERT INTO blueprint_links_blueprint (drawn_on_blueprint_id, referring_blueprint_id, pos_x, pos_y) VALUES (?, ?, ?, ?);");

			insertBlueprintLink.setInt(1, blueprintId);
			insertBlueprintLink.setInt(2, linkingBlueprintId);
			insertBlueprintLink.setFloat(3, x);
			insertBlueprintLink.setFloat(4, y);

			insertBlueprintLink.executeUpdate();
			
			// Get this id
			Statement getId = database.getConnection().createStatement();
			if (getId.execute("SELECT LAST_INSERT_ID()")) {
				ResultSet rs2 = getId.getResultSet();
				rs2.first();
				return rs2.getInt(1);
			}	

		} catch (SQLException e) {
			if (e.getMessage().contains("a foreign key constraint fails")) {
				throw new BlueprintNotFound();
			}
			else
			{
				e.printStackTrace();
			}
		}
		
		return 0;
	}
	

}
