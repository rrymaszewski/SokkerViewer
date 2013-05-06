package pl.pronux.sokker.actions;

import java.io.IOException;

import pl.pronux.sokker.data.properties.PropertiesDatabase;
import pl.pronux.sokker.data.properties.dao.SokkerViewerSettingsDao;
import pl.pronux.sokker.exceptions.SVException;
import pl.pronux.sokker.model.SokkerViewerSettings;

public final class SettingsManager {

	private static SettingsManager instance = new SettingsManager();

	private SettingsManager() {
	}

	public static SettingsManager getInstance() {
		return instance;
	}

	public void updateSettings(SokkerViewerSettings settings) throws IOException, SVException {
		new SokkerViewerSettingsDao(PropertiesDatabase.getSession()).updateSokkerViewerSettings(settings);
	}
	
	public SokkerViewerSettings getSettings() throws IOException {
		return new SokkerViewerSettingsDao(PropertiesDatabase.getSession()).getSokkerViewerSettings();
	}

}
