/**
 * 
 */
package de.wi08e.myhome.myhomescript;

import java.util.Random;

/**
 * @author Marek
 *
 */
public class ScriptingSensor extends ScriptingNode{
	public ScriptingSensor() {
		
	}
	
	/**
	 * Returns the temperature
	 * @return Temperature in Celsius, null when no applicable
	 */
	public Double getTemperatureCelsius() {
		return 61.4;
	}
	
	/**
	 * Returns whether window is open
	 * @return true when open, false when closed, null when no applicable
	 */
	public Boolean isWindowOpen() {
		return new Random().nextBoolean();
	}
	
	public Boolean isLightOn() {
		return null;
	}
	
	/**
	 * 
	 * @return ""
	 */
	public String getType() {
		return "sensorWindowOpener";
	}
	
	public Boolean isButtonSwitchedOn(String button) {
		return true;
	}
	
	public int lastButtonPush(String button) {
		return 1234;
	}
	
	
}
