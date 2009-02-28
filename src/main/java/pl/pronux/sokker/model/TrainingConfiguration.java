package pl.pronux.sokker.model;

import java.util.HashMap;
import java.util.Map;

public class TrainingConfiguration {

	public TrainingConfiguration() {
		Integer[][] table = new Integer[4][4];
		for(int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				table[i][j] = 0;
			}
		}
		
		settings.put(Training.TYPE_DEFENDING, table);
		settings.put(Training.TYPE_KEEPER, table);
		settings.put(Training.TYPE_PACE, table);
		settings.put(Training.TYPE_PASSING, table);
		settings.put(Training.TYPE_PLAYMAKING, table);
		settings.put(Training.TYPE_STRIKER, table);
		settings.put(Training.TYPE_TECHNIQUE, table);
		
		generalSettings.put(OFFICIAL, "100");
		generalSettings.put(ENABLED, "FALSE");
		generalSettings.put(FRIENDLY, "80");
		generalSettings.put(OFFICIAL_FRIENDLY, "120");
		generalSettings.put(OFFICIAL_CUP, "115");
		generalSettings.put(NATIONAL, "20");
	}

	public static final String OFFICIAL_CUP = "TrainingsConfigurationOfficialCup";
	public static final String FRIENDLY = "TrainingsConfigurationFriendly";
	public static final String OFFICIAL_FRIENDLY = "TrainingsConfigurationOfficialFriendly";
	public static final String OFFICIAL = "TrainingsConfigurationOfficial";
	public static final String NATIONAL = "TrainingsConfigurationNational";
	public static final String ENABLED = "TrainingsConfigurationEnabled";

	private Map<String, String> generalSettings = new HashMap<String, String>();
	private Map<Integer, Integer[][]> settings = new HashMap<Integer, Integer[][]>();

	public Map<String, String> getGeneralSettings() {
		return generalSettings;
	}

	public void setGeneralSettings(Map<String, String> generalSettings) {
		this.generalSettings = generalSettings;
	}

	public Map<Integer, Integer[][]> getSettings() {
		return settings;
	}

	public void setSettings(Map<Integer, Integer[][]> settings) {
		this.settings = settings;
	}
	
	public boolean getEnabled() {
		return Boolean.valueOf(generalSettings.get(ENABLED));
	}
}
