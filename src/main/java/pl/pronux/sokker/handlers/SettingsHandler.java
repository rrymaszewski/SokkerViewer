package pl.pronux.sokker.handlers;

import java.util.Properties;

import pl.pronux.sokker.data.properties.SVProperties;
import pl.pronux.sokker.interfaces.SV;
import pl.pronux.sokker.model.SokkerViewerSettings;

public class SettingsHandler {
	public static int OS_TYPE = SV.WINDOWS;
	
	static {
		String os = System.getProperty("os.name"); //$NON-NLS-1$
		if(os.toLowerCase().startsWith("linux")) { //$NON-NLS-1$
			OS_TYPE = SV.LINUX;
		} else if(os.toLowerCase().startsWith("mac")) { //$NON-NLS-1$
			OS_TYPE = SV.MAC;
		} else if(os.toLowerCase().startsWith("windows")) { //$NON-NLS-1$
			OS_TYPE = SV.WINDOWS;
		}
	}
	private static boolean logged;
	private static SokkerViewerSettings sokkerViewerSettings;
	private static Properties defaultProperties;
	private static SVProperties userProperties;

	public static boolean isLogged() {
		return SettingsHandler.logged;
	}

	public static void setLogged(boolean logged) {
		SettingsHandler.logged = logged;
	}

	public static SokkerViewerSettings getSokkerViewerSettings() {
		return SettingsHandler.sokkerViewerSettings;
	}

	public static void setSokkerViewerSettings(SokkerViewerSettings sokkerViewerSettings) {
		SettingsHandler.sokkerViewerSettings = sokkerViewerSettings;
	}

	public static Properties getDefaultProperties() {
		return SettingsHandler.defaultProperties;
	}

	public static SVProperties getUserProperties() {
		return SettingsHandler.userProperties;
	}

	public static void setDefaultProperties(Properties defaultProperties) {
		SettingsHandler.defaultProperties = defaultProperties;
	}

	public static void setUserProperties(SVProperties userProperties) {
		SettingsHandler.userProperties = userProperties;
	}

}
