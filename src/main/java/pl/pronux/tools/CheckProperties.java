package pl.pronux.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import pl.pronux.sokker.data.properties.SVProperties;

public class CheckProperties {

	private static SVProperties sokkerPropertiesIn;
	private static SVProperties sokkerPropertiesOut;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String baseDir = System.getProperty("user.dir");
		File sokkerPropertiesFileIn = new File(baseDir + File.separator
				+ "properties" + File.separator + "lang" + File.separator
				+ "sokker.properties");
		File sokkerPropertiesFileOut = new File(baseDir + File.separator
				+ "properties" + File.separator + "lang" + File.separator
				+ "sokker_cs_CZ.properties");
		sokkerPropertiesIn = new SVProperties();
		sokkerPropertiesOut = new SVProperties();
		try {
			sokkerPropertiesIn.loadFile(sokkerPropertiesFileIn);
			sokkerPropertiesOut.loadFile(sokkerPropertiesFileOut);
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		
		Set<Object> setIn = sokkerPropertiesIn.keySet();
		
		Iterator<Object> itr = setIn.iterator();
		
		while(itr.hasNext()) {
			String key = (String)itr.next(); 
			String valueIn = (String)sokkerPropertiesIn.get(key);
			String valueOut = (String)sokkerPropertiesOut.get(key);
			
			if(valueOut == null || valueOut.equals("")) {
				System.out.println(key + "=" + valueIn);
			}
		}
	}

}
