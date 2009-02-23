package pl.pronux.sokker.updater.actions;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pl.pronux.sokker.downloader.HTMLDownloader;
import pl.pronux.sokker.exceptions.BadArgumentException;
import pl.pronux.sokker.exceptions.SVException;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.interfaces.IProgressMonitor;
import pl.pronux.sokker.interfaces.IRunnableWithProgress;
import pl.pronux.sokker.interfaces.ISecurity;
import pl.pronux.sokker.model.ProxySettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.updater.model.PackagesCollection;
import pl.pronux.sokker.updater.xml.UpdateXMLParser;
import pl.pronux.sokker.utils.file.OperationOnFile;
import pl.pronux.sokker.utils.security.Crypto;

public class PackagesXMLDownloader implements IRunnableWithProgress {
	private String version;
	private String os;
	private String mirror;
	private PackagesCollection packages;
	private String info = ""; //$NON-NLS-1$

	public PackagesXMLDownloader(String mirror, String version, String os, PackagesCollection packages) {
		this.version = version;
		this.os = os;
		this.mirror = mirror;
		this.packages = packages;
	}

	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		String signature;
		HTMLDownloader htmlDownloader;

		monitor.beginTask(String.format("%s (1/4)", Messages.getString("updater.label.xml.download")), 4); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			PublicKey pubk = Crypto.convertByteArrayToPublicKey(Crypto.decodeBase64(ISecurity.SERVER_PUBLIC_KEY), "RSA"); //$NON-NLS-1$

			ProxySettings proxySettings = SettingsHandler.getSokkerViewerSettings().getProxySettings();

			htmlDownloader = new HTMLDownloader(proxySettings);
			htmlDownloader.getInternetFile(mirror + version + os + "/packages.xml", "packages.xml", System.getProperty("user.dir") + File.separator + "tmp" + File.separator); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			signature = htmlDownloader.getPageInBytes(mirror + version + os + "/packages.xml.md5"); //$NON-NLS-1$

			monitor.worked(1);

			monitor.setTaskName(String.format("%s (2/4)", Messages.getString("updater.label.xml.verify"))); //$NON-NLS-1$ //$NON-NLS-2$

			if (Crypto.verifySignature(pubk, Crypto.decodeBase64(signature), new File(System.getProperty("user.dir") + File.separator + "tmp" + File.separator + "packages.xml"))) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				monitor.worked(1);
				// PARSE XML
				monitor.setTaskName(String.format("%s (3/4)", Messages.getString("updater.label.xml.parse"))); //$NON-NLS-1$ //$NON-NLS-2$

				String xml = OperationOnFile.readFromFile(System.getProperty("user.dir") + File.separator + "tmp" + File.separator + "packages.xml"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				final UpdateXMLParser parser = new UpdateXMLParser();
				InputSource input = new InputSource(new StringReader(xml));
				parser.parseXmlSax(input, null);

				UpdateXMLParser oldParser = new UpdateXMLParser();
				InputSource inputOld = new InputSource(new FileReader(new File(System.getProperty("user.dir") + File.separator + "packages.xml"))); //$NON-NLS-1$ //$NON-NLS-2$
				oldParser.parseXmlSax(inputOld, null);

				monitor.worked(1);
				monitor.setTaskName(String.format("%s (4/4)", Messages.getString("updater.label.xml.compare"))); //$NON-NLS-1$ //$NON-NLS-2$

				packages.setPackages(parser.compareTo(oldParser));
				
				if (packages.getPackages().size() > 0) {
					int counter = oldParser.revision + 1;
					while (counter <= parser.revision) {
						info += "revision = " + counter + "\r\n"; //$NON-NLS-1$ //$NON-NLS-2$
						String desc = htmlDownloader.getNormalPage(mirror + version + "/packages/description/revision." + counter); //$NON-NLS-1$
						String[] lines = desc.split("\n"); //$NON-NLS-1$
						for (int i = 0; i < lines.length; i++) {
							info += lines[i] + "\r\n"; //$NON-NLS-1$
						}
						info += "\r\n\r\n"; //$NON-NLS-1$
						counter++;
					}
					packages.setInfo(info);
				} else {
					monitor.interrupt();
//					throw new SVException(Messages.getString("updater.label.info.empty")); //$NON-NLS-1$
				}
				monitor.worked(1);
			} else {
				throw new SVException(Messages.getString("updater.label.info.verify.xml")); //$NON-NLS-1$
			}
			monitor.setTaskName(""); //$NON-NLS-1$
			monitor.done();
		} catch (SVException sve) {
			monitor.interrupt();
			throw new InvocationTargetException(sve, sve.getMessage());
		} catch (IOException e) {
			throw new InvocationTargetException(e, Messages.getString("error.internet.connection") + " " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (SAXException e) {
			throw new InvocationTargetException(e, Messages.getString("error.xml.parse")); //$NON-NLS-1$
		} catch (NoSuchAlgorithmException e) {
			throw new InvocationTargetException(e, Messages.getString("error.security.crypto")); //$NON-NLS-1$
		} catch (InvalidKeySpecException e) {
			throw new InvocationTargetException(e, Messages.getString("error.security.crypto")); //$NON-NLS-1$
		} catch (BadArgumentException e) {
			throw new InvocationTargetException(e, Messages.getString("error.security.crypto")); //$NON-NLS-1$
		}
	}
}
