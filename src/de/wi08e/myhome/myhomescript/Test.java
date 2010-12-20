/**
 * 
 */
package de.wi08e.myhome.myhomescript;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * @author Marek
 *
 */


public class Test {

	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
        // create a script engine manager
        ScriptEngineManager factory = new ScriptEngineManager();
        // create a JavaScript engine
        ScriptEngine engine = factory.getEngineByName("JavaScript");
      
        
        ScriptingSensorList sensor = new ScriptingSensorList();
        engine.put("Sensor", sensor);
        
        engine.eval(new java.io.FileReader("test.js"));
 
        /*
        Invocable inv = (Invocable) engine;

        // invoke the global function named "hello"
        inv.invokeFunction("hello", "Marek" );
        */
   
    }

}
