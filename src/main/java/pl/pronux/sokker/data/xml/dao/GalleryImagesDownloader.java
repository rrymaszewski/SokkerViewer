package pl.pronux.sokker.data.xml.dao;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pl.pronux.sokker.data.xml.dto.GalleryXMLParser;
import pl.pronux.sokker.downloader.HTMLDownloader;
import pl.pronux.sokker.exceptions.BadArgumentException;
import pl.pronux.sokker.exceptions.SVException;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.interfaces.IProgressMonitor;
import pl.pronux.sokker.interfaces.IRunnableWithProgress;
import pl.pronux.sokker.interfaces.ISecurity;
import pl.pronux.sokker.model.GalleryImage;
import pl.pronux.sokker.model.ProxySettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.utils.file.OperationOnFile;
import pl.pronux.sokker.utils.security.Crypto;

public class GalleryImagesDownloader implements IRunnableWithProgress {
	private String mirror = "http://www.sokkerviewer.net/sv/logos/"; //$NON-NLS-1$
	private ArrayList<GalleryImage> images;

	public GalleryImagesDownloader(ArrayList<GalleryImage> images) {
		this.images = images;
	}

	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		String signature;
		HTMLDownloader htmlDownloader;

		monitor.beginTask(String.format("%s (1/4)", Messages.getString("GalleryImagesDownloader.label.xml.download")), 4); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			PublicKey pubk = Crypto.convertByteArrayToPublicKey(Crypto.decodeBase64(ISecurity.SERVER_PUBLIC_KEY), "RSA"); //$NON-NLS-1$

			ProxySettings proxySettings = SettingsHandler.getSokkerViewerSettings().getProxySettings();

			htmlDownloader = new HTMLDownloader(proxySettings);
			if(!new File(System.getProperty("user.dir") + File.separator + "tmp" + File.separator + "logos" + File.separator).exists()) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				new File(System.getProperty("user.dir") + File.separator + "tmp" + File.separator + "logos" + File.separator).mkdirs(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			htmlDownloader.getInternetFile(mirror + "images.xml", "images.xml", System.getProperty("user.dir") + File.separator + "tmp" + File.separator + "logos" + File.separator); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			signature = htmlDownloader.getPageInBytes(mirror + "images.xml.md5"); //$NON-NLS-1$

			monitor.worked(1);

			monitor.setTaskName(String.format("%s (2/4)" ,Messages.getString("GalleryImagesDownloader.label.xml.verify"))); //$NON-NLS-1$ //$NON-NLS-2$

			if (Crypto.verifySignature(pubk, Crypto.decodeBase64(signature), new File(System.getProperty("user.dir") + File.separator + "tmp" + File.separator + "logos" + File.separator + "images.xml"))) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				monitor.worked(1);
				// PARSE XML
				monitor.setTaskName(String.format("%s (3/4)", Messages.getString("GalleryImagesDownloader.label.xml.parse"))); //$NON-NLS-1$ //$NON-NLS-2$

				String xml = OperationOnFile.readFromFile(System.getProperty("user.dir") + File.separator + "tmp" + File.separator + "logos" + File.separator + "images.xml"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				final GalleryXMLParser parser = new GalleryXMLParser();
				InputSource input = new InputSource(new StringReader(xml));
				parser.parseXmlSax(input, null);

				monitor.worked(1);
				monitor.setTaskName(String.format("%s (4/4)", Messages.getString("GalleryImagesDownloader.label.xml.compare"))); //$NON-NLS-1$ //$NON-NLS-2$
				GalleryXMLParser oldParser = new GalleryXMLParser();
				if(!new File(System.getProperty("user.dir") + File.separator + "logos").exists()) { //$NON-NLS-1$ //$NON-NLS-2$
					new File(System.getProperty("user.dir") + File.separator + "logos").mkdir(); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					if(new File(System.getProperty("user.dir") + File.separator + "logos" + File.separator + "images.xml").exists()) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						InputSource inputOld = new InputSource(new FileReader(new File(System.getProperty("user.dir") + File.separator + "logos" + File.separator + "images.xml"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						oldParser.parseXmlSax(inputOld, null);
					}
				}


				images = parser.compareTo(oldParser);
				
				monitor.worked(1);
				
				monitor.beginTask(String.format("%s (1/4)", Messages.getString("GalleryImagesDownloader.label.image.download")), 1); //$NON-NLS-1$ //$NON-NLS-2$
				
				monitor.setTotalTime(images.size());
				for (GalleryImage image : images) {
					try {
						downloadPackage(mirror, System.getProperty("user.dir") + File.separator + "tmp" + File.separator + "logos" + File.separator, image, monitor); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					} catch (IOException ex) {
						throw new SVException(String.format("%s %s", Messages.getString("GalleryImagesDownloader.label.info.download"), image.getFile())); //$NON-NLS-1$ //$NON-NLS-2$
					}
					monitor.worked(1);
				}
				
				monitor.beginTask(String.format("%s (2/4)", Messages.getString("GalleryImagesDownloader.label.image.verify")), 1); //$NON-NLS-1$ //$NON-NLS-2$
				verifyImages(images, pubk, System.getProperty("user.dir") + File.separator + "tmp" + File.separator + "logos" + File.separator, monitor); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			
				monitor.beginTask(String.format("%s (3/4)", Messages.getString("GalleryImagesDownloader.label.image.copy")), 1); //$NON-NLS-1$ //$NON-NLS-2$
				OperationOnFile.copyDirectory(new File(System.getProperty("user.dir") + File.separator + "tmp"), new File(System.getProperty("user.dir"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			
				monitor.beginTask(String.format("%s (4/4)", Messages.getString("GalleryImagesDownloader.label.image.clean")), 1); //$NON-NLS-1$ //$NON-NLS-2$
				OperationOnFile.cleanDir(new File(System.getProperty("user.dir") + File.separator + "tmp")); //$NON-NLS-1$ //$NON-NLS-2$

				if(images.size() > 0) { 
					monitor.setTaskName(Messages.getString("message.information.restart")); //$NON-NLS-1$
				} else {
					monitor.setTaskName(Messages.getString("GalleryImagesDownloader.label.info.noimages")); //$NON-NLS-1$
				}
			} else {
				throw new SVException(Messages.getString("GalleryImagesDownloader.label.info.verify.xml")); //$NON-NLS-1$
			}
			monitor.done();
		} catch (SVException sve) {
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
//			throw new InvocationTargetException(e, Messages.getString("error.security.crypto"));
			throw new InvocationTargetException(e, e.getLocalizedMessage());
		}
	}
	
	private void downloadPackage(final String mirror, String tempDirectory, final GalleryImage image, IProgressMonitor monitor) throws IOException {
		int length;
		int counter;
		URL url = null;
		try {
			url = new URL(mirror + image.getFile());
			URLConnection con = url.openConnection();
			length = con.getContentLength();
		} catch (MalformedURLException e1) {
			length = -1;
		} catch (IOException e) {
			length = -1;
		}

		if (url == null) {
			throw new IOException(Messages.getString("exception.url.null")); //$NON-NLS-1$
		}

		try {
			File file = new File(tempDirectory);
			if (!file.exists()) {
				file.mkdirs();
			}
			byte[] buf = new byte[4096];
			int len;

			BufferedInputStream in = new BufferedInputStream(url.openStream());
			FileOutputStream out = new FileOutputStream(tempDirectory + image.getFile());
			counter = 0;
			while ((len = in.read(buf)) > 0) {
				counter = counter + (len);
				out.write(buf, 0, len);
				if (length != -1) {
					monitor.subTask(String.format("%s ( %dkb of %dkb )" ,mirror + image.getFile(), counter / 1000, length / 1000)); //$NON-NLS-1$
				} else {
					monitor.subTask(String.format("%s ( %dkb )", image.getFile(), counter / 1000)); //$NON-NLS-1$
				}
			}
			in.close();
			out.close();
		} catch (final IOException e) {
			throw e;
		}
	}
	
	private void verifyImages(List<GalleryImage> images, PublicKey publicKey, String temporaryDir, IProgressMonitor monitor) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, SVException, BadArgumentException {

		monitor.setTotalTime(images.size());
		for (GalleryImage image : images) {
			monitor.subTask(image.getFile());
			if (!verifyImage(image.getSignature(), publicKey, new File(temporaryDir + image.getFile()))) {
				throw new SVException(Messages.getString("updater.label.info.verify.package") + " " + image.getFile()); //$NON-NLS-1$ //$NON-NLS-2$
			}
			monitor.worked(1);
		}
	}
	
	private boolean verifyImage(String signature, PublicKey publicKey, File file) throws BadArgumentException {
		if(signature == null) {
			return false;
		} 
		byte[] sig = Crypto.decodeBase64(signature);
		return Crypto.verifySignature(publicKey, sig, file);
	}

	public void onFinish() {
	}
}
