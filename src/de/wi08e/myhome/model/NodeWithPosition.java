/**
 * 
 */
package de.wi08e.myhome.model;

/**
 * @author Marek
 *
 */
public class NodeWithPosition {
	private Node node;
	private float x;
	private float y;
	
	public NodeWithPosition(Node node, float x, float y) {
		super();
		this.node = node;
		this.x = x;
		this.y = y;
	}
	
	public Node getNode() {
		return node;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}	
}
