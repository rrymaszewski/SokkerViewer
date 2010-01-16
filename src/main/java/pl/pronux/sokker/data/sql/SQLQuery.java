package pl.pronux.sokker.data.sql;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import pl.pronux.sokker.interfaces.SV;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.utils.Log;
import pl.pronux.sokker.utils.file.OperationOnFile;

public class SQLQuery {

	private static Statement batchStm;
	private static SokkerViewerSettings settings;

	private synchronized static void addBatch(String expression) throws SQLException {
		batchStm.addBatch(expression);
	}

	private synchronized static void clearBatch() throws SQLException {
		batchStm.clearBatch();
	}

	private synchronized static void closeBatch() throws SQLException {
		try {
			batchStm.close();
		} catch (SQLException e) {
			throw e;
		}
	}

	// use for SQL commands CREATE, DROP, INSERT and UPDATE
	private synchronized static void createBatch() throws SQLException {
		batchStm = SQLSession.getConnection().createStatement();
	}

	public static boolean dbExist() {
		String db = settings.getBaseDirectory() + File.separator + "db" + File.separator + "db_file_" + settings.getUsername() + ".script";
		String log = settings.getBaseDirectory() + File.separator + "db" + File.separator + "db_file_" + settings.getUsername() + ".log";
		return new File(db).exists() || new File(log).exists();
	}

	public static boolean dbPropertiesExist() {
		String file = settings.getBaseDirectory() + File.separator + "db" + File.separator + "db_file_" + settings.getUsername() + ".properties";
		return new File(file).exists();
	}

	private synchronized static void executeBatch() throws SQLException {
		int[] batchArray = batchStm.executeBatch();
		Log.info("Results");
		for (int i = 0; i < batchArray.length; i++) {
			Log.info(String.valueOf(batchArray[i]));
		}
	}

	public static void initDB() throws SQLException, IOException {
		createBatch();
		Log.info("Init DB  " + SV.DB_VERSION);
		String sqlBatch = OperationOnFile.readFromFile(settings.getBaseDirectory() + File.separator + "sql" + File.separator + "0.sql");

		if (sqlBatch != null) {
			String[] SQLBatchArray = sqlBatch.split("\n");
			for (int i = 0; i < SQLBatchArray.length; i++) {
				addBatch(SQLBatchArray[i]);
			}
			addBatch("UPDATE system SET version = " + SV.DB_VERSION);
			executeBatch();
			clearBatch();
		}
		closeBatch();
	}

	public static void setConnection(Connection connection) {
		SQLSession.setConnection(connection);
	}

	// use for SQL commands CREATE, DROP, INSERT and UPDATE
	public synchronized static void update(String expression) throws SQLException {
		Statement stm = null;
		try {
			stm = SQLSession.getConnection().createStatement();
			stm.executeUpdate(expression);
			stm.close();
		} catch (SQLException e) {
			throw e;
		}
	}

	public static void updateDB(Connection connection, int dbVersion) throws ClassNotFoundException, SQLException, IOException {
		createBatch();
		Log.info("Update DB  " + dbVersion);
		String sqlBatch = OperationOnFile.readFromFile(settings.getBaseDirectory() + File.separator + "sql" + File.separator + dbVersion + ".sql");
		if (sqlBatch != null) {
			String[] SQLBatchArray = sqlBatch.split("\n");
			for (int i = 0; i < SQLBatchArray.length; i++) {
				addBatch(SQLBatchArray[i]);
			}
			executeBatch();
			clearBatch();
		}
		closeBatch();
	}

	/**
	 * @return boolean<BR>
	 *         true -> use new connection false -> use old connection
	 */
	public static boolean connect() throws SQLException {
		if (SQLSession.getConnection() != null && !SQLSession.getConnection().isClosed()) {
			return false;
		}
		SQLSession.connect();
		return true;
	}

	public static void close(boolean newConnection) throws SQLException {
		if (newConnection && SQLSession.getConnection() != null) {
			SQLSession.close();
		}
	}

	public static SokkerViewerSettings getSettings() {
		return settings;
	}

	public static void setSettings(SokkerViewerSettings settings) {
		SQLQuery.settings = settings;
	}

}
