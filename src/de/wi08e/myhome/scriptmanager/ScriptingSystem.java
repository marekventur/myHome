package de.wi08e.myhome.scriptmanager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import javax.script.ScriptEngine;

public class ScriptingSystem {
	
	private ScriptEngine scriptingEngine;
	private Map<String, Timer> timeoutList = new HashMap<String, Timer>(); 
	
	
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
		Timer timer = new Timer();
	    timer.schedule (new Timeout(scriptingEngine, code, timeoutList, id), milliseconds );
	    timeoutList.put(id, timer);
	}
	
	public void clearTimeout(String id) {
		Timer timer = timeoutList.get(id);
		timer.cancel();
		timeoutList.remove(id);
	}
	
	public void alert(String text) {
		System.out.println(text);
	}
}
