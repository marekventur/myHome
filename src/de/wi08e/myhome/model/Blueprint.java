/**
 * 
 */
package de.wi08e.myhome.model;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import de.wi08e.myhome.blueprintmanager.BlueprintManager;
import de.wi08e.myhome.database.Database;


/**
 * 
 * 
 * @author Marek
 *
 */
public class Blueprint {

	private int databseId;
	private String name;
	private int width;
	private int height;
	private Image image = null;
	private List<BlueprintLink> blueprintLinks = Collections.synchronizedList(new ArrayList<BlueprintLink>());
	
	public Blueprint() {
		
	}
	
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
	
	public List<BlueprintLink> getBlueprintLinks() {
		return blueprintLinks;
	}

	public void preview() {
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(null);
		frame.setSize(width+20, height+60);
		
		
		
		for (BlueprintLink link: blueprintLinks) {
			JButton button = new JButton(link.getName());
			System.out.println(link.getName());
			System.out.println((int)Math.round(getWidth()*link.getX()));
			
			frame.getContentPane().add(button);
			
			button.setBounds((int)Math.round(getWidth()*link.getX())-20, 
					(int)Math.round(getHeight()*link.getY())-10,
					40, 
					20);
			
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(null, ((JButton)e.getSource()).getText());
				}
				
			});
			
		}
		
		JLabel label = new JLabel(new ImageIcon(image));
		frame.getContentPane().add(label);
		label.setBounds(0, 0, width, height);
		
		
		frame.setVisible(true);
	}
	
	

}
