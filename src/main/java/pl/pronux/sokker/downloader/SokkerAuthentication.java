package pl.pronux.sokker.downloader;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.pronux.sokker.exceptions.SVException;
import pl.pronux.sokker.utils.security.Base64Coder;

public class SokkerAuthentication {
	
	final private static int TIMEOUT_MS = 15000;

	private String errorno;

	private String message;

	private Proxy proxy;

	private String proxyAuth;

	private String sessionID;

	private String status;

	private String teamID;
	
	final public static String OK = "OK"; //$NON-NLS-1$
	final public static String FAILED = "FAILED"; //$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param login -
	 *            sokker login
	 * @param password -
	 *            sokker password
	 * @throws IOException 
	 * @throws SVException 
	 */
	public void login(String login, String password) throws SVException, IOException {
		login(login, password, null, 0, null, null);
	}

	public SokkerAuthentication() {
	}

	private String getContent(String urlString) throws IOException {
		// if (counter == 15) {
		// init(login, password);
		// counter = 0;
		// } else {
		// counter++;
		// }

		// String stringCache = "";
		StringBuffer buffer = new StringBuffer();
		URL url;
		HttpURLConnection connection = null;
		BufferedReader in = null;
		try {
			url = new URL(urlString);
			if (Proxy.NO_PROXY.equals(this.proxy)) {
				connection = (HttpURLConnection) url.openConnection();
			} else {
				connection = (HttpURLConnection) url.openConnection(this.proxy);
			}
			connection.setConnectTimeout(TIMEOUT_MS);
			connection.setReadTimeout(TIMEOUT_MS);
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.8) Gecko/20051224 Debian/1.5.dfsg-3 Firefox/1.5"); //$NON-NLS-1$ //$NON-NLS-2$
			connection.setRequestProperty("Accept", "text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5"); //$NON-NLS-1$ //$NON-NLS-2$
			// connection.setRequestProperty("Accept-Language", "en");
			// connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
			connection.setRequestProperty("Accept-Charset", "utf-8;q=0.7,*;q=0.7"); //$NON-NLS-1$ //$NON-NLS-2$
			connection.setRequestProperty("Keep-Alive", "300"); //$NON-NLS-1$ //$NON-NLS-2$
			// connection.setRequestProperty("Cookie", cookies);
			connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded"); //$NON-NLS-1$ //$NON-NLS-2$
			connection.setRequestProperty("Cookie", sessionID); //$NON-NLS-1$
			if (this.proxyAuth != null) {
				connection.setRequestProperty("Proxy-Authorization", "Basic " + this.proxyAuth); //$NON-NLS-1$ //$NON-NLS-2$
			}
			// for first request cookie doesn't exist
			// if (!cookies.equals("")) {
			// connection.setRequestProperty("Cookie", cookies);
			// } else {
			// cookies = getPHPSESSIONID(connection);
			// }

			// BufferedReader in = new BufferedReader(new
			// InputStreamReader(connection.getInputStream()));
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8")); //$NON-NLS-1$

			String line;
			while ((line = in.readLine()) != null) {
				// stringCache = stringCache + inputLine + "\n";
				buffer.append(line);
				buffer.append('\n');
			}
			// int c;
			// while ((c = in.read()) != -1) {
			// sb.append((char) c);
			// }

		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if(in != null) {
				in.close();
			}
		}

		return buffer.toString();
	}

	/**
	 * return message from sokker.org after action
	 * 
	 * @return logging action number<BR>
	 *         <ul>
	 *         <li>-4 - if message is null
	 *         <li>-1 - if message is not recognized
	 *         <li>0 - OK
	 *         <li>1 - Bad password
	 *         <li>3 - User has no team
	 *         <li>4 - User is banned
	 *         <li>5 - User is bankrupt
	 *         <li>6 - User's IP is on blacklist
	 *         </ul>
	 * 
	 */
	public String getErrorno() {
		return errorno;
	}

	/**
	 * this method return whole message received from server sokker.org
	 * 
	 * @return sokker.org server response String or null
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * return status of log-in.
	 * 
	 * @return status<BR>
	 *         <ul>
	 *         <li> FAILED - not logged
	 *         <li> OK - logged
	 *         </ul>
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * this method return teamID if everything were alright
	 * 
	 * @return logged teamID or null if there were problem with logging
	 */
	public String getTeamID() {
		return teamID;
	}

	protected String getXML(String urlString) throws IOException {
		return getXML(urlString, 5);
	}

	protected String getXML(String urlString, int tries) throws IOException {
		String response = ""; //$NON-NLS-1$
		while (tries > 0) {
			tries--;
			try {
				response = getContent(urlString);
			} catch (IOException ioex) {
				if (tries == 0) {
					throw ioex;
				}
				continue;
			}
			break;
		}
		return response;
	}

	public void login(String login, String password, String proxyHost, Integer proxyPort, String proxyUser, String proxyPass) throws SVException, IOException  {

		if ((proxyHost != null) && (proxyHost.length() > 0) && (proxyPort != null) && (proxyPort.intValue() > 0)) {
			SocketAddress address = new InetSocketAddress(proxyHost, proxyPort.intValue());
			this.proxy = new Proxy(Proxy.Type.HTTP, address);
		} else {
			this.proxy = Proxy.NO_PROXY;
		}

		if ((proxyUser != null) && (proxyUser.length() > 0) && (proxyPass != null) && (proxyPass.length() > 0)) {
			final String pw = proxyUser + ":" + proxyPass; //$NON-NLS-1$
			// this.proxyAuth = (new BASE64Encoder()).encode(pw.getBytes());
			this.proxyAuth = Base64Coder.encodeString(pw);
		}

		try {
			this.message = postDataToPage("http://217.17.40.90/start.php?session=xml", "ilogin=" + URLEncoder.encode(login, "UTF-8") + "&ipassword=" + URLEncoder.encode(password, "UTF-8"), "http://online.sokker.org/xmlinfo.php"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		} catch (UnsupportedEncodingException e) {
			this.status = SokkerAuthentication.FAILED; 
			this.errorno = Synchronizer.ERROR_MESSAGE_NULL;
			throw e;
		} catch (IOException e) {
			this.status = SokkerAuthentication.FAILED; 
			this.errorno = Synchronizer.ERROR_MESSAGE_NULL;
			throw e;
		} catch (SVException e) {
			this.status = SokkerAuthentication.FAILED; 
			this.errorno = Synchronizer.ERROR_MESSAGE_NULL;
			throw e;
		}

		if (this.message == null) {
			this.status = SokkerAuthentication.FAILED; 
			this.errorno = Synchronizer.ERROR_MESSAGE_NULL;
		} else {
			Pattern p1 = Pattern.compile("^OK teamID=[0-9]+\n$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE); //$NON-NLS-1$
			Matcher m1 = p1.matcher(message);
			Pattern p2 = Pattern.compile("FAILED errorno=[0-9]+\n$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE); //$NON-NLS-1$
			Matcher m2 = p2.matcher(message);

			if (m1.matches() || m2.matches()) {
				String[] msgTable = this.message.split(" "); //$NON-NLS-1$
				if (msgTable.length == 2) {
					this.status = msgTable[0];
					this.errorno = msgTable[1];
					if (status.equals(SokkerAuthentication.OK)) {
						this.teamID = msgTable[1].split("=")[1].replaceAll("[^0-9]", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					}
					if (status.equalsIgnoreCase(SokkerAuthentication.FAILED)) {
						this.errorno = msgTable[1].split("=")[1].replaceAll("[^0-9]", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					}

				} else {
					this.status = SokkerAuthentication.FAILED;
					this.errorno = Synchronizer.ERROR_RESPONSE_UNKNOWN; 
				}
			} else {
				this.status = SokkerAuthentication.FAILED;
				this.errorno = Synchronizer.ERROR_RESPONSE_UNKNOWN;
			}
		}
	}
	
	private String postDataToPage(String urlString, String parameters, String referer) throws IOException, SVException {
		String line;// , stringCache = "";
		StringBuffer buffer = new StringBuffer();
		URL url;
		DataOutputStream out = null;
		BufferedReader in = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(urlString);
			
			if (Proxy.NO_PROXY.equals(this.proxy)) {
				connection = (HttpURLConnection) url.openConnection();
			} else {
				connection = (HttpURLConnection) url.openConnection(this.proxy);
			}
			connection.setRequestMethod("POST"); //$NON-NLS-1$
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.8) Gecko/20051224 Debian/1.5.dfsg-3 Firefox/1.5"); //$NON-NLS-1$ //$NON-NLS-2$
			connection.setRequestProperty("Accept", "text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5"); //$NON-NLS-1$ //$NON-NLS-2$
			connection.setRequestProperty("Accept-Language", "pl"); //$NON-NLS-1$ //$NON-NLS-2$
			// connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
			connection.setRequestProperty("Accept-Charset", "UTF-8,*"); //$NON-NLS-1$ //$NON-NLS-2$
			connection.setRequestProperty("Keep-Alive", "300"); //$NON-NLS-1$ //$NON-NLS-2$
			connection.setRequestProperty("Referer", referer); //$NON-NLS-1$
			// connection.setRequestProperty("Cookie", cookies);
			connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded"); //$NON-NLS-1$ //$NON-NLS-2$
			if (this.proxyAuth != null) {
				connection.setRequestProperty("Proxy-Authorization", "Basic " + this.proxyAuth); //$NON-NLS-1$ //$NON-NLS-2$
			}
			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			// helping with loggin into the page
			// connection.setInstanceFollowRedirects(false);

			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(true);
			// connection.connect();

			out = new DataOutputStream(connection.getOutputStream());
			out.writeBytes(parameters);
			out.flush();

			//
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8")); //$NON-NLS-1$

			while ((line = in.readLine()) != null) {
				// stringCache += inputLine.replaceAll("&", "&amp;") + '\n';
				buffer.append(line.replaceAll("&", "&amp;")); //$NON-NLS-1$ //$NON-NLS-2$
				buffer.append('\n');
				// stringCache = stringCache.replaceAll("<", "&lt;");
				// stringCache = stringCache.replaceAll(">", "&gt;");
				// stringCache = stringCache.replaceAll("\"", "&quot;");
				// stringCache = stringCache.replaceAll("'", "&apos;");
			}

			this.sessionID = connection.getHeaderField("Set-Cookie"); //$NON-NLS-1$

		} catch (NullPointerException e) {
			throw new SVException("Error connecting"); //$NON-NLS-1$
		} finally {
			if(in != null) {
				in.close();
			}
			if(out != null) {
				out.close();
			}
			if(connection != null) {
				connection.disconnect();
			}
		}
		return buffer.toString();
	}
}