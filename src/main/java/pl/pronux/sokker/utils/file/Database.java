package pl.pronux.sokker.utils.file;

import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;

import pl.pronux.sokker.model.DatabaseSettings;
import pl.pronux.sokker.model.SokkerViewerSettings;

public class Database {

	public static boolean backup(SokkerViewerSettings settings, String filename) throws IOException {
		DatabaseSettings databaseSettings = settings.getDatabaseSettings();
		if (databaseSettings.getType().equals(DatabaseSettings.HSQLDB)) {
			File dbDir = new File(settings.getBackupDirectory());

			if (!dbDir.exists()) {
				if (!OperationOnFile.createDirectory(dbDir)) {
					throw new IOException("Missing backup directory"); //$NON-NLS-1$
				}
			}
			if (new File(dbDir, settings.getUsername()).exists()) {
				dbDir = new File(dbDir, settings.getUsername());
			} else {
				dbDir = new File(dbDir, settings.getUsername());
				dbDir.mkdir();
			}

			File dbFile = new File(settings.getBaseDirectory() + File.separator + "db" + File.separator + "db_file_" + settings.getUsername() + ".script"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if (dbFile.exists()) {
				File dbBakFile = new File(dbDir, filename);
				OperationOnFile.copyFile(dbFile, dbBakFile);
			}
			return true;
		}
		return false;
	}

	public static boolean backup(SokkerViewerSettings settings) throws IOException {
		return backup(settings, new GregorianCalendar().getTimeInMillis() + ".bak"); //$NON-NLS-1$
	}

	public static void restore(SokkerViewerSettings settings, String filename) throws IOException {
		DatabaseSettings databaseSettings = settings.getDatabaseSettings();
		if (databaseSettings.getType().equals(DatabaseSettings.HSQLDB)) {

			File dbDir = new File(settings.getBackupDirectory() + File.separator + settings.getUsername() + File.separator);

			File dbFile = new File(settings.getBaseDirectory() + File.separator + "db" + File.separator + "db_file_" + settings.getUsername() + ".script"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			File dbBakFile = new File(dbDir, filename);

			OperationOnFile.copyFile(dbBakFile, dbFile);
		}
	}
}
