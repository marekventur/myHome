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
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
			ImageIO.write(toBufferedImage(image), "png", bs);
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

					/* resizedImage = new BufferedImage(newWidth, newHeight,
							BufferedImage.TYPE_INT_RGB);*/					
					Image resizedImage = getScaledInstance(toBufferedImage(blueprint.getImage()), newWidth, newHeight, RenderingHints.VALUE_INTERPOLATION_BICUBIC, true);

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

	/**
	 * Helper function
	 * 
	 * @param image
	 * @return
	 */
	private static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}

		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();

		// Determine if the image has transparent pixels
		boolean hasAlpha = hasAlpha(image);

		// Create a buffered image with a format that's compatible with the
		// screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;
			if (hasAlpha == true) {
				transparency = Transparency.BITMASK;
			}

			// Create the buffered image
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null),
					image.getHeight(null), transparency);
		} catch (HeadlessException e) {
		} // No screen

		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			if (hasAlpha == true) {
				type = BufferedImage.TYPE_INT_ARGB;
			}
			bimage = new BufferedImage(image.getWidth(null),
					image.getHeight(null), type);
		}

		// Copy image to buffered image
		Graphics g = bimage.createGraphics();

		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	}

	public static boolean hasAlpha(Image image) {
		// If buffered image, the color model is readily available
		if (image instanceof BufferedImage) {
			return ((BufferedImage) image).getColorModel().hasAlpha();
		}

		// Use a pixel grabber to retrieve the image's color model;
		// grabbing a single pixel is usually sufficient
		PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
		}

		// Get the image's color model
		return pg.getColorModel().hasAlpha();
	}

	public BufferedImage getScaledInstance(BufferedImage img, int targetWidth,
			int targetHeight, Object hint, boolean higherQuality) {
		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = (BufferedImage) img;
		int w, h;
		if (higherQuality) {
			// Use multi-step technique: start with original size, then
			// scale down in multiple passes with drawImage()
			// until the target size is reached
			w = img.getWidth();
			h = img.getHeight();
		} else {
			// Use one-step technique: scale directly from original
			// size to target size with a single drawImage() call
			w = targetWidth;
			h = targetHeight;
		}

		do {
			if (higherQuality && w > targetWidth) {
				w /= 2;
				if (w < targetWidth) {
					w = targetWidth;
				}
			}

			if (higherQuality && h > targetHeight) {
				h /= 2;
				if (h < targetHeight) {
					h = targetHeight;
				}
			}

			BufferedImage tmp = new BufferedImage(w, h, type);
			Graphics2D g2 = tmp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
			g2.drawImage(ret, 0, 0, w, h, null);
			g2.dispose();

			ret = tmp;
		} while (w != targetWidth || h != targetHeight);

		return ret;
	}

}
