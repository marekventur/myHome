package de.wi08e.myhome.statusmanager;

import de.wi08e.myhome.model.Node;

public interface StatusChangeReceiver {
	public void statusChanged(Node node, String key, String value);
}
