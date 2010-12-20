/**
 * 
 */
package de.wi08e.myhome.myhomescript;

import java.util.Random;

/**
 * @author Marek
 *
 */
public class ScriptingSensor {
	public ScriptingSensor() {
		
	}
	
	public double getTemperatureCelsius() {
		return 61.4;
	}
	
	public boolean isWindowOpen() {
		return new Random().nextBoolean();
		
	}
}
