package pl.pronux.sokker.data.properties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PropertiesDatabase {

	private static PropertiesSession session;

	private static String FILE_NAME = System.getProperty("user.dir") + File.separator + "settings" + File.separator + "sokker.properties";

	public static PropertiesSession getSession() throws FileNotFoundException, IOException {
		if (session != null) {
			return session;
		} else {
			session = new PropertiesSession();
			session.init(FILE_NAME);
			return session;
		}
	}

	public static void reload() throws FileNotFoundException, IOException {
		session.init(FILE_NAME);
	}
}
