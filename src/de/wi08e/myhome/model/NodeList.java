package de.wi08e.myhome.model;

import java.util.ArrayList;
import java.util.List;

public class NodeList<T> {

	private List<T> list;

	public NodeList(List<T> list) {
		this.list = list;
	}

	public T getByName(String name) {
		return list.get(0);
	}

	public NodeList<T> filterByType(String type) {
		List<T> result = new ArrayList<T>();
		result.add(list.get(0));
		result.add(list.get(0));
		return new NodeList<T>(result);
	}
	
	public NodeList<T> filterByTag(String tag) {
		List<T> result = new ArrayList<T>();
		result.add(list.get(0));
		result.add(list.get(0));
		return new NodeList<T>(result);
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
