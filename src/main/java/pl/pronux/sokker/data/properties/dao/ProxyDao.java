package pl.pronux.sokker.data.properties.dao;

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
	
	public void updateProxySettings(ProxySettings proxySettings) throws IOException {
		if (proxySettings.isEnabled()) {
			properties.setProperty("proxy.turn", "1");  
		} else {
			properties.setProperty("proxy.turn", "0");  
		}

		properties.setProperty("proxy.host", proxySettings.getHostname()); 
		properties.setProperty("proxy.port", String.valueOf(proxySettings.getPort())); 
		properties.setProperty("proxy.user", proxySettings.getUsername()); 
		properties.setProperty("proxy.password", Crypto.encodeBase64(proxySettings.getPassword().getBytes())); 
		
		PropertiesDatabase.getSession().save();
	}
}
