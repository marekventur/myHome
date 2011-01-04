package de.wi08e.myhome.frontend;

import javax.xml.bind.annotation.XmlRootElement;

import de.wi08e.myhome.model.NamedNode;
import de.wi08e.myhome.model.Node;

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
	
	public NodeResponse() {
		
	}
	
	/**
	 * Initiate from node-object and abrsctact everything
	 * @param node
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
		
		
		status = new NodeStatusResponse[node.getStatus().keySet().size()];
		int i=0;
		for (String key: node.getStatus().keySet()) 
			status[i++] = new NodeStatusResponse(key, node.getStatus().get(key));
		
		
		//{new NodeStatusResponse("key1", "value1"), new NodeStatusResponse("key2", "value2")}; 
		
	
	}
}
