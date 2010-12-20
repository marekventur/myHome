/**
 * 
 */
package de.wi08e.myhome.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author Marek
 * 
 */

public class Database {

	public Database() {
		Session session = null;

		try {
			// This step will read hibernate.cfg.xml

			SessionFactory sessionFactory = new	Configuration().configure().buildSessionFactory();
			session = sessionFactory.openSession();
			// Create new instance of Contact and set

			System.out.println("Inserting Record");
			Test test = new Test();
			test.setId(3);
			test.setText("Text");
			session.save(test);
			System.out.println("Done");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// Actual contact insertion will happen at this step
			session.flush();
			session.close();

		}

	}

	public static void main(String[] args) {
		new Database();
	}

}
