package pl.pronux.sokker.actions;

import java.io.IOException;
import java.sql.SQLException;
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

public final class ConfigurationManager {

	private static ConfigurationManager instance = new ConfigurationManager();

	private ConfigurationManager() {
	}

	public static ConfigurationManager getInstance() {
		return instance;
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
	
	public void updateDbRepairJuniorsAge(boolean b) throws SQLException {
		DatabaseConfigurationDao dbConfDao = new DatabaseConfigurationDao(SQLSession.getConnection());
		dbConfDao.setDbRepairJuniorsAge(b);
	}

	public DbProperties getDbProperties() throws SQLException {
		DatabaseConfigurationDao dbConfDao = new DatabaseConfigurationDao(SQLSession.getConnection());
		return dbConfDao.getDbProperties();
	}

	public Date getMaxDate() throws SQLException {
		DatabaseConfigurationDao dbConfDao = new DatabaseConfigurationDao(SQLSession.getConnection());
		return dbConfDao.getMaxDate();
	}

	public void updateDbStructure(int dbVersion) throws SQLException, ClassNotFoundException, IOException {
		try {
			SQLSession.beginTransaction();
			DatabaseConfigurationDao databaseConfigurationDao = new DatabaseConfigurationDao(SQLSession.getConnection());
			while (databaseConfigurationDao.checkDBVersion() < dbVersion) {
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

	public void updateDbTeamId(int teamId) throws SQLException {
		DatabaseConfigurationDao dbConfDao = new DatabaseConfigurationDao(SQLSession.getConnection());
		dbConfDao.setDBTeamId(teamId);
	}

	public int getTeamId() throws SQLException {
		DatabaseConfigurationDao dbConfDao = new DatabaseConfigurationDao(SQLSession.getConnection());
		return dbConfDao.getTeamId();
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
		int teamId = getTeamId();
		TeamsDao teamsDao = new TeamsDao(SQLSession.getConnection());
		PlayersDao playersDao = new PlayersDao(SQLSession.getConnection());
		JuniorsDao juniorsDao = new JuniorsDao(SQLSession.getConnection());

		List<ClubArenaName> clubArenaNameList = teamsDao.getClubArenaName(teamId);
		List<ClubName> clubNameList = teamsDao.getClubName(teamId);
		List<Rank> rankList = teamsDao.getRank(teamId);
		Rank previousRank = null;
		for (Rank rank : rankList) {
			if (previousRank == null) {
				previousRank = rank;
			} else {
				if (rank.getRank() == previousRank.getRank()) {
					teamsDao.deleteRank(teamId, rank);
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
					teamsDao.deleteClubArenaName(teamId, clubArenaName);
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
					teamsDao.deleteClubName(teamId, clubName);
				} else {
					previousClubName = clubName;
				}
			}
		}

		List<Training> trainings = teamsDao.getTrainings();
		Map<Integer, Training> trainingsMap = new HashMap<Integer, Training>();
		for (Training training : trainings) {
			trainingsMap.put(training.getDate().getSokkerDate().getTrainingWeek(), training);
		}

		List<PlayerSkills> skills = playersDao.getPlayerSkillsWithoutTrainingId();
		for (PlayerSkills playerSkills : skills) {
			Training training = trainingsMap.get(playerSkills.getDate().getSokkerDate().getTrainingWeek());
			if (training != null) {
				playerSkills.setTrainingId(training.getId());
			} else {
				Training trainingDefault = new Training();
				trainingDefault.setFormation(Training.FORMATION_ALL);
				trainingDefault.setType(Training.TYPE_UNKNOWN);
				trainingDefault.setDate(playerSkills.getDate());

				teamsDao.addTraining(trainingDefault);
				int trainingId = teamsDao.getTrainingId(trainingDefault);
				if (trainingId > -1) {
					trainingDefault.setId(trainingId);
					playerSkills.setTrainingId(trainingId);
					trainingsMap.put(trainingDefault.getDate().getSokkerDate().getTrainingWeek(), trainingDefault);
				} else {
					throw new SQLException(Messages.getString("DatabaseConfiguration.exception.repairing.players")); 
				}

			}
			playersDao.updatePlayerTrainingId(playerSkills);
		}

		List<JuniorSkills> juniorsSkills = juniorsDao.getJuniorSkillsWithoutTrainingId();
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
				int trainingId = teamsDao.getTrainingId(trainingDefault);
				if (trainingId > -1) {
					trainingDefault.setId(trainingId);
					juniorSkills.setTraining(trainingDefault);
					trainingsMap.put(trainingDefault.getDate().getSokkerDate().getTrainingWeek(), trainingDefault);
				} else {
					throw new SQLException(Messages.getString("DatabaseConfiguration.exception.repairing.juniors")); 
				}

			}
			juniorsDao.updateJuniorTrainingId(juniorSkills);
		}

	}

}
