package de.wi08e.myhome.scriptmanager;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class Timeout extends TimerTask {

	private ScriptEngine engine;
	private Object code;
	private Map<String, Timer> timeoutList;
	private String id; 
	
	public Timeout(ScriptEngine engine, Object code, Map<String, Timer> timeoutList, String id) {
		super();
		this.engine = engine;
		this.code = code;
		this.timeoutList = timeoutList;
		this.id = id;
	}

	@Override
	public void run() {
		
		String functionName = "function_"+String.valueOf(Math.round(Math.random()*1000));
		
		engine.put(functionName, code);
		try {
			engine.eval(functionName+"();");
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		timeoutList.remove(id);
	}

}
