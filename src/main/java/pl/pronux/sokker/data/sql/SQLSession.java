package pl.pronux.sokker.data.sql;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import pl.pronux.sokker.model.DatabaseSettings;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.utils.Log;

public class SQLSession {
	static Connection connection;

	public static int databaseType;

	public static final int HSQLDB = 2;

	public static final int POSTGRESQL = 1;

	public static void beginTransaction() throws SQLException {
		if (SQLSession.getConnection() != null) {
			SQLSession.getConnection().setAutoCommit(false);
		}
	}

	public static void close() throws SQLException {
		if (SQLSession.getConnection() != null) {
			try {
				if (!SQLSession.getConnection().isClosed()) {
					SQLSession.getConnection().close();
				}
			} catch (SQLException e) {
				throw e;
			}
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
			DatabaseSettings databaseSettings = settings.getDatabaseSettings();
			if (databaseSettings.getType().equalsIgnoreCase(DatabaseSettings.POSTGRESQL)) {
				SQLSession
						.setConnection(DriverManager
								.getConnection(
										"jdbc:postgresql://" + databaseSettings.getServer() + "/" + databaseSettings.getName() + "", "" + databaseSettings.getUsername() + "", "" + databaseSettings.getPassword() + "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
				SQLSession.databaseType = SQLSession.POSTGRESQL;
			} else if (databaseSettings.getType().equalsIgnoreCase(DatabaseSettings.HSQLDB)) {
				SQLSession.setConnection(DriverManager.getConnection(
						"jdbc:hsqldb:" + settings.getBaseDirectory() + File.separator + "db" + File.separator + "db_file_" + settings.getUsername() + ";shutdown=true", "sa", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
				SQLSession.databaseType = SQLSession.HSQLDB;
			}
		} catch (SQLException se) {
			Log.warning(SQLSession.class, "Couldn't connect: print out a stack trace and exit."); //$NON-NLS-1$
			throw se;
		}
		if (SQLSession.getConnection() != null) {
		}
		// System.out.println("Hooray! We connected to the database!");
		else {
			throw new SQLException("We should never get here."); //$NON-NLS-1$
		}
		return SQLSession.getConnection();
	}

	public static Connection connect(String file) throws ClassNotFoundException, SQLException {
		Class.forName("org.hsqldb.jdbcDriver"); //$NON-NLS-1$

		Connection connection = DriverManager.getConnection("jdbc:hsqldb:" + file + ";shutdown=true", "sa", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		return connection;
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
		// System.out.println("Checking if Driver is registered with
		// DriverManager.");
		DatabaseSettings databaseSettings = SQLQuery.getSettings().getDatabaseSettings();
		try {
			if (databaseSettings.getType().equalsIgnoreCase(DatabaseSettings.POSTGRESQL)) {
				Class.forName("org.postgresql.Driver"); //$NON-NLS-1$
			} else if (databaseSettings.getType().equalsIgnoreCase(DatabaseSettings.HSQLDB)) {
				Class.forName("org.hsqldb.jdbcDriver"); //$NON-NLS-1$
			}

		} catch (ClassNotFoundException cnfe) {
			Log.error(SQLSession.class, "Couldn't find the driver!"); //$NON-NLS-1$
			Log.error(SQLSession.class, "Let's print a stack trace, and exit."); //$NON-NLS-1$
			Log.error(SQLSession.class, "Sql Class", cnfe); //$NON-NLS-1$
		}

		// System.out.println("Registered the driver ok, so let's make a
		// connection.");

	}

	public static void rollback() throws SQLException {
		if (SQLSession.getConnection() != null) {
			if (!SQLSession.getConnection().isClosed()) {
				SQLSession.getConnection().rollback();
			}
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
