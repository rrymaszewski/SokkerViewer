package pl.pronux.sokker.data.sql;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.utils.Log;

public class SQLSession {

	private static Connection connection;

	public static void beginTransaction() throws SQLException {
		if (SQLSession.getConnection() != null) {
			SQLSession.getConnection().setAutoCommit(false);
		}
	}

	public static void close() throws SQLException {
		try {
			if (SQLSession.getConnection() != null && !SQLSession.getConnection().isClosed()) {
				SQLSession.getConnection().close();
			}
		} catch (SQLException e) {
			throw e;
		}
	}

	public static void commit() throws SQLException {
		SQLSession.getConnection().commit();
	}

	public static Connection connect() throws SQLException {
		SokkerViewerSettings settings = SQLQuery.getSettings();
		String filename = settings.getBaseDirectory() + File.separator + "db" + File.separator + "db_file_" + settings.getUsername();
		return connect(filename);
	}

	public static Connection connect(String filename) throws SQLException {
		loadDatabaseDriver("org.hsqldb.jdbcDriver");
		connection = null;
		try {
			// The second and third arguments are the username and password,
			// respectively. They should be whatever is necessary to connect
			// to the database.

			connection = DriverManager.getConnection("jdbc:hsqldb:" + filename + ";shutdown=true", "sa", "");
		} catch (SQLException se) {
			Log.warning("Couldn't connect: print out a stack trace and exit.");
			throw se;
		}
		if (SQLSession.getConnection() == null) {
			throw new SQLException("We should never get here.");
		}
		return SQLSession.getConnection();
	}

	public static void endTransaction() throws SQLException {
		if (SQLSession.getConnection() != null) {
			SQLSession.getConnection().setAutoCommit(true);
		}
	}

	public static Connection getConnection() {
		return SQLSession.connection;
	}

	private static void loadDatabaseDriver(String driverName) {
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException cnfe) {
			Log.error("Couldn't find the driver!", cnfe);
		}
	}

	public static void rollback() throws SQLException {
		if (SQLSession.getConnection() != null && !SQLSession.getConnection().isClosed()) {
			SQLSession.getConnection().rollback();
		}
	}

	public static void close(boolean newConnection) throws SQLException {
		if (newConnection) {
			SQLSession.close();
		}
	}
}
