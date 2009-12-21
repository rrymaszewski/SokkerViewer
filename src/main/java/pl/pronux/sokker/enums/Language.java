package pl.pronux.sokker.enums;

import java.util.HashMap;
import java.util.Map;

public enum Language {

	bg_BG("\u0411\u044A\u043B\u0433\u0430\u0440\u0441\u043A\u0438"),
	cs_CZ("\u010Ce\u0161tina"),
	de_DE("Deutsch"),
	dk_DK("Dansk"),
	ee_EE("Eesti"),
	en_EN("English"),
	es_ES("Espa\u00F1ol"),
	fr_FR("Fran\u00E7ais"),
	it_IT("Italiano"),
	pl_PL("Polski"),
	pt_PT("Portugu\u00EAs"),
	ro_RO("Rom\u00E2n\u00E3"),
	tr_TR("T\u00FCrk\u00E7e");

	private static String[] languageCodes;
	private static String[] languageNames;
	private static Map<String, String> langCodes = new HashMap<String, String>();
	static {
		languageCodes = new String[values().length];
		languageNames = new String[values().length];

		for (int i = 0; i < values().length; i++) {
			languageCodes[i] = values()[i].name();
			languageNames[i] = values()[i].languageName;
			langCodes.put(languageNames[i], languageCodes[i]);
		}
	}
	
	private String languageName;

	private Language(String languageName) {
		this.languageName = languageName;
	}

	public String getLanguageName() {
		return this.languageName;
	}

	public static String[] languageCodes() {
		return languageCodes;
	}

	public static String[] languageNames() {
		return languageNames;
	}

	public static String getLanguageCode(String languageName) {
		return langCodes.get(languageName);
	}
}