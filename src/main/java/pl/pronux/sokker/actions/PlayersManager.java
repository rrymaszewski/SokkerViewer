package pl.pronux.sokker.actions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import pl.pronux.sokker.data.sql.SQLQuery;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.data.sql.dao.AssistantDao;
import pl.pronux.sokker.data.sql.dao.PlayersArchiveDao;
import pl.pronux.sokker.data.sql.dao.PlayersDao;
import pl.pronux.sokker.interfaces.DateConst;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.NtSkills;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.PlayerArchive;
import pl.pronux.sokker.model.PlayerInterface;
import pl.pronux.sokker.model.PlayerSkills;
import pl.pronux.sokker.model.PlayerStats;
import pl.pronux.sokker.model.Training;
import pl.pronux.sokker.model.Transfer;

public class PlayersManager {

	private final static PlayersManager _instance = new PlayersManager();

	private PlayersManager() {
	}

	public static PlayersManager instance() {
		return _instance;
	}

	public void updatePlayerArchive(PlayerArchive playerArchive) throws SQLException {
		try {
			SQLSession.connect();
			new PlayersArchiveDao(SQLSession.getConnection()).updatePlayerArchive(playerArchive);
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLSession.close();
		}
	}

	public void addPlayerArchive(PlayerArchive playerArchive) throws SQLException {
		PlayersArchiveDao playersArchiveDao = new PlayersArchiveDao(SQLSession.getConnection());

		if (playersArchiveDao.getPlayerArchive(playerArchive.getId()) == null) {
			playersArchiveDao.addPlayer(playerArchive);
		} else {
			playersArchiveDao.updatePlayerArchive(playerArchive);
		}
	}

	public void importPlayers(List<Player> players, Training training) throws SQLException {
		PlayersDao playersDao = new PlayersDao(SQLSession.getConnection());
		for (Player player : players) {

			if (!playersDao.existsPlayer(player.getId())) {
				playersDao.addPlayer(player);
				player.getSkills()[0].setPassTraining(true);
				playersDao.addPlayerSkills(player.getId(), player.getSkills()[0], training.getDate(), training.getId());
				if (player.getNtSkills() != null && player.getNtSkills().length > 0) {
					playersDao.addNtPlayerSkills(player.getId(), player.getNtSkills()[0], training.getDate());
				}
				player.setPositionTable(this.calculatePosition(player, AssistantDao.getAssistantData()));
				player.setPosition(player.getBestPosition());
				this.updatePlayersPositions(player);

			} else {

				if (playersDao.existsPlayerHistory(player.getId())) {
					playersDao.movePlayer(player.getId(), Player.STATUS_INCLUB);
				}

				if ((training.getStatus() & Training.NEW_TRAINING) != 0) {
					playersDao.addPlayerSkills(player.getId(), player.getSkills()[0], training.getDate(), training.getId());
				} else if ((training.getStatus() & Training.UPDATE_PLAYERS) != 0) {
					playersDao.updatePlayerSkills(player.getId(), player.getSkills()[0], training);
					playersDao.updatePlayer(player);
				}

			}
		}
		// FIXME instead of creating list of player in club get clubs and remove
		// all (do everything on collections)

		// sTemp = "(";
		// for (int i = 0; i < players.size(); i++) {
		// // warunek dla ostatniego stringa zeby nie dodawac na koncu ','
		// if (i == players.size() - 1) {
		// sTemp += players.get(i).getId();
		// break;
		// }
		// sTemp += players.get(i).getId() + ",";
		// }
		//		
		// sTemp += ")";
		// if (players.size() > 0) {
		// SQLQuery.removeSoldPlayer(sTemp);
		// } else {
		// SQLQuery.removeSoldPlayer();
		// }

	}

	public void addPlayers(List<Player> players, Training training) throws SQLException {
		PlayersDao playersDao = new PlayersDao(SQLSession.getConnection());
		String sTemp;
		for (Player player : players) {

			if (!playersDao.existsPlayer(player.getId())) {
				playersDao.addPlayer(player);
				player.getSkills()[0].setPassTraining(true);
				playersDao.addPlayerSkills(player.getId(), player.getSkills()[0], training.getDate(), training.getId());
				if (player.getNtSkills() != null && player.getNtSkills().length > 0) {
					playersDao.addNtPlayerSkills(player.getId(), player.getNtSkills()[0], training.getDate());
				}
				player.setPositionTable(this.calculatePosition(player, AssistantDao.getAssistantData()));
				player.setPosition(player.getBestPosition());
				this.updatePlayersPositions(player);

			} else {

				if (playersDao.existsPlayerHistory(player.getId())) {
					playersDao.movePlayer(player.getId(), Player.STATUS_INCLUB);
				}

				if ((training.getStatus() & Training.NEW_TRAINING) != 0 || playersDao.getPlayerSkills(player, training) == null) {
					playersDao.addPlayerSkills(player.getId(), player.getSkills()[0], training.getDate(), training.getId());
				} else {
					playersDao.updatePlayerSkills(player.getId(), player.getSkills()[0], training.getDate());
				}

				playersDao.updatePlayer(player);

				if (player.getNtSkills() != null && player.getNtSkills().length > 0
					&& playersDao.getNumberOfNtMatch(player.getId()) < player.getNtSkills()[0].getNtMatches()) {
					playersDao.addNtPlayerSkills(player.getId(), player.getNtSkills()[0], training.getDate());
				}
			}

		}
		// FIXME instead of creating list of player in club get clubs and remove
		// all (do everything on collections)

		sTemp = "("; //$NON-NLS-1$
		for (int i = 0; i < players.size(); i++) {
			// warunek dla ostatniego stringa zeby nie dodawac na koncu ','
			if (i == players.size() - 1) {
				sTemp += players.get(i).getId();
				break;
			}
			sTemp += players.get(i).getId() + ","; //$NON-NLS-1$
		}

		sTemp += ")"; //$NON-NLS-1$
		if (players.size() > 0) {
			playersDao.removeSoldPlayer(sTemp);
		} else {
			playersDao.removeSoldPlayer();
		}

	}

	public void importPlayer(PlayerInterface player) throws SQLException {
		try {

			PlayerSkills[] skills = player.getSkills();
			Calendar cal = Calendar.getInstance();
			SQLSession.connect();
			PlayersDao playersDao = new PlayersDao(SQLSession.getConnection());
			ArrayList<Long> alMillis = playersDao.getTrainingDates(player.getId());

			for (int i = 0; i < skills.length; i++) {
				cal.setTimeInMillis(skills[i].getDate().getMillis());

				Date date = new Date(cal.getTimeInMillis());

				int day = date.thursdayFirstDayOfWeek();

				Date date1 = new Date(cal.getTimeInMillis() - day * (DateConst.DAY));
				date1.getCalendar().set(Calendar.HOUR_OF_DAY, 0);
				date1.getCalendar().set(Calendar.MINUTE, 0);
				date1.getCalendar().set(Calendar.SECOND, 0);
				date1.getCalendar().set(Calendar.MILLISECOND, 0);
				Date date2 = new Date(cal.getTimeInMillis() + (7 - day) * (DateConst.DAY));
				date2.getCalendar().set(Calendar.HOUR_OF_DAY, 0);
				date2.getCalendar().set(Calendar.MINUTE, 0);
				date2.getCalendar().set(Calendar.SECOND, 0);
				date2.getCalendar().set(Calendar.MILLISECOND, 0);

				Long lDate;
				if (alMillis.size() > 0) {
					boolean found = false;
					for (Iterator<Long> itr = alMillis.iterator(); itr.hasNext();) {
						lDate = itr.next();
						if (lDate >= date1.getMillis() && lDate < date2.getMillis()) {
							if (lDate < cal.getTimeInMillis()) {
								playersDao.updatePlayerSkills(player.getId(), skills[i], date);
							}
							itr.remove();
							// alMillis.remove(lDate);
							found = true;
						}
					}

					if (!found) {
						playersDao.addPlayerSkills(player.getId(), skills[i], date);
					}
				}
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			SQLSession.close();
		}

	}

	public ArrayList<Player> getPlayers(int status, Club team, HashMap<Integer, Junior> juniorTrainedMap, HashMap<Integer, Training> trainingMap,
		HashMap<Integer, Transfer> transfersSellMap, HashMap<Integer, Transfer> transfersBuyMap) throws SQLException {
		ArrayList<Player> players;
		PlayerSkills[] skills;
		NtSkills[] ntSkills;
		boolean newConnection = SQLQuery.connect();
		// pobieranie graczy
		PlayersDao playersDao = new PlayersDao(SQLSession.getConnection());

		players = playersDao.getPlayers(status, juniorTrainedMap);

		for (Player player : players) {

			player.setTeam(team);

			if (transfersBuyMap.get(player.getId()) != null) {
				player.setTransferBuy(transfersBuyMap.get(player.getId()));
				transfersBuyMap.get(player.getId()).setPlayer(player);
			}
			if (transfersSellMap.get(player.getId()) != null) {
				player.setTransferSell(transfersSellMap.get(player.getId()));
				transfersSellMap.get(player.getId()).setPlayer(player);
			}

			skills = playersDao.getPlayerSkills(player, trainingMap);
			ntSkills = playersDao.getPlayerNtSkills(player);
			player.setNtSkills(ntSkills);

			Junior junior = juniorTrainedMap.get(player.getIdJuniorFK());
			player.setJunior(junior);
			if (junior != null) {
				junior.setPlayer(player);
			}

			player.setSkills(skills);
		}

		SQLSession.close(newConnection);
		return players;

	}

	public ArrayList<Player> getPlayers(Club team, HashMap<Integer, Junior> juniorTrainedMap, HashMap<Integer, Training> trainingMap,
		HashMap<Integer, Transfer> transfersSellMap, HashMap<Integer, Transfer> transfersBuyMap) throws SQLException {
		return getPlayers(Player.STATUS_INCLUB, team, juniorTrainedMap, trainingMap, transfersSellMap, transfersBuyMap);
	}

	public HashMap<Integer, PlayerArchive> getPlayersArchive() throws SQLException {
		HashMap<Integer, PlayerArchive> players;
		boolean newConnection = SQLQuery.connect();
		PlayersArchiveDao playersArchiveDao = new PlayersArchiveDao(SQLSession.getConnection());
		players = playersArchiveDao.getPlayers();
		SQLSession.close(newConnection);
		return players;
	}

	public String getPlayerArchiveNote(int playerID) throws SQLException {
		PlayersArchiveDao playersArchiveDao = new PlayersArchiveDao(SQLSession.getConnection());
		String note = playersArchiveDao.getPlayerArchiveNote(playerID);
		return note;
	}

	public ArrayList<Player> getPlayersFromTrashData(Club team, HashMap<Integer, Junior> juniorTrainedMap, HashMap<Integer, Training> trainingMap,
		HashMap<Integer, Transfer> transfersSellMap, HashMap<Integer, Transfer> transfersBuyMap) throws SQLException {
		return getPlayers(Player.STATUS_TRASH, team, juniorTrainedMap, trainingMap, transfersSellMap, transfersBuyMap);
	}

	public ArrayList<Player> getPlayersHistoryData(Club team, HashMap<Integer, Junior> juniorTrainedMap, HashMap<Integer, Training> trainingMap,
		HashMap<Integer, Transfer> transfersSellMap, HashMap<Integer, Transfer> transfersBuyMap) throws SQLException {
		return getPlayers(Player.STATUS_HISTORY, team, juniorTrainedMap, trainingMap, transfersSellMap, transfersBuyMap);
	}

	public void updatePlayerStatsInjury(PlayerStats stats) throws SQLException {
		try {
			SQLSession.connect();
			new PlayersDao(SQLSession.getConnection()).updatePlayerStatsInjuryDays(stats);
		} finally {
			SQLSession.close();
		}
	}

	public void changePlayerPassTraining(PlayerSkills playerSkills) throws SQLException {
		try {
			SQLSession.connect();
			playerSkills.setPassTraining(!playerSkills.isPassTraining());
			new PlayersDao(SQLSession.getConnection()).updatePlayerPassTraining(playerSkills);
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLSession.close();
		}
	}

	public void updatePlayersBuyPrice(Player player) throws SQLException {
		try {
			SQLSession.connect();
			new PlayersDao(SQLSession.getConnection()).updatePlayerBuyPrice(player);
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLSession.close();
		}
	}

	public void updatePlayersPositions(ArrayList<Player> players) throws SQLException {
		boolean newConnection = false;
		try {
			if (SQLSession.getConnection().isClosed()) {
				SQLSession.connect();
				newConnection = true;
			}

			for (Player player : players) {
				new PlayersDao(SQLSession.getConnection()).updatePlayerPosition(player);
			}
			if (newConnection) {
				SQLSession.close();
			}
		} catch (SQLException e) {
			SQLSession.close();
			throw e;
		}
	}

	public void updatePlayersPositions(Player player) throws SQLException {
		boolean connect = true;
		try {
			if (SQLSession.getConnection() != null) {
				if (SQLSession.getConnection().isClosed()) {
					SQLSession.connect();
					connect = false;
				}
			} else {
				SQLSession.connect();
			}

			new PlayersDao(SQLSession.getConnection()).updatePlayerPosition(player);
			if (!connect) {
				SQLSession.close();
			}

		} catch (SQLException e) {
			throw e;
		}
	}

	public void updatePlayersSoldPrice(Player player) throws SQLException {
		try {
			SQLSession.connect();
			new PlayersDao(SQLSession.getConnection()).updatePlayerSoldPrice(player);
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLSession.close();
		}
	}

	public void calculatePositionForAllPlayer(ArrayList<Player> players, int[][] data) {
		for (Player player : players) {
			player.setPositionTable(calculatePosition(player, data));
			player.setPosition(player.getBestPosition());
		}
	}

	public double[] calculatePosition(Player player, int[][] data) {
		double sum = 0.0;
		double[] table = new double[data.length];

		for (int j = 0; j < data.length; j++) {
			sum = 0.0;
			for (int i = 1; i < data[j].length; i++) {
				sum += data[j][i] * player.getSkills()[player.getSkills().length - 1].getStatsTable()[i + 2];
			}
			sum = sum / 100;
			table[j] = sum;
		}
		return table;
	}

	public void updateAssistantData(int[][] data) throws SQLException {
		try {
			SQLSession.connect();
			AssistantDao.updateAssistantData(data);
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLSession.close();
		}
	}

}
