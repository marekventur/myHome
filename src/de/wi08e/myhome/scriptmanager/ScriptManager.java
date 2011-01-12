/**
 * 
 */
package de.wi08e.myhome.scriptmanager;

import de.wi08e.myhome.database.Database;
import de.wi08e.myhome.nodemanager.NodeManager;

/**
 * @author Marek
 *
 */
public class ScriptManager implements Runnable {

	private Database database;
	private NodeManager nodeManager;

	private boolean running = true;
	
	public ScriptManager(Database database, NodeManager nodeManager) {
		this.database = database;
		this.nodeManager = nodeManager;
	}
	
	@Override
	public void run() {
		while(running) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}

}
