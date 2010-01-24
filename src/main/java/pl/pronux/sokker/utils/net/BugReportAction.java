package pl.pronux.sokker.utils.net;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

import pl.pronux.sokker.downloader.HTMLDownloader;
import pl.pronux.sokker.exceptions.BadArgumentException;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.interfaces.SV;
import pl.pronux.sokker.model.ProxySettings;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.utils.security.Crypto;

public class BugReportAction {

	private final String SECRET_KEY_128 = "vsXFsdVfeGFTyMdpOVhY4A=="; //$NON-NLS-1$

	private final static String SYMMETRIC_KEY_TYPE = "Rijndael"; //$NON-NLS-1$

	private final static String SYMMETRIC_KEY_SEQUENCE = "Rijndael/CBC/NoPadding"; //$NON-NLS-1$

	private final static String NT_DATABASE_ADDRESS = "http://www.sokkerviewer.net/bugs/"; //$NON-NLS-1$

	private final static String NT_DATABASE_ADDRESS_ADD = NT_DATABASE_ADDRESS + "sv_addbug.php"; //$NON-NLS-1$

	public String sendBug(String bug, SokkerViewerSettings settings) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
		IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, ShortBufferException, IOException, BadArgumentException {
		String parameters = ""; //$NON-NLS-1$
		SecretKeySpec skey = Crypto.convertByteArrayToSymmetricKey(Crypto.decodeBase64(SECRET_KEY_128), SYMMETRIC_KEY_TYPE);
		parameters = Crypto
			.encodeBase64(Crypto
				.encryptSymmetric(
								  (SettingsHandler.getSokkerViewerSettings().getUsername() + ";;" + bug + ";;" + getEnvironment()).getBytes("UTF-8"), skey, SYMMETRIC_KEY_SEQUENCE)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		parameters = URLEncoder.encode(parameters, "UTF-8"); //$NON-NLS-1$

		String params = "parser=" + parameters; //$NON-NLS-1$
		// params = params.replaceAll("\\+", "%2B");

		String url = NT_DATABASE_ADDRESS_ADD;
		String referer = url;

		ProxySettings proxySettings = settings.getProxySettings();
		HTMLDownloader htmlDownloader;
		htmlDownloader = new HTMLDownloader(proxySettings);
		String value = htmlDownloader.postDataToPage(url, params, referer).replaceAll("[^0-9]", ""); //$NON-NLS-1$ //$NON-NLS-2$
		return value;
	}

	private String getEnvironment() {
		return System.getProperty("os.name") + "#" + System.getProperty("os.arch") + "#" + System.getProperty("java.version") + "#" + SV.SK_VERSION; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}
}
