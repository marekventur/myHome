package de.wi08e.myhome.scriptmanager;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import javax.script.ScriptEngine;

public class ScriptingSystem {
	
	private ScriptEngine scriptingEngine;
	private static Map<String, Timeout> timeoutList = Collections.synchronizedMap(new HashMap<String, Timeout>()); 
	private Timer timer = new Timer();
	
	
	public ScriptingSystem(ScriptEngine scriptingEngine) {
		super();
		this.scriptingEngine = scriptingEngine;
	}

	public Date getTime() {
		return null;
	}
	
	public String setTimeout(Object code, int milliseconds) {
		String id = Long.toHexString(Math.round(Math.random()*100000));
		setTimeout(code, milliseconds, id);
		return id;
	}
	
	public void setTimeout(Object code, int milliseconds, String id) {
		Timeout timeout = new Timeout(scriptingEngine, code, timeoutList);
	    timer.schedule (timeout, milliseconds);
	    timeoutList.put(id, timeout);
	}
	
	public void clearTimeout(String id) {
		Timeout timeout = timeoutList.get(id);
		if (timeout != null) 
			timeout.stop();
	}
	
	public void alert(String text) {
		System.out.println(text);
	}
}
