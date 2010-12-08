package de.wi08e.myhome.frontend.exceptions;

import org.apache.xmlrpc.XmlRpcException;

public class FrontendException extends XmlRpcException {

	public FrontendException(int code, String text) {
		super(code, text);
	}

}
