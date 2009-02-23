package pl.pronux.sokker.data.properties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PropertiesDatabase {
	private static PropertiesSession session;
	private static PropertiesSession map;
	
	private static String FILE_NAME = System.getProperty("user.dir") + File.separator + "settings" + File.separator + "sokker.properties"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	private static String LOGINS = System.getProperty("user.dir") + File.separator + "settings" + File.separator + "logins.properties"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	
	public static PropertiesSession getSession() throws FileNotFoundException, IOException {
		if(session != null) {
			return session;	
		} else {
			session = new PropertiesSession();
			session.init(FILE_NAME);
			return session;
		}
	}
	
	public static PropertiesSession getLoginsMap() throws FileNotFoundException, IOException {
		if(map != null) {
			return map;	
		} else {
			map = new PropertiesSession();
			map.init(LOGINS);
			return map;
		}
	}
	
	public static void reload() throws FileNotFoundException, IOException {
		session.init(FILE_NAME);
//		map.init(LOGINS);
	}
}
