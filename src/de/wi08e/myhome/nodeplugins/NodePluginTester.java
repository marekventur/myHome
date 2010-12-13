/**
 * 
 */
package de.wi08e.myhome.nodeplugins;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import de.wi08e.myhome.httpserver.HTTPServer;


/**
 * @author Marek
 *
 */
public class NodePluginTester {
	public static void main(String[] args) throws IOException, SecurityException, NoSuchMethodException, ClassNotFoundException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		//NodePluginLoader.addFile("nodeplugins/german.jar");
		NodePluginLoader.addFile("nodeplugins/german.jar");
		
		Class<?> loadedClass = ClassLoader.getSystemClassLoader()
				.loadClass("de.wi08e.myhome.nodeplugins.german.German");
		
		
		Constructor<?> cs = loadedClass.getConstructor();
		
		try {
			NodePlugin plugin = (NodePlugin)cs.newInstance();
			System.out.println(plugin.greet("Marek"));
		} catch (java.lang.ClassCastException e) {
			System.out.println("Can't cast to de.wi08e.myhome.nodeplugins.NodePlugin");
		}
	}
}
