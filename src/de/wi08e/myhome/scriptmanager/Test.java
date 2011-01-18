/**
 * 
 */
package de.wi08e.myhome.scriptmanager;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author Marek
 *
 */


class TestInner {
	public void test(Object a) {
		ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        engine.put("run", a);
        try {
			engine.eval("run('World')");
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}
}

public class Test {
/*
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
      
        /* Sensor */
        /*
        List<ScriptingSensor> sensorList = new ArrayList<ScriptingSensor>();
        sensorList.add(new ScriptingSensor());
        sensorList.add(new ScriptingSensor());
        sensorList.add(new ScriptingSensor());
        ScriptingNodeList<ScriptingSensor> sensor = new ScriptingNodeList<ScriptingSensor>(sensorList);
        */
        /* Actor */
        /*
        List<ScriptingActor> actorList = new ArrayList<ScriptingActor>();
        actorList.add(new ScriptingActor());
        actorList.add(new ScriptingActor());
        actorList.add(new ScriptingActor());
        ScriptingNodeList<ScriptingActor> actor = new ScriptingNodeList<ScriptingActor>(actorList);
        
        engine.put("Sensor", sensor);
        engine.put("Actor", actor);
        */
        engine.put("Test", new TestInner());
        engine.eval(new java.io.FileReader("test.js"));
        
        
        /*
        Invocable inv = (Invocable) engine;

        // invoke the global function named "hello"
        inv.invokeFunction("hello", "Marek" );
        */
   
    }

}
