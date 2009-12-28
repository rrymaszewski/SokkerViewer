package pl.pronux.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import pl.pronux.sokker.data.properties.SVProperties;
import pl.pronux.sokker.enums.Language;

public class CheckProperties {

	private static SVProperties sokkerPropertiesIn;
	private static SVProperties sokkerPropertiesOut;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (Language language : Language.values()) {
			String baseDir = System.getProperty("user.dir");
			File sokkerPropertiesFileIn = new File(baseDir + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator
												   + "lang" + File.separator + "sokker.properties");
			File sokkerPropertiesFileOut = new File(baseDir + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator
													+ "lang" + File.separator + "sokker_" + language.name() + ".properties");
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

			System.out.println("========================== " + language.getLanguageName() + " =================================");
			
			while (itr.hasNext()) {
				String key = (String) itr.next();
				String valueIn = (String) sokkerPropertiesIn.get(key);
				String valueOut = (String) sokkerPropertiesOut.get(key);

				if (valueOut == null || valueOut.isEmpty()) {
					System.out.println(key + "=" + valueIn);
				}
			}
		}
	}
}
