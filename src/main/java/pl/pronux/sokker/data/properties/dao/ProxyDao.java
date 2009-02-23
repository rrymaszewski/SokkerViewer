package pl.pronux.sokker.data.properties.dao;

import java.io.FileNotFoundException;
import java.io.IOException;

import pl.pronux.sokker.data.properties.PropertiesDatabase;
import pl.pronux.sokker.data.properties.PropertiesSession;
import pl.pronux.sokker.data.properties.dto.ProxyDto;
import pl.pronux.sokker.model.ProxySettings;
import pl.pronux.sokker.utils.security.Crypto;

public class ProxyDao {
	private PropertiesSession properties;

	public ProxyDao(PropertiesSession properties) {
		this.properties = properties;
	}
	
	public ProxySettings getProxySettings() {
		return new ProxyDto(properties);
	}
	
	public void updateProxySettings(ProxySettings proxySettings) throws FileNotFoundException, IOException {
		if (proxySettings.isEnabled()) {
			properties.setProperty("proxy.turn", "1"); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			properties.setProperty("proxy.turn", "0"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		properties.setProperty("proxy.host", proxySettings.getHostname()); //$NON-NLS-1$
		properties.setProperty("proxy.port", String.valueOf(proxySettings.getPort())); //$NON-NLS-1$
		properties.setProperty("proxy.user", proxySettings.getUsername()); //$NON-NLS-1$
		properties.setProperty("proxy.password", Crypto.encodeBase64(proxySettings.getPassword().getBytes())); //$NON-NLS-1$
		
		PropertiesDatabase.getSession().save();
	}
}
