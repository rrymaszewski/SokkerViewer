package pl.pronux.sokker.updater.model;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.PublicKey;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import pl.pronux.sokker.exceptions.BadArgumentException;
import pl.pronux.sokker.utils.security.Crypto;

public class Package {
	private String name;

	private int revision;

	private String signature;

	private String path;

	private String author;

	private String localpath;

	private String filename;

	private String rootDirectory;

	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRootDirectory() {
		return rootDirectory;
	}

	public void setRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getLocalpath() {
		return localpath;
	}

	public void setLocalpath(String localpath) {
		this.localpath = localpath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public boolean verify(PublicKey publicKey) throws IOException, BadArgumentException {
		if (signature == null) {
			return false;
		}
		byte[] sig = Crypto.decodeBase64(signature);
		return Crypto.verifySignature(publicKey, sig, new File(rootDirectory + localpath + filename));
	}

	public void unzip() throws IOException {
		ZipFile zipFile;
		Enumeration<? extends ZipEntry> entries;
		if (filename.matches("[a-zA-Z0-9.-]+.zip")) { //$NON-NLS-1$
			zipFile = new ZipFile(rootDirectory + localpath + filename);

			entries = zipFile.entries();

			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();

				if (entry.isDirectory()) {
					// Assume directories are stored parents first then
					// children.
					// This is not robust, just for demonstration purposes.
					(new File(rootDirectory + File.separator + localpath + File.separator + entry.getName())).mkdirs();
					continue;
				}

				copyInputStream(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(rootDirectory + File.separator + localpath + File.separator + entry.getName())));
			}

			zipFile.close();
		}
	}

	private static void copyInputStream(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		while ((len = in.read(buffer)) >= 0) {
			out.write(buffer, 0, len);
		}

		in.close();
		out.close();
	}
}
