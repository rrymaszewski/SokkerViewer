package pl.pronux.sokker.utils.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.interfaces.IProgressMonitor;
import pl.pronux.sokker.model.PlayerInterface;
import pl.pronux.sokker.resources.Messages;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

public class OperationOnFile {

	public static void appendToFile(String fileName, String stringToWrite) throws IOException {

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
			out.write(stringToWrite);
			out.close();
		} catch (IOException e) {
			throw e;
		}

	}

	public static void serializePlayer(PlayerInterface player, String filename) throws Exception {
		try {
			if (!filename.contains(".sv_")) { //$NON-NLS-1$
				filename += ".sv_"; //$NON-NLS-1$
			}
			FileOutputStream out = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(player);
			oos.flush();
		} catch (Exception e) {
			throw e;
		}
	}

	public static PlayerInterface serializePlayer(String filename) throws Exception {
		PlayerInterface player = null;
		try {
			FileInputStream in = new FileInputStream(filename);
			ObjectInputStream ois = new ObjectInputStream(in);
			player = (PlayerInterface) (ois.readObject());
		} catch (Exception e) {
			throw e;
		}
		return player;
	}

	public static void appendToFileUTF(String fileName, String stringToWrite) throws IOException {
		try {
			OutputStreamWriter out3 = new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8"); //$NON-NLS-1$
			// BufferedWriter out2 = new BufferedWriter(new
			// FileWriter("c:\\test.xml"));
			out3.write(stringToWrite);
			out3.close();
		} catch (IOException e) {
			throw e;
		}

	}

	public static void backup() {

	}

	public static void convertISO88592toUTF() {

	}

	public static void copyFile(File inputFile, File outputFile) throws IOException {

		FileChannel sourceChannel = new FileInputStream(inputFile).getChannel();
		FileChannel destinationChannel = new FileOutputStream(outputFile).getChannel();
		sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
		// or
		// destinationChannel.transferFrom
		// (sourceChannel, 0, sourceChannel.size());
		sourceChannel.close();
		destinationChannel.close();
		// FileInputStream in = new FileInputStream(inputFile);
		// FileOutputStream out = new FileOutputStream(outputFile);
		// int c;
		//		
		// while ((c = in.read()) != -1) {
		// out.write(c);
		// }
		//		
		// in.close();
		// out.close();
	}

	public static void copyDirectory(File srcDir, File dstDir) throws IOException {
		if (srcDir.isDirectory()) {
			if (!dstDir.exists()) {
				dstDir.mkdir();
			}

			String[] children = srcDir.list();
			for (int i = 0; i < children.length; i++) {
				copyDirectory(new File(srcDir, children[i]), new File(dstDir, children[i]));
			}
		} else {
			copyFile(srcDir, dstDir);
		}
	}

	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	public static boolean cleanDir(File dir) {
		String[] children = dir.list();
		for (int i = 0; i < children.length; i++) {
			if (new File(children[i]).isDirectory()) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			} else {
				new File(dir, children[i]).delete();
			}
		}
		return true;
	}

	public static boolean moveDirectory(File source, String destination) {
		// Move file to new directory
		boolean success = source.renameTo(new File(destination));

		ArrayList<File> files = getDirList(source);

		for (File file : files) {
			moveFile(file, destination);
		}

		if (!success) {
			return false;
		}
		return true;
	}

	public static boolean createDirectory(File dir) {
		return dir.mkdirs();
	}

	public static boolean moveFile(File source, String destination) {
		// Move file to new directory
		boolean success = source.renameTo(new File(destination, source.getName()));

		if (!success) {
			return false;
		}
		return true;
	}

	public static String readFromFile(String fileName) throws IOException {
		return readFromFile(fileName, null);
	}

	public static String readFromFile(String fileName, String encoding) throws IOException {
		String buffer = ""; //$NON-NLS-1$
		InputStreamReader in = null;
		try {
			if (encoding != null) {
				in = new InputStreamReader(new FileInputStream(new File(fileName)), encoding);
			} else {
				in = new InputStreamReader(new FileInputStream(new File(fileName)));
			}

			int c;
			StringBuffer sb = new StringBuffer();
			while ((c = in.read()) != -1) {
				sb.append((char) c);
			}
			buffer = sb.toString();
			in.close();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return (buffer);

	}

	public static String readFromFile(File file) throws IOException {
		return readFromFile(file, null);
	}

	public static String readFromFile(File file, String encoding) throws IOException {
		String buffer = ""; //$NON-NLS-1$
		InputStreamReader in = null;
		int c;
		try {
			if (encoding != null) {
				in = new InputStreamReader(new FileInputStream(file), encoding);
			} else {
				in = new InputStreamReader(new FileInputStream(file));
			}

			StringBuffer sb = new StringBuffer();
			while ((c = in.read()) != -1) {
				sb.append((char) c);
			}
			buffer = sb.toString();

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}

		}
		return (buffer);

	}

	public static boolean createDirectory(String directoryName) {
		if (directoryName == null) {
			return false;
		}
		boolean success = (new File(directoryName)).mkdirs();
		if (!success) {
			return false;
		}
		return true;
	}

	public static void generateToPDF(String file, String text) throws IOException, DocumentException {
		if (!file.contains(".pdf")) { //$NON-NLS-1$
			file += ".pdf"; //$NON-NLS-1$
		}
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer that listens to the document
			// and directs a PDF-stream to a file
			PdfWriter.getInstance(document, new FileOutputStream(file));

			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			BaseFont bfCourier = BaseFont.createFont(SettingsHandler.getSokkerViewerSettings().getBaseDirectory() + File.separator
													 + "ext" + File.separator + "cour.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED); //$NON-NLS-1$ //$NON-NLS-2$
			Font font = new Font(bfCourier, 12);
			document.add(new Paragraph(text, font));
		} catch (DocumentException de) {
			throw de;
		} catch (IOException ioe) {
			throw ioe;
		}

		// step 5: we close the document
		document.close();
	}

	public static ArrayList<File> visitAllDirs(File dir, FileFilter filter, ArrayList<File> listFiles) {
		// This filter only returns directories

		if (dir.isDirectory()) {
			File[] children = dir.listFiles(filter);
			for (int i = 0; i < children.length; i++) {
				visitAllDirs(children[i], filter, listFiles);
			}
			return listFiles;
		} else {
			listFiles.add(dir);
			return listFiles;
		}
	}

	public static ArrayList<File> visitAllDirs(File dir, FileFilter filter, ArrayList<File> listFiles, int level) {
		// This filter only returns directories
		if (dir.isDirectory()) {
			File[] children = dir.listFiles(filter);
			if (level > 0) {
				for (int i = 0; i < children.length; i++) {
					visitAllDirs(children[i], filter, listFiles, level - 1);
				}
			}
			return listFiles;
		} else {
			listFiles.add(dir);
			return listFiles;
		}
	}

	public static ArrayList<File> getFileChildren(File dir, FileFilter filter, ArrayList<File> listFiles, IProgressMonitor monitor) {
		// This filter only returns directories
		if (dir.isDirectory()) {
			monitor.beginTask(Messages.getString("OperationOnFile.scanning"), 1); //$NON-NLS-1$
			File[] children = dir.listFiles(filter);
			monitor.worked(1);
			monitor.beginTask(Messages.getString("OperationOnFile.checking"), children.length); //$NON-NLS-1$
			for (int i = 0; i < children.length; i++) {
				monitor.subTask(Messages.getString("OperationOnFile.checking.file") + " " + children[i].getName() + i + File.separator + children.length); //$NON-NLS-1$ //$NON-NLS-2$
				if (children[i].isFile()) {
					listFiles.add(children[i]);
				}
				monitor.worked(1);

			}
		}
		return listFiles;
	}

	public static ArrayList<String> getChildrensPath(File dir, FileFilter filter, ArrayList<String> listFiles, int level) {
		// This filter only returns directories
		if (dir.isDirectory()) {
			File[] children = dir.listFiles(filter);
			if (level > 0) {
				for (int i = 0; i < children.length; i++) {
					getChildrensPath(children[i], filter, listFiles, level - 1);
				}
			}
			return listFiles;
		} else {
			listFiles.add(dir.getPath());
			return listFiles;
		}
	}

	public static ArrayList<File> getDirList(File dir) {
		// This filter only returns directories
		ArrayList<File> files = new ArrayList<File>();
		File[] children = dir.listFiles();
		if (children == null) {
			// Either dir does not exist or is not a directory
		} else {
			for (int i = 0; i < children.length; i++) {
				// Get filename of file or directory
				files.add(children[i]);
			}
		}
		return files;
	}

	public static void writeToFile(String fileName, String stringToWrite) throws IOException {
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
		out.write(stringToWrite);
		out.close();

		// try {
		// BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
		// out.write(new String(stringToWrite.getBytes("UTF-8")));
		// out.close();
		// } catch (IOException e) {
		// }
	}

	public static void writeToFileLATIN1(String fileName, String stringToWrite) throws IOException {
		OutputStreamWriter out3 = new OutputStreamWriter(new FileOutputStream(fileName), "ISO-8859-1"); //$NON-NLS-1$
		// BufferedWriter out2 = new BufferedWriter(new
		// FileWriter("c:\\test.xml"));
		out3.write(stringToWrite);
		out3.close();

		// try {
		// BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
		// out.write(new String(stringToWrite.getBytes("UTF-8")));
		// out.close();
		// } catch (IOException e) {
		// }
	}

	public static void writeToFileUTF(String fileName, String stringToWrite) throws IOException {
		OutputStreamWriter out3 = new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"); //$NON-NLS-1$
		// BufferedWriter out2 = new BufferedWriter(new
		// FileWriter("c:\\test.xml"));
		out3.write(stringToWrite);
		out3.close();

		// try {
		// BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
		// out.write(new String(stringToWrite.getBytes("UTF-8")));
		// out.close();
		// } catch (IOException e) {
		// }
	}

}
