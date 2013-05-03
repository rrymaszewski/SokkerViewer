package pl.pronux.sokker.handlers;

import java.util.Properties;

import pl.pronux.sokker.data.properties.SVProperties;
import pl.pronux.sokker.enums.OperatingSystem;
import pl.pronux.sokker.model.SokkerViewerSettings;

public class SettingsHandler {

	private static OperatingSystem operatingSystem = OperatingSystem.WINDOWS;

	static {
		String os = System.getProperty("os.name"); 
		if (os.toLowerCase().startsWith("linux")) { 
			operatingSystem = OperatingSystem.LINUX;
		} else if (os.toLowerCase().startsWith("mac")) { 
			operatingSystem = OperatingSystem.MACOSX;
		} else if (os.toLowerCase().startsWith("windows")) { 
			operatingSystem = OperatingSystem.WINDOWS;
		}
	}
	public static final boolean IS_WINDOWS = operatingSystem == OperatingSystem.WINDOWS;
	public static final boolean IS_LINUX = operatingSystem == OperatingSystem.LINUX;
	public static final boolean IS_MACOSX = operatingSystem == OperatingSystem.MACOSX;
	
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
