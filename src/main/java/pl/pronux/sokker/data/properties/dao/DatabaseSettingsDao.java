package pl.pronux.sokker.data.properties.dao;

import java.io.FileNotFoundException;
import java.io.IOException;

import pl.pronux.sokker.data.properties.PropertiesSession;
import pl.pronux.sokker.data.properties.dto.DatabaseSettingsDto;
import pl.pronux.sokker.model.DatabaseSettings;
import pl.pronux.sokker.utils.security.Crypto;

public class DatabaseSettingsDao {
	private PropertiesSession properties;

	public DatabaseSettingsDao(PropertiesSession properties) {
		this.properties = properties;
	}
	
	public DatabaseSettings getDatabaseSettings() {
		return new DatabaseSettingsDto(properties);
	}
	
	public void updateDatabaseSettings(DatabaseSettings databaseSettings) throws FileNotFoundException, IOException {
		properties.setProperty("db.server", databaseSettings.getServer()); //$NON-NLS-1$
		properties.setProperty("db.name", databaseSettings.getName()); //$NON-NLS-1$
		properties.setProperty("db.login", databaseSettings.getUsername()); //$NON-NLS-1$
		properties.setProperty("db.password", Crypto.encodeBase64(databaseSettings.getPassword().getBytes())); //$NON-NLS-1$
		properties.setProperty("db.type", databaseSettings.getType()); //$NON-NLS-1$

		properties.save();
	}
}
