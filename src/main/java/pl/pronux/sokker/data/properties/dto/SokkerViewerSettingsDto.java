package pl.pronux.sokker.data.properties.dto;

import pl.pronux.sokker.data.properties.PropertiesSession;
import pl.pronux.sokker.exceptions.BadArgumentException;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.utils.security.Crypto;

public class SokkerViewerSettingsDto extends SokkerViewerSettings {

	public SokkerViewerSettingsDto(PropertiesSession properties) {
		this.setBackupDirectory(properties.getProperty("backup.dir")); 
		this.setLangCode(properties.getProperty("lang.code")); 
		this.setUsername(properties.getProperty("sk.login")); 
		
		this.setBaseDirectory(properties.getProperty("sk.dir")); 
		
		try {
			this.setPassword(new String(Crypto.decodeBase64(properties
					.getProperty("sk.password")))); 
		} catch (BadArgumentException e1) {
			this.setPassword(""); 
		}
		
		try {
			if(properties.getProperty("check.properties").equals("1")) {  
				this.setCheckProperties(true);
			} else {
				this.setCheckProperties(false);
			}
		} catch (Exception e) {
			this.setCheckProperties(true);
		}
		
		try {
			if(properties.getProperty("info.close").equals("1")) {  
				this.setInfoClose(true);
			} else {
				this.setInfoClose(false);
			}
		} catch (Exception e) {
			this.setInfoClose(false);
		} 
		
		try {
			if(properties.getProperty("info.update").equals("1")) {  
				this.setInfoUpdate(true);
			} else { 
				this.setInfoUpdate(false);
			}
		} catch (Exception e) {
			this.setInfoUpdate(false);
		}
		
		try {
			if(properties.getProperty("conf.savepass").equals("1")) {  
				this.setSavePassword(true);
			} else {
				this.setSavePassword(false);
			}
		} catch (Exception e) {
			this.setSavePassword(false);
		} 
		
		try {
			if(properties.getProperty("sv.startup").equals("1")) {  
				this.setStartup(true);
			} else {
				this.setStartup(false);				
			}
		} catch (Exception e) {
			
		} 
		
		try {
			if(properties.getProperty("conf.getXML").equals("1")) {  
				this.setUpdate(true);
			} else {
				this.setUpdate(false);
			}
		} catch (Exception e) {
			this.setUpdate(false);
		} 
	}

}
