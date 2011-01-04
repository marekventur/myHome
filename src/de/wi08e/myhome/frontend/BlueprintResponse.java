package de.wi08e.myhome.frontend;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="blueprintResponse")
public class BlueprintResponse {
	public String name;
	public int width;
	public int height;
	public String image = null;
}
