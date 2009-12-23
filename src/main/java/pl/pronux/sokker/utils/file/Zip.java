package pl.pronux.sokker.utils.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import pl.pronux.sokker.utils.Log;

public class Zip {

	final public static int BUFFER_SIZE = 10240;

	public static void createZipArchive(File archiveFile, File[] tobeZippedFiles) throws IOException {
		byte buffer[] = new byte[BUFFER_SIZE];
		// Open archive file
		FileOutputStream stream = new FileOutputStream(archiveFile);
		ZipOutputStream out = new ZipOutputStream(stream);

		for (int i = 0; i < tobeZippedFiles.length; i++) {
			if (tobeZippedFiles[i] == null || !tobeZippedFiles[i].exists() || tobeZippedFiles[i].isDirectory()) {
				continue;
			}
			// Add archive entry
			ZipEntry zipAdd = new ZipEntry(tobeZippedFiles[i].getName());
			zipAdd.setTime(tobeZippedFiles[i].lastModified());
			out.putNextEntry(zipAdd);

			// Read input & write to output
			FileInputStream in = new FileInputStream(tobeZippedFiles[i]);
			while (true) {
				int nRead = in.read(buffer, 0, buffer.length);
				if (nRead <= 0) {
					break;
				}
				out.write(buffer, 0, nRead);
			}
			in.close();
		}

		out.close();
		stream.close();
		Log.info(Zip.class, "Adding completed OK"); //$NON-NLS-1$
	}

	public static void unzip(String filename) throws IOException {
		InputStream in = new BufferedInputStream(new FileInputStream(filename));
		ZipInputStream zin = new ZipInputStream(in);
		ZipEntry e;

		while ((e = zin.getNextEntry()) != null) {
			unzip(zin, e.getName());
			zin.close();
		}
	}

	private static void unzip(ZipInputStream zin, String s) throws IOException {
		FileOutputStream out = new FileOutputStream(s);
		byte[] b = new byte[512];
		int len = 0;
		while ((len = zin.read(b)) != -1) {
			out.write(b, 0, len);
		}
		out.close();
	}
}