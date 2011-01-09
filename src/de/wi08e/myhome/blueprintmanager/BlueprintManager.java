package de.wi08e.myhome.blueprintmanager;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
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

import de.wi08e.myhome.database.Database;
import de.wi08e.myhome.model.Blueprint;

/**
 * @author Marek
 * 
 */
public class BlueprintManager {

	private Database database;

	public BlueprintManager(Database database) {
		this.database = database;
	}

	public void addBlueprint(String name, Image image) {

		try {
			PreparedStatement insertTrigger = database
					.getConnection()
					.prepareStatement(
							"INSERT INTO blueprint (name, width, height, image) VALUES (?, ?, ?, ?);");

			insertTrigger.setString(1, name);
			insertTrigger.setInt(2, image.getWidth(null));
			insertTrigger.setInt(3, image.getHeight(null));

			Blob imageBlob = database.getConnection().createBlob();
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			ImageIO.write(ImageHelper.toBufferedImage(image), "png", bs);
			imageBlob.setBytes(1, bs.toByteArray());
			bs.flush();
			bs.close();

			insertTrigger.setBlob(4, imageBlob);

			insertTrigger.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void preview(Image image) {
		JFrame frame = new JFrame();
		JLabel label = new JLabel(new ImageIcon(image));
		frame.getContentPane().add(label, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
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

					int newHeight = height;
					int newWidth = width;

					double xscale = blueprint.getWidth() / width;
					double yscale = blueprint.getHeight() / height;
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

					preview(resizedImage);
					blueprint.setImage(resizedImage);

					return blueprint;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	

}
