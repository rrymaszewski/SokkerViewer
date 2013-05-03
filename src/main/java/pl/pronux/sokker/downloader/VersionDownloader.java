package pl.pronux.sokker.downloader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.model.ProxySettings;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.updater.xml.UpdateXMLParser;

public class VersionDownloader {

	public static final String NO_UPDATES = "NO_UPDATES";

	private SokkerViewerSettings settings;

	public VersionDownloader(SokkerViewerSettings settings) {
		this.settings = settings;
	}

	public String getVersion() throws SAXException, IOException {
		String xml;
		String osType = "/windows";

		String version = NO_UPDATES;
		if (SettingsHandler.IS_WINDOWS) {
			osType = "/windows"; 
		} else if (SettingsHandler.IS_LINUX) {
			osType = "/linux"; 
		} else if (SettingsHandler.IS_MACOSX) {
			osType = "/mac"; 
		}

		String query = "http://www.sokkerviewer.net/sv/updates/stable" + osType + "/packages.xml";  

		ProxySettings proxySettings = settings.getProxySettings();
		xml = new HTMLDownloader(proxySettings).getNormalPage(query);

		UpdateXMLParser parser = new UpdateXMLParser();
		InputSource input = new InputSource(new StringReader(xml));
		parser.parseXmlSax(input, null);

		UpdateXMLParser oldParser = new UpdateXMLParser();
		InputSource inputOld = new InputSource(new FileReader(new File(settings.getBaseDirectory() + File.separator + "packages.xml"))); 
		oldParser.parseXmlSax(inputOld, null);

		if (parser.getRevision() > oldParser.getRevision()) {
			version = parser.getVersion();
		}
		return version;
	}
}
