package pl.pronux.sokker.actions;

import java.io.FileNotFoundException;
import java.io.IOException;

import pl.pronux.sokker.data.properties.PropertiesDatabase;
import pl.pronux.sokker.data.properties.dao.SokkerViewerSettingsDao;
import pl.pronux.sokker.exceptions.SVException;
import pl.pronux.sokker.model.SokkerViewerSettings;

public class SettingsManager {

	private final static SettingsManager _instance = new SettingsManager();

	private SettingsManager() {
	}

	public static SettingsManager instance() {
		return _instance;
	}

	public void updateSettings(SokkerViewerSettings settings) throws FileNotFoundException, IOException, SVException {
		new SokkerViewerSettingsDao(PropertiesDatabase.getSession()).updateSokkerViewerSettings(settings);
	}
	
	public SokkerViewerSettings getSettings() throws FileNotFoundException, IOException {
		return new SokkerViewerSettingsDao(PropertiesDatabase.getSession()).getSokkerViewerSettings();
	}

}
