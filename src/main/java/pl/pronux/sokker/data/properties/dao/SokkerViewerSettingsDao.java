package pl.pronux.sokker.data.properties.dao;

import java.io.FileNotFoundException;
import java.io.IOException;

import pl.pronux.sokker.data.properties.PropertiesDatabase;
import pl.pronux.sokker.data.properties.PropertiesSession;
import pl.pronux.sokker.data.properties.dto.SokkerViewerSettingsDto;
import pl.pronux.sokker.exceptions.SVException;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.utils.security.Crypto;

public class SokkerViewerSettingsDao {

	private PropertiesSession properties;

	public SokkerViewerSettingsDao(PropertiesSession properties) {
		this.properties = properties;
	}

	public SokkerViewerSettings getSokkerViewerSettings() {
		SokkerViewerSettings settings = new SokkerViewerSettingsDto(properties);
		settings.setProxySettings(new ProxyDao(properties).getProxySettings());
		return settings;
	}

	public void updateSokkerViewerSettings(SokkerViewerSettings sokkerViewerSettings) throws FileNotFoundException, IOException, SVException {
		properties.setProperty("sk.login", sokkerViewerSettings.getUsername()); //$NON-NLS-1$
		
		properties.setProperty("sk.dir", sokkerViewerSettings.getBaseDirectory()); //$NON-NLS-1$
		properties.setProperty("backup.dir", sokkerViewerSettings.getBackupDirectory()); //$NON-NLS-1$
		if(sokkerViewerSettings.isUpdate()) {
			properties.setProperty("conf.getXML", "1");	 //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			properties.setProperty("conf.getXML", "0"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if(sokkerViewerSettings.isSavePassword()) {
			properties.setProperty("sk.password", Crypto.encodeBase64(sokkerViewerSettings.getPassword().getBytes())); //$NON-NLS-1$
			properties.setProperty("conf.savepass", "1");	 //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			properties.setProperty("sk.password", Crypto.encodeBase64("".getBytes())); //$NON-NLS-1$ //$NON-NLS-2$
			properties.setProperty("conf.savepass", "0"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if(sokkerViewerSettings.isInfoUpdate()) {
			properties.setProperty("info.update", "1");	 //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			properties.setProperty("info.update", "0"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if(sokkerViewerSettings.isInfoClose()) {
			properties.setProperty("info.close", "1"); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			properties.setProperty("info.close", "0"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if(sokkerViewerSettings.isCheckProperties()) {
			properties.setProperty("check.properties", "1"); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			properties.setProperty("check.properties", "0"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		if(sokkerViewerSettings.isStartup()) {
			properties.setProperty("sv.startup", "1"); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			properties.setProperty("sv.startup", "0"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		properties.setProperty("lang.code", sokkerViewerSettings.getLangCode()); //$NON-NLS-1$

		PropertiesDatabase.getSession().save();
		
		new ProxyDao(PropertiesDatabase.getSession()).updateProxySettings(sokkerViewerSettings.getProxySettings());
	}
}
