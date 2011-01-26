package de.wi08e.myhome.snapshotmanager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.List;

import javax.imageio.ImageIO;

import de.wi08e.myhome.ImageHelper;
import de.wi08e.myhome.database.Database;
import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.Snapshot;

public class SnapshotManager {
	private Database database;

	public SnapshotManager(Database database) {
		super();
		this.database = database;
	}
	
	public int storeSnapshot(Snapshot snapshot) {
		try {

			PreparedStatement insertBlueprint = database

					.getConnection()
					.prepareStatement("INSERT INTO snapshot (node_id, title, image) VALUES (?, ?, ?);");

			insertBlueprint.setInt(1, snapshot.getNode().getDatabaseId());
			insertBlueprint.setString(2, snapshot.getTitle());

			Blob imageBlob = database.getConnection().createBlob();
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			ImageIO.write(ImageHelper.toBufferedImage(snapshot.getImage()), "jpg", bs);
			imageBlob.setBytes(1, bs.toByteArray());
			bs.flush();
			bs.close();
			insertBlueprint.setBlob(3, imageBlob);

			insertBlueprint.executeUpdate();
			
			// Get this id
			Statement getId = database.getConnection().createStatement();
			getId.execute("SELECT LAST_INSERT_ID()");
			ResultSet rs2 = getId.getResultSet();
			rs2.first();
			int snapshotId = rs2.getInt(1);
			getId.close();
			return snapshotId;

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public Snapshot getSnapshot(int id) {
		try {
			String sql = "SELECT s.id AS snapshot_id, s.title, s.image, s.`time`" +
					"n.id, n.name, n.type, n.database_id, n.manufacurer, n.category " +
					" FROM snapshot s LEFT JOIN node n ON s.node_id = n.id WHERE s.id=?;";
			
			PreparedStatement getSnapshot = database.getConnection().prepareStatement(sql);
			getSnapshot.setInt(1, id);
			
			
			if (getSnapshot.execute()) {
				ResultSet rs = getSnapshot.getResultSet();
				if (rs.next())
					return new Snapshot(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public List<Snapshot> getLastSnapshot(Node node, int count) {
		return null;
	}
	
	public List<Snapshot> getSnapshots(Node node, Time from, Time to) {
		return null;
	}
	
	
}
