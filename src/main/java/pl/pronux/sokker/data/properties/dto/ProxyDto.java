package pl.pronux.sokker.data.properties.dto;

import pl.pronux.sokker.data.properties.PropertiesSession;
import pl.pronux.sokker.exceptions.BadArgumentException;
import pl.pronux.sokker.model.ProxySettings;
import pl.pronux.sokker.utils.security.Crypto;

public class ProxyDto extends ProxySettings {
	public ProxyDto(PropertiesSession properties) {
		if(properties.getProperty("proxy.turn") != null && properties.getProperty("proxy.turn").equals("1")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			this.setEnabled(true);	
		} else {
			this.setEnabled(false);
		}

		this.setHostname(properties.getProperty("proxy.host")); //$NON-NLS-1$
		this.setUsername(properties.getProperty("proxy.user")); //$NON-NLS-1$
		try {
			this.setPassword(new String(Crypto.decodeBase64(properties.getProperty("proxy.password")))); //$NON-NLS-1$
		} catch (BadArgumentException e1) {
			this.setPassword(""); //$NON-NLS-1$
		}
		
		try {
			this.setPort(Integer.valueOf(properties.getProperty("proxy.port"))); //$NON-NLS-1$
		} catch (Exception e) {
			this.setPort(8080);
		}
	}
}
