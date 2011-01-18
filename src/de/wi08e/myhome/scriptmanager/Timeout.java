package de.wi08e.myhome.scriptmanager;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class Timeout extends TimerTask {

	private ScriptEngine engine;
	private Object code;
	private Map<String, Timeout> timeoutList;

	private boolean running = true;
	
	public Timeout(ScriptEngine engine, Object code, Map<String, Timeout> timeoutList) {
		super();
		this.engine = engine;
		this.code = code;
		this.timeoutList = timeoutList;
	}

	@Override
	public void run() {
		
		if (running) {
			String functionName = "function_"+String.valueOf(Math.round(Math.random()*1000));
			
			engine.put(functionName, code);
			try {
				engine.eval(functionName+"();");
			} catch (ScriptException e) {
				e.printStackTrace();
			}
			

			for (Entry<String, Timeout> entry: timeoutList.entrySet()) 
				if (entry.getValue() == this)
					timeoutList.remove(entry.getKey());
			
		}
		
	}
	
	public void stop() {
		running = false;
		for (Entry<String, Timeout> entry: timeoutList.entrySet()) 
			if (entry.getValue() == this)
				timeoutList.remove(entry.getKey());
	}

}
