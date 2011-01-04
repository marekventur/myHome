package de.wi08e.myhome.frontend.exceptions;

public class NodeNotFound extends FrontendException {
	private static final long serialVersionUID = 1L;

	public NodeNotFound() {
		super(9, "NodeNotFound", "The requested Node is not found");
	}
}
