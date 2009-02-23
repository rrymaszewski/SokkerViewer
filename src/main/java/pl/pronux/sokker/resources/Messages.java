package pl.pronux.sokker.resources;

import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static String bundleName = "lang.sokker"; //$NON-NLS-1$

	private static final HashMap<Locale, LangResources> cache = new HashMap<Locale, LangResources>();

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
