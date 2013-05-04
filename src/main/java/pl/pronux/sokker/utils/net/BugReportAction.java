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

	private static final String SECRET_KEY_128 = "vsXFsdVfeGFTyMdpOVhY4A=="; 

	private static final String SYMMETRIC_KEY_TYPE = "Rijndael"; 

	private static final String SYMMETRIC_KEY_SEQUENCE = "Rijndael/CBC/NoPadding"; 

	private static final String NT_DATABASE_ADDRESS = "http://www.sokkerviewer.net/bugs/"; 

	private static final String NT_DATABASE_ADDRESS_ADD = NT_DATABASE_ADDRESS + "sv_addbug.php"; 

	public String sendBug(String bug, SokkerViewerSettings settings) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
		IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, ShortBufferException, IOException, BadArgumentException {
		String parameters = ""; 
		SecretKeySpec skey = Crypto.convertByteArrayToSymmetricKey(Crypto.decodeBase64(SECRET_KEY_128), SYMMETRIC_KEY_TYPE);
		parameters = Crypto
			.encodeBase64(Crypto
				.encryptSymmetric(
								  (SettingsHandler.getSokkerViewerSettings().getUsername() + ";;" + bug + ";;" + getEnvironment()).getBytes("UTF-8"), skey, SYMMETRIC_KEY_SEQUENCE));   
		parameters = URLEncoder.encode(parameters, "UTF-8"); 

		String params = "parser=" + parameters; 
		// params = params.replaceAll("\\+", "%2B");

		String url = NT_DATABASE_ADDRESS_ADD;
		String referer = url;

		ProxySettings proxySettings = settings.getProxySettings();
		HTMLDownloader htmlDownloader = new HTMLDownloader(proxySettings);
		return htmlDownloader.postDataToPage(url, params, referer).replaceAll("[^0-9]", "");  
	}

	private String getEnvironment() {
		return System.getProperty("os.name") + "#" + System.getProperty("os.arch") + "#" + System.getProperty("java.version") + "#" + SV.SK_VERSION;     
	}
}
