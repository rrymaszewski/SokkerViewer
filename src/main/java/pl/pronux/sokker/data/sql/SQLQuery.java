package pl.pronux.sokker.data.sql;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import pl.pronux.sokker.actions.PlayersManager;
import pl.pronux.sokker.data.sql.dao.AssistantDao;
import pl.pronux.sokker.interfaces.SV;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.model.Training;
import pl.pronux.sokker.model.Transfer;
import pl.pronux.sokker.utils.Log;
import pl.pronux.sokker.utils.file.OperationOnFile;

public class SQLQuery {
	private static Statement batchStm;
	private static SokkerViewerSettings settings;
	
	private static PlayersManager playersManager = PlayersManager.instance();

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
		String db = settings.getBaseDirectory() + File.separator + "db" + File.separator + "db_file_" + settings.getUsername() + ".script"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String log = settings.getBaseDirectory() + File.separator + "db" + File.separator + "db_file_" + settings.getUsername() + ".log"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return new File(db).exists() || new File(log).exists();
	}

	public static boolean dbPropertiesExist() {
		String file = settings.getBaseDirectory() + File.separator + "db" + File.separator + "db_file_" + settings.getUsername() + ".properties"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return new File(file).exists();
	}

	private synchronized static void executeBatch() throws SQLException {
		int[] batchArray = batchStm.executeBatch();
		Log.info("Results"); //$NON-NLS-1$
		for (int i = 0; i < batchArray.length; i++) {
			Log.info(String.valueOf(batchArray[i]));
		}

	}

	public static void initDB() throws SQLException, IOException {
		createBatch();
		Log.info("Init DB  " + SV.DB_VERSION); //$NON-NLS-1$
		String sqlBatch = OperationOnFile.readFromFile(settings.getBaseDirectory() + File.separator + "sql" + File.separator + "0.sql"); //$NON-NLS-1$ //$NON-NLS-2$

		if (sqlBatch != null) {
			String[] SQLBatchArray = sqlBatch.split("\n"); //$NON-NLS-1$
			for (int i = 0; i < SQLBatchArray.length; i++) {
				addBatch(SQLBatchArray[i]);
			}
			addBatch("UPDATE system SET version = " + SV.DB_VERSION); //$NON-NLS-1$
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
		Log.info("Update DB  " + dbVersion); //$NON-NLS-1$
		String sqlBatch = OperationOnFile.readFromFile(settings.getBaseDirectory() + File.separator + "sql" + File.separator + dbVersion + ".sql"); //$NON-NLS-1$ //$NON-NLS-2$
		if (sqlBatch != null) {
			String[] SQLBatchArray = sqlBatch.split("\n"); //$NON-NLS-1$
			for (int i = 0; i < SQLBatchArray.length; i++) {
				addBatch(SQLBatchArray[i]);
			}
			// executeBatch(SQLBatch);
			// update(SQLBatch);
			executeBatch();
			clearBatch();
		}

		switch (dbVersion) {
		case 1:
			ArrayList<Player> players = playersManager.getPlayers(null, new HashMap<Integer, Junior>(), new HashMap<Integer, Training>(), new HashMap<Integer, Transfer>(),
					new HashMap<Integer, Transfer>());
			int[][] data = AssistantDao.getAssistantData();
			for (Player player : players) {
				player.setPositionTable(playersManager.calculatePosition(player, data));
				player.setPosition(player.getBestPosition());
			}
			playersManager.updatePlayersPositions(players);
			break;
		case 4:
			try {
				SQLQuery.update("ALTER TABLE player_skills DROP COLUMN training;"); //$NON-NLS-1$
			} catch (SQLException e) {
			}
			break;
		default:
			break;
		}
		closeBatch();
	}

	/**
	 * @return boolean<BR>
	 *         true -> use new connection false -> use old connection
	 */
	public static boolean connect() throws SQLException {
		if (SQLSession.getConnection() != null) {
			if (!SQLSession.getConnection().isClosed()) {
				return false;
			}
		}
		SQLSession.connect();
		return true;
	}

	public static void close(boolean newConnection) throws SQLException {
		if (newConnection) {
			if (SQLSession.getConnection() != null) {
				SQLSession.close();
			}
		}
	}

	public static SokkerViewerSettings getSettings() {
		return settings;
	}

	public static void setSettings(SokkerViewerSettings settings) {
		SQLQuery.settings = settings;
	}

}
