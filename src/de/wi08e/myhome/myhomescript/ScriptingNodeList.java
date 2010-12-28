package de.wi08e.myhome.myhomescript;

import java.util.ArrayList;
import java.util.List;

public class ScriptingNodeList<T extends ScriptingNode> {
	
	private List<T> list;
	
	public ScriptingNodeList(List<T> list) {
		this.list = list;
	}
	
	public T getByName(String name) {
		return list.get(0);
	}
	
	public ScriptingNodeList<T> filterByType(String type) {
		ArrayList<T> result = new ArrayList<T>();
		result.add(list.get(0));
		result.add(list.get(0));
		return new ScriptingNodeList<T>(result);
	}
	
	
	public ScriptingNodeList<T> filterByRoom(String type) {
		ArrayList<T> result = new ArrayList<T>();
		result.add(list.get(0));
		result.add(list.get(0));
		return new ScriptingNodeList<T>(result);
	}
	
	public int size() {
		return list.size();
	}
	
	public T first() {
		return list.get(0);
	}
	
	public T get(int index) {
		return list.get(index);
	}
}
