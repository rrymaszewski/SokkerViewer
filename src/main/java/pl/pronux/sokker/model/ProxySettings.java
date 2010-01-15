package pl.pronux.sokker.model;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

import pl.pronux.sokker.utils.security.Base64Coder;

public class ProxySettings {

	private int port;
	private String hostname;
	private String username;
	private String password;
	private boolean enabled;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isProxyWithAuthentication() {
		return username != null && !username.isEmpty() && password != null && !password.isEmpty();
	}

	public String getProxyAuthentication() {
		String proxyAuth = null;
		if (isProxyEnabled() && isProxyWithAuthentication()) {
			proxyAuth = Base64Coder.encodeString(this.username + ":" + this.password);
		}
		return proxyAuth;
	}

	public boolean isProxyEnabled() {
		return isEnabled() && hostname != null && !hostname.isEmpty() && port > 0;
	}

	public Proxy getProxy() {
		Proxy proxy = null;
		if (isProxyEnabled()) {
			SocketAddress address = new InetSocketAddress(hostname, port);
			proxy = new Proxy(Proxy.Type.HTTP, address);
		} else {
			proxy = Proxy.NO_PROXY;
		}
		return proxy;
	}
}
