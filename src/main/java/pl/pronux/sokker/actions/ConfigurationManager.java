package pl.pronux.sokker.actions;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.pronux.sokker.data.sql.SQLQuery;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.data.sql.dao.DatabaseConfigurationDao;
import pl.pronux.sokker.data.sql.dao.JuniorsDao;
import pl.pronux.sokker.data.sql.dao.PlayersDao;
import pl.pronux.sokker.data.sql.dao.TeamsDao;
import pl.pronux.sokker.model.ClubArenaName;
import pl.pronux.sokker.model.ClubName;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.DbProperties;
import pl.pronux.sokker.model.JuniorSkills;
import pl.pronux.sokker.model.PlayerSkills;
import pl.pronux.sokker.model.Rank;
import pl.pronux.sokker.model.Training;
import pl.pronux.sokker.resources.Messages;

public class ConfigurationManager {
	
	private final static ConfigurationManager _instance = new ConfigurationManager();
	
	private ConfigurationManager() {
	}
	
	public static ConfigurationManager instance() {
		return _instance;
	}

	public void updateDbRepairCoaches(boolean b) throws SQLException {
		DatabaseConfigurationDao dbConfDao = new DatabaseConfigurationDao(SQLSession.getConnection());
		dbConfDao.setDbRepair(b);
	}

	public void updateDbCountry(boolean b) throws SQLException {
		DatabaseConfigurationDao dbConfDao = new DatabaseConfigurationDao(SQLSession.getConnection());
		dbConfDao.setDbCountry(b);
	}

	public void updateDbDate(Date date) throws SQLException {
		DatabaseConfigurationDao dbConfDao = new DatabaseConfigurationDao(SQLSession.getConnection());
		dbConfDao.setDbDate(date);
	}

	public void updateDbUpdate(boolean b) throws SQLException {
		DatabaseConfigurationDao dbConfDao = new DatabaseConfigurationDao(SQLSession.getConnection());
		dbConfDao.setDbUpdate(b);
	}

	public void updateDbVersion(int version) throws SQLException {
		DatabaseConfigurationDao dbConfDao = new DatabaseConfigurationDao(SQLSession.getConnection());
		dbConfDao.setDBVersion(version);
	}
	
	public DbProperties getDbProperties() throws SQLException {
		DatabaseConfigurationDao dbConfDao = new DatabaseConfigurationDao(SQLSession.getConnection());
		DbProperties dbProperties = dbConfDao.getDbProperties();
		return dbProperties;
	}

	public Date getMaxDate() throws SQLException {
		DatabaseConfigurationDao dbConfDao = new DatabaseConfigurationDao(SQLSession.getConnection());
		Date date = dbConfDao.getMaxDate();
		return date;
	}

	public void updateDbStructure(int db_version) throws SQLException, ClassNotFoundException, IOException {
		try {
			SQLSession.beginTransaction();
			DatabaseConfigurationDao databaseConfigurationDao = new DatabaseConfigurationDao(SQLSession.getConnection());
			while (databaseConfigurationDao.checkDBVersion() < db_version) {
				SQLQuery.updateDB(SQLSession.getConnection(), databaseConfigurationDao.checkDBVersion() + 1);
			}
			SQLSession.commit();
		} catch (SQLException e) {
			SQLSession.rollback();
			throw e;
		} finally {
			SQLSession.endTransaction();
		}
	}

	public void updateDbTeamID(int teamID) throws SQLException {
		DatabaseConfigurationDao dbConfDao = new DatabaseConfigurationDao(SQLSession.getConnection());
		dbConfDao.setDBTeamID(teamID);
	}

	public int getTeamID() throws SQLException {
		DatabaseConfigurationDao dbConfDao = new DatabaseConfigurationDao(SQLSession.getConnection());
		return dbConfDao.getTeamID();
	}

	public void setJuniorMinimumPop(double pop) throws SQLException {
		try {
			SQLSession.connect();
			new DatabaseConfigurationDao(SQLSession.getConnection()).setJuniorMinimumPop(pop);
		} finally {
			SQLSession.close();
		}
	}

	public void updateScanCounter(int scanCounter) throws SQLException {
		DatabaseConfigurationDao dbConfDao = new DatabaseConfigurationDao(SQLSession.getConnection());
		dbConfDao.updateScanCounter(scanCounter);
	}

	public double getJuniorMinimumPop() throws SQLException {
		DatabaseConfigurationDao dbConfDao = new DatabaseConfigurationDao(SQLSession.getConnection());
		return dbConfDao.getJuniorMinimumPop();
	}

	public void repairDatabase() throws SQLException {
		int teamID = getTeamID();
		TeamsDao teamsDao = new TeamsDao(SQLSession.getConnection());
		PlayersDao playersDao = new PlayersDao(SQLSession.getConnection());
		JuniorsDao juniorsDao = new JuniorsDao(SQLSession.getConnection());

		ArrayList<ClubArenaName> clubArenaNameList = teamsDao.getClubArenaName(teamID);
		ArrayList<ClubName> clubNameList = teamsDao.getClubName(teamID);
		ArrayList<Rank> rankList = teamsDao.getRank(teamID);
		Rank previousRank = null;
		for (Rank rank : rankList) {
			if (previousRank == null) {
				previousRank = rank;
			} else {
				if (rank.getRank() == previousRank.getRank()) {
					teamsDao.deleteRank(teamID, rank);
				} else {
					previousRank = rank;
				}
			}
		}
		ClubArenaName previousClubArenaName = null;
		for (ClubArenaName clubArenaName : clubArenaNameList) {
			if (previousClubArenaName == null) {
				previousClubArenaName = clubArenaName;
			} else {
				if (clubArenaName.getArenaName().equals(previousClubArenaName.getArenaName())) {
					teamsDao.deleteClubArenaName(teamID, clubArenaName);
				} else {
					previousClubArenaName = clubArenaName;
				}
			}
		}
		ClubName previousClubName = null;
		for (ClubName clubName : clubNameList) {
			if (previousClubName == null) {
				previousClubName = clubName;
			} else {
				if (clubName.getName().equals(previousClubName.getName())) {
					teamsDao.deleteClubName(teamID, clubName);
				} else {
					previousClubName = clubName;
				}
			}
		}

		ArrayList<Training> trainings = teamsDao.getTrainings();
		Map<Integer, Training> trainingsMap = new HashMap<Integer, Training>();
		for (Training training : trainings) {
			trainingsMap.put(training.getDate().getSokkerDate().getTrainingWeek(), training);
		}

		ArrayList<PlayerSkills> skills = playersDao.getPlayerSkillsWithoutTrainingID();
		for (PlayerSkills playerSkills : skills) {
			Training training = trainingsMap.get(playerSkills.getDate().getSokkerDate().getTrainingWeek());
			if (training != null) {
				playerSkills.setTrainingID(training.getId());
			} else {
				Training trainingDefault = new Training();
				trainingDefault.setFormation(Training.FORMATION_ALL);
				trainingDefault.setType(Training.TYPE_UNKNOWN);
				trainingDefault.setDate(playerSkills.getDate());

				teamsDao.addTraining(trainingDefault);
				int trainingID = teamsDao.getTrainingId(trainingDefault);
				if (trainingID > -1) {
					trainingDefault.setId(trainingID);
					playerSkills.setTrainingID(trainingID);
					trainingsMap.put(trainingDefault.getDate().getSokkerDate().getTrainingWeek(), trainingDefault);
				} else {
					throw new SQLException(Messages.getString("DatabaseConfiguration.exception.repairing.players")); //$NON-NLS-1$
				}

			}
			playersDao.updatePlayerTrainingID(playerSkills);
		}

		List<JuniorSkills> juniorsSkills = juniorsDao.getJuniorSkillsWithoutTrainingID();
		for (JuniorSkills juniorSkills : juniorsSkills) {
			Training training = trainingsMap.get(juniorSkills.getDate().getSokkerDate().getTrainingWeek());
			if (training != null) {
				juniorSkills.setTraining(training);
			} else {
				Training trainingDefault = new Training();
				trainingDefault.setFormation(Training.FORMATION_ALL);
				trainingDefault.setType(Training.TYPE_UNKNOWN);
				trainingDefault.setDate(juniorSkills.getDate());

				teamsDao.addTraining(trainingDefault);
				int trainingID = teamsDao.getTrainingId(trainingDefault);
				if (trainingID > -1) {
					trainingDefault.setId(trainingID);
					juniorSkills.setTraining(trainingDefault);
					trainingsMap.put(trainingDefault.getDate().getSokkerDate().getTrainingWeek(), trainingDefault);
				} else {
					throw new SQLException(Messages.getString("DatabaseConfiguration.exception.repairing.juniors")); //$NON-NLS-1$
				}

			}
			juniorsDao.updateJuniorTrainingID(juniorSkills);
		}

	}

}
