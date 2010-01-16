package pl.pronux.sokker.data.sql;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.utils.Log;

public class SQLSession {

	static Connection connection;

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

	public static void close(Connection connection) throws SQLException {
		connection.close();
	}

	public static void commit() throws SQLException {
		SQLSession.getConnection().commit();
	}

	public static Connection connect() throws SQLException {
		register();
		SQLSession.setConnection(null);
		try {
			// The second and third arguments are the username and password,
			// respectively. They should be whatever is necessary to connect
			// to the database.
			SokkerViewerSettings settings = SQLQuery.getSettings();
			SQLSession.setConnection(DriverManager.getConnection("jdbc:hsqldb:" + settings.getBaseDirectory() + File.separator + "db" + File.separator
																 + "db_file_" + settings.getUsername() + ";shutdown=true", "sa", ""));
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

	static void register() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException cnfe) {
			Log.error("Couldn't find the driver!", cnfe);
		}
	}

	public static void rollback() throws SQLException {
		if (SQLSession.getConnection() != null && !SQLSession.getConnection().isClosed()) {
			SQLSession.getConnection().rollback();
		}
	}

	public static void setConnection(Connection connection) {
		SQLSession.connection = connection;
	}

	public static void close(boolean newConnection) throws SQLException {
		if (newConnection) {
			SQLSession.close();
		}
	}
}
