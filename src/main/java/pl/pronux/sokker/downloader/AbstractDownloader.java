package pl.pronux.sokker.downloader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

public class AbstractDownloader {

	public static final String POST = "POST";
	public static final String GET = "GET";

	private Proxy proxy = Proxy.NO_PROXY;

	private String proxyAuth;

	public Proxy getProxy() {
		return proxy;
	}

	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}

	public String getProxyAuth() {
		return proxyAuth;
	}

	public void setProxyAuth(String proxyAuth) {
		this.proxyAuth = proxyAuth;
	}

	protected HttpURLConnection getDefaultConnection(String urlString, String type) throws IOException {

		HttpURLConnection connection = getDefaultConnection(urlString);

		if (type != null) {
			connection.setRequestMethod(type); 	
		}
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.8) Gecko/20051224 Debian/1.5.dfsg-3 Firefox/1.5");  
		connection.setRequestProperty("Accept", "text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");  
		connection.setRequestProperty("Accept-Language", "pl");  
		connection.setRequestProperty("Accept-Charset", "UTF-8,*");  
		connection.setRequestProperty("Keep-Alive", "300");  
		connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");  
		return connection;
	}

	protected HttpURLConnection getDefaultConnection(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection connection;
		if (Proxy.NO_PROXY.equals(proxy)) {
			connection = (HttpURLConnection) url.openConnection();
		} else {
			connection = (HttpURLConnection) url.openConnection(proxy);
			if (proxyAuth != null) {
				connection.setRequestProperty("Proxy-Authorization", "Basic " + proxyAuth);  
			}
		}
		return connection;
	}
}
