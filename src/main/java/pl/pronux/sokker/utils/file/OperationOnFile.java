package pl.pronux.sokker.utils.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	public static void serializePlayer(PlayerInterface player, String filename) throws IOException {
		if (!filename.contains(".sv_")) { 
			filename += ".sv_"; 
		}
		FileOutputStream out = null;
		ObjectOutputStream oos = null;
		try {
			out = new FileOutputStream(filename);
			oos = new ObjectOutputStream(out);
			oos.writeObject(player);
			oos.flush();
		} finally {
			if (out != null) {
				out.close();	
			}
			if (oos != null) {
				oos.close();
			}
		}
	}

	public static PlayerInterface serializePlayer(String filename) throws IOException, ClassNotFoundException  {
		PlayerInterface player = null;
		ObjectInputStream ois = null;
		try {
			FileInputStream in = new FileInputStream(filename);
			ois = new ObjectInputStream(in);
			player = (PlayerInterface) (ois.readObject());
		} finally {
			if (ois != null) {
				ois.close();
			}
		}
		return player;
	}

	public static void copyFile(File inputFile, File outputFile) throws IOException {
		FileChannel sourceChannel = null;
		FileChannel destinationChannel = null;
		try {
			sourceChannel = new FileInputStream(inputFile).getChannel();
			destinationChannel = new FileOutputStream(outputFile).getChannel();
			sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
		} finally {
			if (sourceChannel != null) {
				sourceChannel.close();	
			}
			if (destinationChannel != null) {
				destinationChannel.close();	
			}
		}
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

		if (success) {
			List<File> files = getDirList(source);
			for (File file : files) {
				moveFile(file, destination);
			}
		}
		return success;
	}

	public static boolean createDirectory(File dir) {
		return dir.mkdirs();
	}

	public static boolean moveFile(File source, String destination) {
		// Move file to new directory
		boolean success = source.renameTo(new File(destination, source.getName()));
		return success;
	}

	public static String readFromFile(String fileName) throws IOException {
		return readFromFile(fileName, null);
	}

	public static String readFromFile(String fileName, String encoding) throws IOException {
		return readFromFile(new File(fileName), encoding);
	}

	public static String readFromFile(File file) throws IOException {
		return readFromFile(file, null);
	}

	public static String readFromFile(File file, String encoding) throws IOException {
		StringBuilder sb = new StringBuilder();
		InputStreamReader in = null;
		int c;
		try {
			if (encoding != null) {
				in = new InputStreamReader(new FileInputStream(file), encoding);
			} else {
				in = new InputStreamReader(new FileInputStream(file));
			}
			while ((c = in.read()) != -1) {
				sb.append((char) c);
			}
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return sb.toString();
	}

	public static boolean createDirectory(String directoryName) {
		if (directoryName == null) {
			return false;
		}
		boolean success = (new File(directoryName)).mkdirs();
		return success;
	}

	public static void generateToPDF(String file, String text) throws IOException, DocumentException {
		if (!file.contains(".pdf")) { 
			file += ".pdf"; 
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
													 + "ext" + File.separator + "cour.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);  
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

	public static List<File> visitAllDirs(File dir, FileFilter filter, List<File> listFiles) {
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

	public static List<File> visitAllDirs(File dir, FileFilter filter, List<File> listFiles, int level) {
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

	public static List<File> getFileChildren(File dir, FileFilter filter, List<File> listFiles, IProgressMonitor monitor) {
		// This filter only returns directories
		if (dir.isDirectory()) {
			monitor.beginTask(Messages.getString("OperationOnFile.scanning"), 1); 
			File[] children = dir.listFiles(filter);
			monitor.worked(1);
			monitor.beginTask(Messages.getString("OperationOnFile.checking"), children.length); 
			for (int i = 0; i < children.length; i++) {
				monitor.subTask(Messages.getString("OperationOnFile.checking.file") + " " + children[i].getName() + i + File.separator + children.length);  
				if (children[i].isFile()) {
					listFiles.add(children[i]);
				}
				monitor.worked(1);
			}
		}
		return listFiles;
	}

	public static List<String> getChildrensPath(File dir, FileFilter filter, List<String> listFiles, int level) {
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

	public static List<File> getDirList(File dir) {
		// This filter only returns directories
		File[] children = dir.listFiles();
		List<File> files = new ArrayList<File>();
		if (children != null) {
			files.addAll(Arrays.asList(children));
		}
		return files;
	}

	public static void writeToFile(String fileName, String stringToWrite) throws IOException {
		Writer out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
			out.write(stringToWrite);
			out.flush();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	public static void writeToFileLATIN1(String filename, String content) throws IOException {
	 	writeToFile(filename, content, "ISO-8859-1"); 
	}

	public static void writeToFileUTF(String filename, String content) throws IOException {
		writeToFile(filename, content, "UTF-8"); 
	}
	
	private static void writeToFile(String filename, String content, String encoding) throws IOException {
		OutputStreamWriter out = null;
		try {
			out = new OutputStreamWriter(new FileOutputStream(filename), encoding); 
			out.write(content);
			out.flush();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}