package de.wi08e.myhome.frontend;

import javax.xml.bind.annotation.XmlRootElement;

import de.wi08e.myhome.model.NamedNode;
import de.wi08e.myhome.model.Node;
import de.wi08e.myhome.model.NodeWithPosition;

/**
 * @author Thilo_Gerheim
 */

@XmlRootElement(name="nodeResponse")
public class NodeResponse {
	public int id;
	public String category;
	public String manufacturer;
	public String hardwareId;
	public String name;
	public String type;
	public NodeStatusResponse[] status = null;
	public String[] tags;
	
	/* Only be used in combination with blueprint  */
	private Float x;
	private Float y;
	
	
	public NodeResponse() {
		
	}
	
	/**
	 * Initiate from node-object and abstract everything
	 * @param node gets the parameter defined in the Database
	 */
	public NodeResponse(Node node) {
		id = node.getDatabaseId();
		category = node.getCategory();
		manufacturer = node.getManufacturer();
		hardwareId = node.getHardwareId();
		type = node.getType();
		
		if (node instanceof NamedNode) {
			name = ((NamedNode)node).getName();
		}
		
		tags = new String[node.getTags().size()];
		int i = 0;
		for (String tag: node.getTags())
			tags[i++] = tag;
		
		
		status = new NodeStatusResponse[node.getStatus().keySet().size()];
		i=0;
		for (String key: node.getStatus().keySet()) 
			status[i++] = new NodeStatusResponse(key, node.getStatus().get(key));
		
		
		//{new NodeStatusResponse("key1", "value1"), new NodeStatusResponse("key2", "value2")}; 
		
	
	}

	/**
	 * @param node
	 */
	public NodeResponse(NodeWithPosition nodeWithPosition) {
		this(nodeWithPosition.getNode());
		System.out.println(nodeWithPosition.getX());
		x = nodeWithPosition.getX();
		y = nodeWithPosition.getY();
	}

	public Float getX() {
		return x;
	}

	public void setX(Float x) {
		this.x = x;
	}

	public Float getY() {
		return y;
	}

	public void setY(Float y) {
		this.y = y;
	}

	
	
}
