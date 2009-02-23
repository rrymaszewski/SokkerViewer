package pl.pronux.sokker.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
/**
 * @author rym3k
 *
 */
final public class PropertiesResources {
	private static final HashMap<String, Properties> cache = new HashMap<String, Properties>();

	private static final String PROPERTIES_PATH = "/properties/"; //$NON-NLS-1$

	private PropertiesResources() {
	}

	public static Properties getProperties(String filename) throws IOException {
		Properties properties = cache.get(filename);
		if(properties == null) {
			properties = loadProperties(filename);
			cache.put(filename, properties);
		}
		return properties;
	}
	
	private static Properties loadProperties(String filename) throws IOException {
		Properties properties = new Properties();
		InputStream is = PropertiesResources.class.getResourceAsStream(PROPERTIES_PATH + filename);
		if (is != null) {
			properties.load(is);
		}
		return properties;
	}
}
