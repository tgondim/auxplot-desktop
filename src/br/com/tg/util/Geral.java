package br.com.tg.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

public class Geral {
	
	public static Vector<String[]> getProperties(Vector<String> properties, String arquivo) throws FileNotFoundException, IOException {
		Vector<String[]> propertiesRetorno = new Vector<String[]>();  
		File file = new File(arquivo);
		Properties prop = new Properties();
		prop.load(new FileInputStream(file));
		for (int i = 0; i < properties.size(); i++) {
			String[] stringArray = {  properties.get(i), prop.getProperty(properties.get(i)) };
			propertiesRetorno.set(i, stringArray);
		}
		return propertiesRetorno;
	}
	
}
