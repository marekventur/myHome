/**
 * 
 */
package de.wi08e.myhome;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Marek
 *
 */
public class XMLHelper {
	
	public static String readSimpleTag(Node node, String tagName, String defaultResult) {
		NodeList nodes = ((Element) node).getElementsByTagName(tagName);
		if (nodes.getLength() == 0)
			return defaultResult;
		org.w3c.dom.Node nodeInner = nodes.item(0);
		
		return nodeInner.getChildNodes().item(0).getNodeValue().toString();
	}
	
	public static String readSimpleTag(Node node, String tagName) {
		return readSimpleTag(node, tagName, null);
	}
	
	public static <E extends Exception> String readSimpleTagException(Node node, String tagName, E e) throws E {
		String result = readSimpleTag(node, tagName);
		if (result == null)
			throw e;
		
		return result;
	}
}
