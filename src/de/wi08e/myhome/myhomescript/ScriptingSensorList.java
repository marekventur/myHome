/**
 * 
 */
package de.wi08e.myhome.myhomescript;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Marek
 *
 */
public class ScriptingSensorList {
	
	private List<ScriptingSensor> list;
	
	public ScriptingSensorList() {
		list = new ArrayList<ScriptingSensor>();
		list.add(new ScriptingSensor());
		list.add(new ScriptingSensor());
		list.add(new ScriptingSensor());
		list.add(new ScriptingSensor());
	}
	
	public ScriptingSensorList(List<ScriptingSensor> list) {
		this.list = list;
	}
	
	public ScriptingSensor getByName(String name) {
		return list.get(0);
	}
	
	public ScriptingSensorList filterByType(String type) {
		ArrayList<ScriptingSensor> result = new ArrayList<ScriptingSensor>();
		result.add(list.get(0));
		result.add(list.get(0));
		return new ScriptingSensorList(result);
	}
	
	
	public ScriptingSensorList filterByRoom(String type) {
		ArrayList<ScriptingSensor> result = new ArrayList<ScriptingSensor>();
		result.add(list.get(0));
		result.add(list.get(0));
		return new ScriptingSensorList(result);
	}
	
	public int size() {
		return list.size();
	}
	
	public ScriptingSensor first() {
		return list.get(0);
	}
	
	public ScriptingSensor get(int index) {
		return list.get(index);
	}
}
