package pl.pronux.sokker.downloader;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.pronux.sokker.exceptions.SVException;
import pl.pronux.sokker.model.ProxySettings;

public class SokkerAuthentication extends AbstractDownloader {

	public static final String OK = "OK"; //$NON-NLS-1$
	public static final String FAILED = "FAILED"; //$NON-NLS-1$
	
	private static final int TIMEOUT_MS = 15000;

	private String errorno;

	private String message;

	private String sessionID;

	private String status;

	private String teamID;

	/**
	 * Constructor
	 * 
	 * @param login
	 *        - sokker login
	 * @param password
	 *        - sokker password
	 * @throws IOException
	 * @throws SVException
	 */
	public void login(String login, String password) throws SVException, IOException {
		login(login, password, null);
	}

	public SokkerAuthentication() {
	}

	private String getContent(String urlString) throws IOException {
		StringBuilder buffer = new StringBuilder();
		HttpURLConnection connection = null;
		BufferedReader in = null;
		try {
			connection = getDefaultConnection(urlString, GET);
			connection.setConnectTimeout(TIMEOUT_MS);
			connection.setReadTimeout(TIMEOUT_MS);
			connection.setRequestProperty("Cookie", sessionID); //$NON-NLS-1$
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8")); //$NON-NLS-1$

			String line;
			while ((line = in.readLine()) != null) {
				buffer.append(line);
				buffer.append('\n');
			}
		} finally {
			if (connection != null) connection.disconnect();
			if (in != null) in.close();
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
	 *         <li>FAILED - not logged
	 *         <li>OK - logged
	 *         </ul>
v	 */
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
		while (tries > 0 && response.isEmpty()) {
			tries--;
			try {
				response = getContent(urlString);
			} catch (IOException ioex) {
				if (tries == 0) {
					throw ioex;
				}
			}
		}
		return response;
	}

	public void login(String login, String password, ProxySettings proxySettings) throws SVException, IOException {
		if (proxySettings != null) {
			super.setProxy(proxySettings.getProxy());
			super.setProxyAuth(proxySettings.getProxyAuthentication());
		}

		try {
			this.message = postDataToPage(
										  "http://217.17.40.90/start.php?session=xml", "ilogin=" + URLEncoder.encode(login, "UTF-8") + "&ipassword=" + URLEncoder.encode(password, "UTF-8"), "http://online.sokker.org/xmlinfo.php"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
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
		StringBuilder buffer = new StringBuilder();
		DataOutputStream out = null;
		BufferedReader in = null;
		HttpURLConnection connection = null;
		try {
			connection = getDefaultConnection(urlString, POST);

			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			// helping with loggin into the page
			// connection.setInstanceFollowRedirects(false);

			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			connection.setRequestProperty("Referer", referer); //$NON-NLS-1$
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(true);

			out = new DataOutputStream(connection.getOutputStream());
			out.writeBytes(parameters);
			out.flush();

			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8")); //$NON-NLS-1$

			String line;
			while ((line = in.readLine()) != null) {
				buffer.append(line.replaceAll("&", "&amp;")); //$NON-NLS-1$ //$NON-NLS-2$
				buffer.append('\n');
			}

			this.sessionID = connection.getHeaderField("Set-Cookie"); //$NON-NLS-1$

		} catch (NullPointerException e) {
			throw new SVException("Error connecting"); //$NON-NLS-1$
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
		return buffer.toString();
	}
}