package de.wi08e.myhome.frontend;

import javax.xml.bind.annotation.XmlRootElement;

import de.wi08e.myhome.model.NamedNode;
import de.wi08e.myhome.model.Node;

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
	public float positionX = 0;
	public float positionY = 0;
	public int blueprintId = -1;
	public NodeStatusResponse[] status = null;
	public String[] tags;
	
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
			blueprintId = ((NamedNode)node).getBlueprintId();
			positionX = ((NamedNode)node).getPositionX();
			positionY = ((NamedNode)node).getPositionY();
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
}
