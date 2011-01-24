package de.wi08e.myhome.nodeplugins.camera;


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

import de.wi08e.myhome.blueprintmanager.ImageHelper;
	import de.wi08e.myhome.database.Database;
import de.wi08e.myhome.model.Blueprint;

	/**
	 * @author Nico
	 * 
	 */
	public class ImageManager {

		private Database database;

		public ImageManager(Database database) {
			this.database = database;
		}
/*
		public void addImage(String name, Image image) {

			try {
				PreparedStatement insertImage = database
						.getConnection()
						.prepareStatement(
								"INSERT INTO image (name, width, height, image) VALUES (?, ?, ?, ?);");
				insertImage.setString(1, name);
				insertImage.setInt(2, image.getWidth(null));
				insertImage.setInt(3, image.getHeight(null));

				Blob imageBlob = database.getConnection().createBlob();
				ByteArrayOutputStream bs = new ByteArrayOutputStream();
				ImageIO.write(ImageHelper.toBufferedImage(image), "png", bs);
				imageBlob.setBytes(1, bs.toByteArray());
				bs.flush();
				bs.close();

				insertImage.setBlob(4, imageBlob);

				insertImage.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public List<Blueprint> getAllImages() {
			List<Blueprint> result = new ArrayList<Blueprint>();

			try {
				Statement getBlueprints = database.getConnection()
						.createStatement();
				if (getBlueprints
						.execute("SELECT id, name, width, height FROM image;")) {
					ResultSet rs = getBlueprints.getResultSet();
					while (rs.next())
						result.add(new Blueprint(rs));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			return result;
		}

		public Blueprint getImage() {
			try {
				Statement getImage= database.getConnection()
						.createStatement();
				if (getImage
						.execute("SELECT id, name, width, height, image FROM image;")) {
					ResultSet rs = getImage.getResultSet();
					if (rs.next())
						return new Blueprint(rs);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public boolean deleteBlueprint(int ImageId) {
			try {
				PreparedStatement deleteBlueprint = database
					.getConnection()
					.prepareStatement("DELETE FROM image WHERE id = ?;");

				deleteBlueprint.setInt(1, ImageId);
				return (deleteBlueprint.executeUpdate() == 1);

			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		

		public Blueprint getImage(int imageId, int height, int width) {
			try {
				Statement getBlueprints = database.getConnection()
						.createStatement();
				if (getBlueprints
						.execute("SELECT id, name, width, height, image FROM image WHERE id="
								+ String.valueOf(imageId) + ";")) {
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

						//preview(resizedImage);
						blueprint.setImage(resizedImage);

						return blueprint;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
*/
}
