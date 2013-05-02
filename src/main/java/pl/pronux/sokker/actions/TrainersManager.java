package pl.pronux.sokker.actions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.pronux.sokker.data.sql.SQLQuery;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.data.sql.dao.TrainersDao;
import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.Training;

public class TrainersManager {
	
	private static final int JUNIOR_COACH = 3;
	private static final int ASSISTANT = 2;
	private static final int HEAD_COACH = 1;
	
	private static TrainersManager instance = new TrainersManager();
	
	private TrainersManager(){
	}
	
	public static TrainersManager instance() {
		return instance;
	}
	
	public void repairCoaches(List<Coach> coaches) throws SQLException {
		TrainersDao trainersDao = new TrainersDao(SQLSession.getConnection());
		for (Coach coach : coaches) {
			trainersDao.repairCoach(coach);
		}
	}

	public void updateTrainersAtTraining(List<Coach> trainers, Training training) throws SQLException {
		TrainersDao trainersDao = new TrainersDao(SQLSession.getConnection());
		trainersDao.deleteCoachesAtTraining(training);

		List<Coach> alAssistants = new ArrayList<Coach>();
		training.setAssistants(alAssistants);

		for (Coach trainer : trainers) {
			if (trainer.getJob() == HEAD_COACH) {
				training.setHeadCoach(trainer);
			} else if (trainer.getJob() == ASSISTANT) {
				training.getAssistants().add(trainer);
			} else if (trainer.getJob() == JUNIOR_COACH) {
				training.setJuniorCoach(trainer);
			}
		}

		trainersDao.addCoachesAtTraining(training);
	}
	
	
	public void importTrainersAtTraining(List<Coach> alTrainers, Training training) throws SQLException {

		List<Coach> alAssistants = new ArrayList<Coach>();
		training.setAssistants(alAssistants);

		for (Coach coach : alTrainers) {
			if (coach.getJob() == HEAD_COACH) {
				training.setHeadCoach(coach);
			} else if (coach.getJob() == ASSISTANT) {
				training.getAssistants().add(coach);
			} else if (coach.getJob() == HEAD_COACH) {
				training.setJuniorCoach(coach);
			}
		}

		new TrainersDao(SQLSession.getConnection()).addCoachesAtTraining(training);
	}
	
	public void importTrainers(List<Coach> trainers) throws SQLException {
		TrainersDao trainersDao = new TrainersDao(SQLSession.getConnection());
		StringBuilder sb = new StringBuilder("("); //$NON-NLS-1$
		for (int i = 0; i < trainers.size(); i++) {
			// warunek dla ostatniego stringa zeby nie dodawac na koncu ','
			if (i == trainers.size() - 1) {
				sb.append(trainers.get(i).getId());
				break;
			}
			sb.append(trainers.get(i).getId()).append(","); //$NON-NLS-1$
		}
		sb.append(")"); //$NON-NLS-1$
		if (trainers.size() > 0) {
			trainersDao.removeCoaches(sb.toString());
		} else {
			trainersDao.removeCoaches();
		}

		// pobieramy dane wpisywane do bazy danych
		for (Coach coach : trainers) {
			// wrzucamy dane do bazy danych
			// wstawiane dane przeparsowac a najlepiej przepisac do obiektu
			// player
			if (!trainersDao.existsCoach(coach.getId())) {

				trainersDao.addCoach(coach);

			} else {
				if (trainersDao.existsCoachHistory(coach.getId())) {
					trainersDao.moveCoach(coach.getId(), 0);
				}
				trainersDao.updateCoach(coach);
			}
		}

	}

	public void importerTrainers(List<Coach> trainers) throws SQLException {
		TrainersDao trainersDao = new TrainersDao(SQLSession.getConnection());
		StringBuilder sb = new StringBuilder("("); //$NON-NLS-1$
		for (int i = 0; i < trainers.size(); i++) {
			// warunek dla ostatniego stringa zeby nie dodawac na koncu ','
			if (i == trainers.size() - 1) {
				sb.append(trainers.get(i).getId());
				break;
			}
			sb.append(trainers.get(i).getId()).append(","); //$NON-NLS-1$
		}
		sb.append(")"); //$NON-NLS-1$
		if (trainers.size() > 0) {
			trainersDao.removeCoaches(sb.toString());
		} else {
			trainersDao.removeCoaches();
		}
		
		List<Coach> trainersDB = trainersDao.getCoaches(Coach.STATUS_IN_CLUB);
		Map<Integer, Coach> trainersMap = new HashMap<Integer, Coach>();
		for (Coach coach : trainersDB) {
			trainersMap.put(coach.getId(), coach);
		}

		// pobieramy dane wpisywane do bazy danych
		for (Coach coach : trainers) {
			// wrzucamy dane do bazy danych
			// wstawiane dane przeparsowac a najlepiej przepisac do obiektu
			// player
			if (!trainersDao.existsCoach(coach.getId())) {

				trainersDao.addCoach(coach);

			} else {
				if (trainersDao.existsCoachHistory(coach.getId())) {
					trainersDao.moveCoach(coach.getId(), 0);
				}
				if(trainersMap.get(coach.getId()) == null || trainersMap.get(coach.getId()).getAge() < coach.getAge() ) {
					trainersDao.updateCoach(coach);	
				}
			}
		}

	}

	
	public List<Coach> getCoachesData() throws SQLException {
		boolean newConnection = SQLQuery.connect();
		List<Coach> coach = new TrainersDao(SQLSession.getConnection()).getCoaches(Coach.STATUS_IN_CLUB);
		SQLQuery.close(newConnection);
		return coach;
	}

	public List<Coach> getCoachesFiredData() throws SQLException {
		boolean newConnection = SQLQuery.connect();
		List<Coach> coach = new TrainersDao(SQLSession.getConnection()).getCoaches(Coach.STATUS_SACKED);
		SQLQuery.close(newConnection);
		return coach;
	}

	public List<Coach> getCoachesDeletedData() throws SQLException {
		boolean newConnection = SQLQuery.connect();
		List<Coach> coach = new TrainersDao(SQLSession.getConnection()).getCoaches(Coach.STATUS_DELETED);
		SQLQuery.close(newConnection);
		return coach;
	}

	public List<Coach> getCoachesFromTrashData() throws SQLException {
		boolean newConnection = SQLQuery.connect();
		List<Coach> coaches = new TrainersDao(SQLSession.getConnection()).getCoaches(Coach.STATUS_TRASH);
		SQLQuery.close(newConnection);
		return coaches;
	}
	
}
