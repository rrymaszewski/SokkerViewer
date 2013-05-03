package pl.pronux.sokker.data.properties;

import java.io.File;
import java.io.IOException;

public class PropertiesDatabase {

	private static PropertiesSession session;

	private static String fileName = System.getProperty("user.dir") + File.separator + "settings" + File.separator + "sokker.properties";

	public static PropertiesSession getSession() throws IOException {
		if (session != null) {
			return session;
		} else {
			session = new PropertiesSession();
			session.init(fileName);
			return session;
		}
	}

	public static void reload() throws IOException {
		session.init(fileName);
	}
}
