package pl.pronux.sokker.resources;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.jfree.util.Log;

public class LangResources {

	private ResourceBundle resourceBundle;

	public LangResources(Locale locale) {
		this.resourceBundle = ResourceBundle.getBundle(Messages.getBaseName(), locale);
	}

	public String getString(String key) {
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			Log.warn("Missing translation key: " + key);
			return '!' + key + '!';
		}
	}
}