package pl.pronux.sokker.actions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.pronux.sokker.data.sql.SQLQuery;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.data.sql.dao.CountriesDao;
import pl.pronux.sokker.data.sql.dao.JuniorsDao;
import pl.pronux.sokker.data.sql.dao.ReportsDao;
import pl.pronux.sokker.data.sql.dao.TeamsDao;
import pl.pronux.sokker.data.sql.dao.TrainersDao;
import pl.pronux.sokker.data.sql.dao.TransfersDao;
import pl.pronux.sokker.model.Arena;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.ClubArenaName;
import pl.pronux.sokker.model.ClubBudget;
import pl.pronux.sokker.model.ClubName;
import pl.pronux.sokker.model.ClubSupporters;
import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.JuniorSkills;
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.PlayerArchive;
import pl.pronux.sokker.model.Rank;
import pl.pronux.sokker.model.Region;
import pl.pronux.sokker.model.Report;
import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.model.Stand;
import pl.pronux.sokker.model.Training;
import pl.pronux.sokker.model.Transfer;
import pl.pronux.sokker.resources.Messages;

public final class TeamManager {

	private static TeamManager instance = new TeamManager();

	private TeamManager() {
	}

	public static TeamManager getInstance() {
		return instance;
	}

	public void importReports(List<Report> reports) throws SQLException {
		ReportsDao reportsDao = new ReportsDao(SQLSession.getConnection());
		for (Report report : reports) {
			if (!reportsDao.existsReport(report.getReportId())) {
				reportsDao.addReport(report);
			} else {
				break;
			}
		}
	}

	public void importerReports(List<Report> reports) throws SQLException {
		ReportsDao reportsDao = new ReportsDao(SQLSession.getConnection());
		for (Report report : reports) {
			if (!reportsDao.existsReport(report.getReportId())) {
				reportsDao.addReport(report);
			}
		}
	}

	public void importTransfers(List<Transfer> transfers) throws SQLException {
		TransfersDao transfersDao = new TransfersDao(SQLSession.getConnection());
		for (Transfer transfer : transfers) {
			if (!transfersDao.existsTransfer(transfer.getTransferId())) {
				transfersDao.addTransfer(transfer);
			} else {
				break;
			}
		}
	}

	public void importerTransfers(List<Transfer> transfers) throws SQLException {
		TransfersDao transfersDao = new TransfersDao(SQLSession.getConnection());
		for (Transfer transfer : transfers) {
			if (!transfersDao.existsTransfer(transfer.getTransferId())) {
				transfersDao.addTransfer(transfer);
			}
		}
	}

	public List<Integer> getNotImportedTeams() throws SQLException {
		TeamsDao teamsDao = new TeamsDao(SQLSession.getConnection());
		return teamsDao.getNotImportedClubsId();
	}

	public void importTraining(Training training) throws SQLException {
		boolean isTraining = true;
		TeamsDao teamsDao = new TeamsDao(SQLSession.getConnection());
		Training lastTraining = teamsDao.getLastTraining();

		if (lastTraining != null) {
			isTraining = checkIsTraining(lastTraining.getDate().getSokkerDate(), training.getDate().getSokkerDate());
		}

		if (isTraining) {
			teamsDao.addTraining(training);
			training.setId(teamsDao.getTrainingId(training));
			training.setStatus(Training.NEW_TRAINING);
		} else {
			training.setId(lastTraining.getId());
			if (lastTraining.getType() == Training.TYPE_UNKNOWN) {
				updateTraining(training);
				training.setStatus(Training.UPDATE_TRAINING);
			} else {
				training.setStatus(Training.NO_TRAINING);
				training.setType(lastTraining.getType());
				training.setFormation(lastTraining.getFormation());
			}
		}
	}

	public void importerTraining(Training training) throws SQLException {
		TeamsDao teamsDao = new TeamsDao(SQLSession.getConnection());
		Training trainingDB = teamsDao.getTrainingForDay(training.getDate());

		if (trainingDB == null) {
			teamsDao.addTraining(training);
			training.setId(teamsDao.getTrainingId(training));
			training.setStatus(Training.NEW_TRAINING);
		} else {
			training.setId(trainingDB.getId());
			if (trainingDB.getType() == Training.TYPE_UNKNOWN) {
				updateTraining(training);
				training.setStatus(Training.UPDATE_TRAINING);
			} else {
				training.setStatus(Training.NO_TRAINING);
				training.setType(trainingDB.getType());
				training.setFormation(trainingDB.getFormation());
			}
			if (checkIsTraining(trainingDB.getDate().getSokkerDate(), training.getDate().getSokkerDate())) {
				training.setStatus(training.getStatus() | Training.UPDATE_PLAYERS);
			}
		}
	}

	private boolean checkIsTraining(SokkerDate sokkerDateBefore, SokkerDate sokkerDateNow) {
		if (sokkerDateBefore == null || sokkerDateNow == null) {
			return false;
		}
		if ((sokkerDateNow.getWeek() == sokkerDateBefore.getWeek() && sokkerDateNow.getDay() >= SokkerDate.THURSDAY && sokkerDateBefore.getDay() < SokkerDate.THURSDAY)
			|| (sokkerDateNow.getWeek() - sokkerDateBefore.getWeek() == 1 && (sokkerDateBefore.getDay() < SokkerDate.THURSDAY || sokkerDateNow.getDay() >= SokkerDate.THURSDAY))
			|| (sokkerDateNow.getWeek() - sokkerDateBefore.getWeek() > 1)) {
			return true;
		}
		return false;
	}

	public void importForeignClub(Club club, Date currentDay) throws SQLException {
		TeamsDao teamsDao = new TeamsDao(SQLSession.getConnection());

		if (!teamsDao.existsClub(club.getId())) {
			teamsDao.addClub(club);
			teamsDao.addClubName(club.getId(), club.getClubName().get(0), currentDay);
			teamsDao.addClubArenaName(club.getId(), club.getArena().getArenaNames().get(0), currentDay);
			teamsDao.addRank(club.getId(), club.getRank().get(0), currentDay);
		} else {

			teamsDao.updateClubName(club, currentDay);
			teamsDao.updateClubArenaName(club, currentDay);

			if (!teamsDao.getLastClubRank(club.getId()).equals(club.getRank().get(0).getRank())) {
				teamsDao.addRank(club.getId(), club.getRank().get(0), currentDay);
			}
		}

		// check club date created

		boolean check = teamsDao.ifNotNullClubDateCreated();
		if (!check && club.getDateCreated() != null) {
			teamsDao.updateClubDateCreated(club);
		}

		Arena arena = club.getArena();

		if (arena != null) {
			List<Stand> stands = arena.getStands();
			// for (int i = Stand.N; i <= Stand.SE; i++) {
			// if (stands.get(i) == null) {
			// if (!teamsDao.existsStand(i, club.getId())) {
			// teamsDao.addStand(new Stand(i, 0, 100, 0, 0.0), currentDay, club.getId());
			// }
			// }
			// }

			for (Stand stand : stands) {
				if (stand.getConstructionDays() == null) {
					stand.setConstructionDays(0.0);
				}
				if (teamsDao.getStandChanges(stand, club.getId())) {
					teamsDao.updateStand(stand, currentDay, club.getId());
				}
			}
		}
	}

	public void importerTeam(Club club, Date currentDay) throws SQLException {
		TeamsDao teamsDao = new TeamsDao(SQLSession.getConnection());

		if (!teamsDao.existsClub(club.getId())) {
			teamsDao.addClub(club);
			teamsDao.addClubDataFanclub(club.getId(), club.getClubSupporters().get(0), currentDay);
			teamsDao.addClubDataMoney(club.getId(), club.getClubBudget().get(0), currentDay);
			teamsDao.addClubName(club.getId(), club.getClubName().get(0), currentDay);
			teamsDao.addClubArenaName(club.getId(), club.getArena().getArenaNames().get(0), currentDay);
			if (club.getRank().size() > 0) {
				teamsDao.addRank(club.getId(), club.getRank().get(0), currentDay);
			}
		} else {
			if (club.getRank().size() > 0) {
				Rank rank = teamsDao.getRankForDay(club.getId(), currentDay);
				if (rank == null) {
					teamsDao.addRank(club.getId(), club.getRank().get(0), currentDay);
				} else {
					teamsDao.updateRank(club.getRank().get(0), currentDay, rank.getId());
				}

			}

			// insert budget
			ClubBudget clubBudget = teamsDao.getBudgetForDay(club.getId(), currentDay);

			if (clubBudget == null) {
				teamsDao.addClubDataMoney(club.getId(), club.getClubBudget().get(0), currentDay);
			} else if ((currentDay.getSokkerDate().getWeek() > clubBudget.getDate().getSokkerDate().getWeek())
					   || (currentDay.getSokkerDate().getWeek() == clubBudget.getDate().getSokkerDate().getWeek() && currentDay.getSokkerDate().getDay() > clubBudget
						   .getDate()
						   .getSokkerDate()
						   .getDay())) {
				teamsDao.updateClubDataMoney(club.getId(), club.getClubBudget().get(0), clubBudget);
			}

			// insert fanclub
			ClubSupporters clubSupporters = teamsDao.getSupportersForDay(club.getId(), currentDay);
			if (clubSupporters == null) {
				teamsDao.addClubDataFanclub(club.getId(), club.getClubSupporters().get(0), currentDay);
			} else if ((currentDay.getSokkerDate().getWeek() > clubSupporters.getDate().getSokkerDate().getWeek())
					   || (currentDay.getSokkerDate().getWeek() == clubSupporters.getDate().getSokkerDate().getWeek() && currentDay.getSokkerDate().getDay() > clubSupporters
						   .getDate()
						   .getSokkerDate()
						   .getDay())) {
				teamsDao.updateClubDataFanclubForDay(club.getId(), club.getClubSupporters().get(0), clubSupporters);
			}
		}

		if (club.getTraining() != null) {
			club.getTraining().setDate(currentDay);
			importerTraining(club.getTraining());
			// club.getTraining().setId(trainingID);
		}

	}

	public void importTeam(Club club, Date currentDay) throws SQLException {
		TeamsDao teamsDao = new TeamsDao(SQLSession.getConnection());

		if (!teamsDao.existsClub(club.getId())) {
			teamsDao.addClub(club);
			teamsDao.addClubDataFanclub(club.getId(), club.getClubSupporters().get(0), currentDay);
			teamsDao.addClubDataMoney(club.getId(), club.getClubBudget().get(0), currentDay);
			teamsDao.addClubName(club.getId(), club.getClubName().get(0), currentDay);
			teamsDao.addClubArenaName(club.getId(), club.getArena().getArenaNames().get(0), currentDay);
			if (club.getRank().size() > 0) {
				teamsDao.addRank(club.getId(), club.getRank().get(0), currentDay);
			}
		} else {
			if (!teamsDao.getLastClubName(club.getId()).equals(club.getClubName().get(0).getName())) {
				teamsDao.addClubName(club.getId(), club.getClubName().get(0), currentDay);
			}
			if (!teamsDao.getLastClubArenaName(club.getId()).equals(club.getArena().getArenaNames().get(0).getArenaName())) {
				teamsDao.addClubArenaName(club.getId(), club.getArena().getArenaNames().get(0), currentDay);
			}

			if (club.getRank().size() > 0 && !teamsDao.getLastClubRank(club.getId()).equals(club.getRank().get(0).getRank())) {
				teamsDao.addRank(club.getId(), club.getRank().get(0), currentDay);
			}

			// insert budget
			SokkerDate lastMoney = teamsDao.getLastMoneySokkerDate();

			if (lastMoney != null) {
				if ((currentDay.getSokkerDate().getWeek() == lastMoney.getWeek() && currentDay.getSokkerDate().getDay() >= SokkerDate.SATURDAY && lastMoney
					.getDay() < SokkerDate.SATURDAY)
					|| (currentDay.getSokkerDate().getWeek() - lastMoney.getWeek() == 1 && (lastMoney.getDay() < SokkerDate.SATURDAY || currentDay
						.getSokkerDate()
						.getDay() >= SokkerDate.SATURDAY)) || (currentDay.getSokkerDate().getWeek() - lastMoney.getWeek() > 1)) {
					teamsDao.addClubDataMoney(club.getId(), club.getClubBudget().get(0), currentDay);
				} else {
					teamsDao.updateClubDataMoney(club.getId(), club.getClubBudget().get(0), currentDay);
				}
			} else {
				teamsDao.addClubDataMoney(club.getId(), club.getClubBudget().get(0), currentDay);
			}

			// check club date created

			boolean check = teamsDao.ifNotNullClubDateCreated();
			if (!check && club.getDateCreated() != null) {
				teamsDao.updateClubDateCreated(club);
			}

			// insert juniors

			teamsDao.updateJuniorsMax(club.getId(), club.getJuniorsMax());

			// insert fanclub
			SokkerDate lastFanclub = teamsDao.getLastFanclubSokkerDate();

			if (lastFanclub != null) {
				if ((currentDay.getSokkerDate().getWeek() == lastFanclub.getWeek() && currentDay.getSokkerDate().getDay() >= SokkerDate.MONDAY && lastFanclub
					.getDay() < SokkerDate.MONDAY)
					|| (currentDay.getSokkerDate().getWeek() - lastFanclub.getWeek() == 1 && (lastFanclub.getDay() < SokkerDate.MONDAY || currentDay
						.getSokkerDate()
						.getDay() >= SokkerDate.MONDAY)) || (currentDay.getSokkerDate().getWeek() - lastFanclub.getWeek() > 1)) {
					teamsDao.addClubDataFanclub(club.getId(), club.getClubSupporters().get(0), currentDay);
				} else {
					teamsDao.updateClubDataFanclub(club.getId(), club.getClubSupporters().get(0), currentDay);
				}
			} else {
				teamsDao.addClubDataFanclub(club.getId(), club.getClubSupporters().get(0), currentDay);
			}
		}

		Arena arena = club.getArena();

		if (arena != null) {
			List<Stand> stands = arena.getStands();

			if (stands.size() < 8) {
				Map<Integer, Stand> hmStands = new HashMap<Integer, Stand>();

				for (Stand stand : stands) {
					hmStands.put(stand.getLocation(), stand);
				}

				for (int i = 1; i < 9; i++) {
					if (hmStands.get(i) == null) {
						if (!teamsDao.existsStand(i, club.getId())) {
							teamsDao.addStand(new Stand(i, 0, 100, 0, 0.0), currentDay, club.getId());
						}
					}
				}
			}

			for (Stand stand : stands) {
				if (teamsDao.getStandChanges(stand, club.getId())) {
					teamsDao.addStand(stand, currentDay, club.getId());
				} else {
					teamsDao.updateStand(stand, currentDay, club.getId());
				}
			}

		}

		if (club.getTraining() != null) {
			club.getTraining().setDate(currentDay);
			importTraining(club.getTraining());
			// club.getTraining().setId(trainingID);
		}

	}

	public Map<Integer, Club> getTeams() throws SQLException {
		Map<Integer, Club> clubMap = new HashMap<Integer, Club>();
		boolean newConnection = SQLQuery.connect();
		// pobieranie informacji o klubie
		TeamsDao teamsDao = new TeamsDao(SQLSession.getConnection());
		CountriesDao countriesDao = new CountriesDao(SQLSession.getConnection());
		List<Club> clubs = teamsDao.getClubs();

		for (Club club : clubs) {
			List<ClubName> clubName = teamsDao.getClubName(club.getId());
			club.setClubName(clubName);

			List<Rank> rank = teamsDao.getRank(club.getId());
			club.setRank(rank);

			Region region = countriesDao.getRegion(club.getRegionId());
			club.setRegion(region);

			Arena arena = new Arena();
			List<Stand> stands = teamsDao.getStands(club.getId());
			arena.setStands(stands);

			List<ClubArenaName> clubArenaName = teamsDao.getClubArenaName(club.getId());
			arena.setArenaNames(clubArenaName);

			club.setArena(arena);
			clubMap.put(club.getId(), club);
		}

		SQLSession.close(newConnection);

		return clubMap;
	}

	public Club getTeam(int teamId) throws SQLException {
		
		boolean newConnection = SQLQuery.connect();
		// pobieranie informacji o klubie
		TeamsDao teamsDao = new TeamsDao(SQLSession.getConnection());
		CountriesDao countriesDao = new CountriesDao(SQLSession.getConnection());

		Club club = teamsDao.getClub(teamId);

		List<ClubBudget> clubDataMoney = teamsDao.getClubDataMoney(teamId);
		club.setClubBudget(clubDataMoney);

		List<ClubSupporters> clubDataFanclub = teamsDao.getClubDataFanclub(teamId);
		club.setClubSupporters(clubDataFanclub);

		List<ClubName> clubName = teamsDao.getClubName(teamId);
		club.setClubName(clubName);

		List<Rank> rank = teamsDao.getRank(teamId);
		club.setRank(rank);

		Region region = countriesDao.getRegion(club.getRegionId());
		club.setRegion(region);

		Arena arena = new Arena();
		List<Stand> stands = teamsDao.getStands(teamId);
		arena.setStands(stands);

		List<ClubArenaName> clubArenaName = teamsDao.getClubArenaName(teamId);
		arena.setArenaNames(clubArenaName);

		club.setArena(arena);

		club.setVisitedCountries(teamsDao.getVisitedCountries(teamId));

		club.setInvitedCountries(teamsDao.getInvitedCountries(teamId));

		SQLSession.close(newConnection);
		return club;
	}

	public void updateClubImagePath(Club club) throws SQLException {
		try {
			SQLSession.connect();
			new TeamsDao(SQLSession.getConnection()).updateClubImagePath(club);
		} finally {
			SQLSession.close();
		}
	}

	public List<Training> getTrainingData(Map<Integer, Coach> coachMap) throws SQLException {
		boolean newConnection = SQLQuery.connect();
		TeamsDao teamsDao = new TeamsDao(SQLSession.getConnection());
		TrainersDao trainersDao = new TrainersDao(SQLSession.getConnection());

		List<Training> trainings = teamsDao.getTrainings();

		for (Training training : trainings) {
			training.setJuniors(new ArrayList<Junior>());
			training.setPlayers(new ArrayList<Player>());
			trainersDao.getCoachesAtTraining(training, coachMap);
		}
		SQLSession.close(newConnection);
		return trainings;
	}

	public List<Report> getReports(Map<Integer, PlayerArchive> playersMap, Map<Integer, Coach> coachMap) throws SQLException {
		boolean newConnection = SQLQuery.connect();
		ReportsDao reportsDao = new ReportsDao(SQLSession.getConnection());

		List<Report> reports = reportsDao.getReports();

		for (Report report : reports) {
			if (report.getPersonId() > 0) {
				if (report.getType() == 216) {
					report.setPerson(coachMap.get(report.getPersonId()));
				} else if (report.getType() != 212) {
					report.setPerson(playersMap.get(report.getPersonId()));
				}
			}

			List<String> params = new ArrayList<String>();

			if ((report.getType() < 200 && report.getType() != 1 && report.getType() != 101) || report.getType() == 212) {
				params.add(new Money(report.getValue()).formatIntegerCurrencySymbol());
				if (report.getPersonId() > 0) {
					if (report.getPerson() != null) {
						params.add(report.getPerson().getName() + " " + report.getPerson().getSurname()); 
					} else {
						params.add(String.valueOf(report.getPersonId()));
					}
				}
			} else {
				if (report.getPersonId() > 0) {
					if (report.getPerson() != null) {
						params.add(report.getPerson().getName() + " " + report.getPerson().getSurname()); 
					} else {
						params.add(String.valueOf(report.getPersonId()));
					}
					params.add(new Money(report.getValue()).formatIntegerCurrencySymbol());
				} else {
					params.add(String.valueOf(report.getValue()));
				}

			}

			report.setMessage(String.format(Messages.getString("report." + report.getType()), params.toArray(new Object[params.size()])));

			if (report.getType() > 0 && report.getType() < 100) {
				report.setStatus(Report.INCOME);
			} else if (report.getType() >= 100 && report.getType() < 200) {
				report.setStatus(Report.COST);
			} else if (report.getType() >= 200) {
				report.setStatus(Report.INFO);
			}
		}

		reportsDao.checkReports();
		SQLSession.close(newConnection);
		return reports;
	}

	public void updateReportedTrainings(Training training) throws SQLException {
		try {
			SQLSession.connect();
			TeamsDao teamsDao = new TeamsDao(SQLSession.getConnection());
			teamsDao.updateReportedTrainings();
		} finally {
			SQLSession.close();
		}
	}
	
	public List<Junior> getJuniors(Map<Integer, Training> trainingMap) throws SQLException {
		boolean newConnection = SQLQuery.connect();
		JuniorsDao juniorsDao = new JuniorsDao(SQLSession.getConnection());
		List<Junior> juniors = juniorsDao.getJuniors(Junior.STATUS_IN_SCHOOL);

		for (Junior junior : juniors) {
			JuniorSkills[] juniorSkills = juniorsDao.getJuniorsSkills(junior, trainingMap);
			junior.setSkills(juniorSkills);
		}
		SQLQuery.close(newConnection);
		return juniors;
	}

	public List<Junior> getJuniorsFired(Map<Integer, Training> trainingMap) throws SQLException {
		boolean newConnection = SQLQuery.connect();
		JuniorsDao juniorsDao = new JuniorsDao(SQLSession.getConnection());
		List<Junior> juniorsFired = juniorsDao.getJuniors(Junior.STATUS_SACKED);

		for (Junior junior : juniorsFired) {
			JuniorSkills[] juniorSkills = juniorsDao.getJuniorsSkills(junior, trainingMap);
			junior.setSkills(juniorSkills);
		}

		SQLQuery.close(newConnection);
		return juniorsFired;
	}

	public List<Junior> getJuniorsFromTrash(Map<Integer, Training> trainingMap) throws SQLException {
		boolean newConnection = SQLQuery.connect();
		JuniorsDao juniorsDao = new JuniorsDao(SQLSession.getConnection());
		List<Junior> juniors = juniorsDao.getJuniorsFromTrash();

		for (Junior junior : juniors) {
			JuniorSkills[] juniorSkills = juniorsDao.getJuniorsSkills(junior, trainingMap);
			junior.setSkills(juniorSkills);
		}
		SQLQuery.close(newConnection);
		return juniors;
	}

	public List<Junior> getJuniorsTrained(Map<Integer, Training> trainingMap) throws SQLException {
		boolean newConnection = SQLQuery.connect();
		JuniorsDao juniorsDao = new JuniorsDao(SQLSession.getConnection());
		List<Junior> juniorsTrained = juniorsDao.getJuniors(Junior.STATUS_TRAINED);

		for (Junior junior : juniorsTrained) {
			JuniorSkills[] juniorSkills = juniorsDao.getJuniorsSkills(junior, trainingMap);
			junior.setSkills(juniorSkills);
		}

		SQLQuery.close(newConnection);
		return juniorsTrained;
	}

	public List<Transfer> getTransfers(Club club) throws SQLException {
		boolean newConnection = SQLQuery.connect();
		TransfersDao transfersDao = new TransfersDao(SQLSession.getConnection());
		List<Transfer> transfers = transfersDao.getTransfers(club);
		SQLQuery.close(newConnection);
		return transfers;
	}

	public void updateTraining(Training training) throws SQLException {
		boolean newConnection = SQLQuery.connect();
		TeamsDao teamsDao = new TeamsDao(SQLSession.getConnection());
		TrainersDao trainersDao = new TrainersDao(SQLSession.getConnection());

		teamsDao.updateTraining(training);
		trainersDao.deleteCoachesAtTraining(training);
		trainersDao.addCoachesAtTraining(training);
		SQLQuery.close(newConnection);
	}

}
