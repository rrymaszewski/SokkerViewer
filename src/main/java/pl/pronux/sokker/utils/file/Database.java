package pl.pronux.sokker.utils.file;

import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;

import pl.pronux.sokker.model.SokkerViewerSettings;

public class Database {

	public static boolean backup(SokkerViewerSettings settings, String filename) throws IOException {
		File dbDir = new File(settings.getBackupDirectory());

		if (!dbDir.exists() && !OperationOnFile.createDirectory(dbDir)) {
			throw new IOException("Missing backup directory"); 
		}
		if (new File(dbDir, settings.getUsername()).exists()) {
			dbDir = new File(dbDir, settings.getUsername());
		} else {
			dbDir = new File(dbDir, settings.getUsername());
			dbDir.mkdir();
		}

		File dbFile = new File(settings.getBaseDirectory() + File.separator + "db" + File.separator + "db_file_" + settings.getUsername() + ".script");   
		if (dbFile.exists()) {
			File dbBakFile = new File(dbDir, filename);
			OperationOnFile.copyFile(dbFile, dbBakFile);
		}
		return true;
	}

	public static boolean backup(SokkerViewerSettings settings) throws IOException {
		return backup(settings, new GregorianCalendar().getTimeInMillis() + ".bak"); 
	}

	public static void restore(SokkerViewerSettings settings, String filename) throws IOException {
		File dbDir = new File(settings.getBackupDirectory() + File.separator + settings.getUsername() + File.separator);
		File dbFile = new File(settings.getBaseDirectory() + File.separator + "db" + File.separator + "db_file_" + settings.getUsername() + ".script");   
		File dbBakFile = new File(dbDir, filename);
		OperationOnFile.copyFile(dbBakFile, dbFile);
	}
}
