package pl.pronux.sokker.data.properties.dto;

import java.util.ArrayList;
import java.util.Arrays;

import pl.pronux.sokker.data.properties.PropertiesSession;
import pl.pronux.sokker.exceptions.BadArgumentException;
import pl.pronux.sokker.model.DatabaseSettings;
import pl.pronux.sokker.utils.security.Crypto;

public class DatabaseSettingsDto extends DatabaseSettings {
	
	public DatabaseSettingsDto(PropertiesSession properties) {
		this.setUsername(properties.getProperty("db.login")); //$NON-NLS-1$
		try {
			this.setPassword(new String(Crypto.decodeBase64(properties.getProperty("db.password")))); //$NON-NLS-1$
		} catch (BadArgumentException e) {
			this.setPassword(""); //$NON-NLS-1$
		}
		this.setName(properties.getProperty("db.name")); //$NON-NLS-1$
		this.setServer(properties.getProperty("db.server")); //$NON-NLS-1$
		this.setType(properties.getProperty("db.type")); //$NON-NLS-1$
		
		String databases = properties.getProperty("db.list"); //$NON-NLS-1$
		if(databases != null) {
			String[] databasesTable = databases.split(";"); //$NON-NLS-1$
			this.setTypes(Arrays.asList(databasesTable));
		} else {
			this.setTypes(new ArrayList<String>());
		}
		
	}

}
