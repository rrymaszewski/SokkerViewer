package pl.pronux.sokker.data.properties.dto;

import pl.pronux.sokker.data.properties.PropertiesSession;
import pl.pronux.sokker.exceptions.BadArgumentException;
import pl.pronux.sokker.model.ProxySettings;
import pl.pronux.sokker.utils.security.Crypto;

public class ProxyDto extends ProxySettings {
	public ProxyDto(PropertiesSession properties) {
		if(properties.getProperty("proxy.turn") != null && properties.getProperty("proxy.turn").equals("1")) {   
			this.setEnabled(true);	
		} else {
			this.setEnabled(false);
		}

		this.setHostname(properties.getProperty("proxy.host")); 
		this.setUsername(properties.getProperty("proxy.user")); 
		try {
			this.setPassword(new String(Crypto.decodeBase64(properties.getProperty("proxy.password")))); 
		} catch (BadArgumentException e1) {
			this.setPassword(""); 
		}
		
		try {
			this.setPort(Integer.valueOf(properties.getProperty("proxy.port"))); 
		} catch (Exception e) {
			this.setPort(8080);
		}
	}
}
