package pl.pronux.sokker.data.properties.dao;

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

	public void updateSokkerViewerSettings(SokkerViewerSettings sokkerViewerSettings) throws IOException, SVException {
		properties.setProperty("sk.login", sokkerViewerSettings.getUsername()); 
		
		properties.setProperty("sk.dir", sokkerViewerSettings.getBaseDirectory()); 
		properties.setProperty("backup.dir", sokkerViewerSettings.getBackupDirectory()); 
		if(sokkerViewerSettings.isUpdate()) {
			properties.setProperty("conf.getXML", "1");	  
		} else {
			properties.setProperty("conf.getXML", "0");  
		}
		if(sokkerViewerSettings.isSavePassword()) {
			properties.setProperty("sk.password", Crypto.encodeBase64(sokkerViewerSettings.getPassword().getBytes())); 
			properties.setProperty("conf.savepass", "1");	  
		} else {
			properties.setProperty("sk.password", Crypto.encodeBase64("".getBytes()));  
			properties.setProperty("conf.savepass", "0");  
		}
		if(sokkerViewerSettings.isInfoUpdate()) {
			properties.setProperty("info.update", "1");	  
		} else {
			properties.setProperty("info.update", "0");  
		}
		if(sokkerViewerSettings.isInfoClose()) {
			properties.setProperty("info.close", "1");  
		} else {
			properties.setProperty("info.close", "0");  
		}
		if(sokkerViewerSettings.isCheckProperties()) {
			properties.setProperty("check.properties", "1");  
		} else {
			properties.setProperty("check.properties", "0");  
		}
		
		if(sokkerViewerSettings.isStartup()) {
			properties.setProperty("sv.startup", "1");  
		} else {
			properties.setProperty("sv.startup", "0");  
		}
		
		properties.setProperty("lang.code", sokkerViewerSettings.getLangCode()); 

		PropertiesDatabase.getSession().save();
		
		new ProxyDao(PropertiesDatabase.getSession()).updateProxySettings(sokkerViewerSettings.getProxySettings());
	}
}
