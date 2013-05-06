package pl.pronux.sokker.downloader;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;

import pl.pronux.sokker.bean.Cookie;
import pl.pronux.sokker.interfaces.ProgressMonitor;
import pl.pronux.sokker.model.ProxySettings;
import pl.pronux.sokker.resources.Messages;

public class HTMLDownloader extends AbstractDownloader {

	private String cookies = ""; 

	public HTMLDownloader(ProxySettings proxySettings) {
		super.setProxy(proxySettings.getProxy());
		super.setProxyAuth(proxySettings.getProxyAuthentication());
	}

	public void downloadPackage(final String srcFile, String dstDirectory, String dstFile, ProgressMonitor monitor) throws IOException {
		int length;
		URL url = null;
		try {
			URLConnection con = getDefaultConnection(srcFile);
			length = con.getContentLength();
			url = con.getURL();
		} catch (MalformedURLException e1) {
			length = -1;
		} catch (IOException e) {
			length = -1;
		}

		if (url == null) {
			throw new IOException(Messages.getString("exception.url.null")); 
		}

		BufferedInputStream in = null;
		FileOutputStream out = null;
		try {
			File file = new File(dstDirectory);
			if (!file.exists()) {
				file.mkdirs();
			}
			byte[] buf = new byte[4096];

			in = new BufferedInputStream(url.openStream());
			out = new FileOutputStream(dstDirectory + File.separator + dstFile);
			int counter = 0;
			int len;
			while ((len = in.read(buf)) > 0) {
				counter = counter + (len);
				out.write(buf, 0, len);
				if (length != -1) {
					monitor.subTask(String.format("%s ( %dkb of %dkb )", srcFile, counter / 1000, length / 1000)); 
				} else {
					monitor.subTask(String.format("%s ( %dkb)", srcFile, counter / 1000)); 
				}
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

	public String getPageInBytes(String urlString) throws IOException {
		BufferedInputStream in = null;
		HttpURLConnection connection = null;
		StringBuilder buffer = new StringBuilder();
		try {
			connection = getDefaultConnection(urlString, GET);
			connection.setRequestProperty("Cookie", cookies); 
			// for first request cookie doesn't exist
			if (!cookies.isEmpty()) { 
				connection.setRequestProperty("Cookie", cookies); 
			} else {
				cookies = getPHPSESSIONID(connection);
			}

			int len;
			in = new BufferedInputStream(new URL(urlString).openStream());
			while ((len = in.read()) > 0) {
				buffer.append((char) len);
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (connection != null) {
				connection.disconnect();
			}
		}

		return buffer.toString();
	}

	public String getNormalPage(String urlString) throws IOException {
		StringBuilder content = new StringBuilder();
		BufferedReader in = null;
		HttpURLConnection connection = null;
		try {
			connection = getDefaultConnection(urlString, GET);
			connection.setRequestProperty("Cookie", cookies); 
			// for first request cookie doesn't exist
			if (!cookies.isEmpty()) { 
				connection.setRequestProperty("Cookie", cookies); 
			} else {
				cookies = getPHPSESSIONID(connection);
			}

			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String line;
			while ((line = in.readLine()) != null) {
				content.append(line).append("\n"); 
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (connection != null) {
				connection.disconnect();
			}
		}
		return content.toString();
	}

	public void getInternetFile(String urlString, String filename, String destinationDirectory) throws IOException {
		byte[] buf = new byte[4096];
		int len;
		BufferedInputStream in = null;
		FileOutputStream out = null;
		HttpURLConnection connection = null;
		try {
			connection = getDefaultConnection(urlString);
			in = new BufferedInputStream(connection.getInputStream());
			out = new FileOutputStream(destinationDirectory + File.separator + filename);
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public byte[] getInternetFile(String urlString) throws IOException {
		BufferedInputStream in = null;
		HttpURLConnection connection = null;
		try {

			byte[] buf = new byte[4096];
			int len;

			ByteBuffer byteBuffer = ByteBuffer.wrap(buf);

			connection = getDefaultConnection(urlString);

			in = new BufferedInputStream(connection.getInputStream());
			while ((len = in.read(buf)) > 0) {
				byteBuffer.put(buf, 0, len);
			}

			return byteBuffer.array();
		} finally {
			if (in != null) {
				in.close();
			}
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	private String getPHPSESSIONID(HttpURLConnection conn) {
		Cookie cookies = new Cookie();
		StringBuilder cookie = new StringBuilder();
		for (int i = 0;; i++) {
			String headerName = conn.getHeaderFieldKey(i);
			String headerValue = conn.getHeaderField(i);

			if (headerName == null && headerValue == null) {
				// No more headers
				break;
			}
			if ("Set-Cookie".equalsIgnoreCase(headerName)) { 
				// Parse cookie
				String[] fields = headerValue.split(";\\s*"); 

				String cookieValue = fields[0];
				String expires = null;
				String path = null;
				String domain = null;
				boolean secure = false;

				// Parse each field
				for (int j = 1; j < fields.length; j++) {
					if ("secure".equalsIgnoreCase(fields[j])) { 
						secure = true;
					} else if (fields[j].indexOf('=') > 0) {
						String[] f = fields[j].split("="); 
						if ("expires".equalsIgnoreCase(f[0])) { 
							expires = f[1];
						} else if ("domain".equalsIgnoreCase(f[0])) { 
							domain = f[1];
						} else if ("path".equalsIgnoreCase(f[0])) { 
							path = f[1];
						}
					}
				}

				// Save the cookie...

				cookie.append(cookieValue).append(";"); 
				cookies.setCookieValue(cookieValue);
				cookies.setDomain(domain);
				cookies.setExpires(expires);
				cookies.setPath(path);
				cookies.setSecure(secure);
			}
		}
		return cookie.toString();
	}

	public String postDataToPage(String urlString, String parameters, String referer) throws IOException {
		StringBuilder content = new StringBuilder();
		HttpURLConnection connection = null;
		BufferedReader in = null;
		DataOutputStream out = null;
		try {
			connection = getDefaultConnection(urlString, POST);
			connection.setRequestProperty("Referer", referer); 
			connection.setRequestProperty("Cookie", cookies); 
			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			// helping with loggin into the page
			// connection.setInstanceFollowRedirects(false);

			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(true);
			connection.connect();

			out = new DataOutputStream(connection.getOutputStream());
			out.writeBytes(parameters);
			out.flush();

			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8")); 

			String line;
			while ((line = in.readLine()) != null) {
				content.append(line.replaceAll("&", "&amp;")).append('\n');  
				// stringCache = stringCache.replaceAll("<", "&lt;");
				// stringCache = stringCache.replaceAll(">", "&gt;");
				// stringCache = stringCache.replaceAll("\"", "&quot;");
				// stringCache = stringCache.replaceAll("'", "&apos;");
			}

		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
			if (connection != null) {
				connection.disconnect();
			}
		}
		return content.toString();
	}
}