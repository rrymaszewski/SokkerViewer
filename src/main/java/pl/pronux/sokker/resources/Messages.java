package pl.pronux.sokker.resources;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import pl.pronux.sokker.utils.Log;

public class Messages {

	private static Map<Locale, LangResources> cache = new HashMap<Locale, LangResources>();

	private static String bundleName = "lang.sokker"; 
	
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName);

//	private Messages() {
//	}

	public static String getBaseName() {
		return bundleName;
	}

	public static void setDefault(Locale newLocale) {
		resourceBundle = ResourceBundle.getBundle(bundleName, newLocale);
	}

	public static String getString(String key) {
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			Log.warning("Missing translation key: " + key);
			return '!' + key + '!';
		}
	}

//	public static Messages getInstance() {
//		return new Messages();
//	}

	public static LangResources getDefaultLangResources() {
		return getLangResources(resourceBundle.getLocale());
	}
	
	public static LangResources getLangResources(Locale locale) {
		if (cache.get(locale) != null) {
			return cache.get(locale);
		} else {
			LangResources langResources = new LangResources(locale);
			cache.put(locale, langResources);
			return langResources;
		}
	}

}
