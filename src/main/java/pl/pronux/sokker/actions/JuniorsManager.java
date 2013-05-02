package pl.pronux.sokker.actions;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.data.sql.dao.JuniorsDao;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.JuniorSkills;
import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.model.Training;

public class JuniorsManager {

	private static JuniorsManager instance = new JuniorsManager();

	private JuniorsManager() {
	}

	public static JuniorsManager instance() {
		return instance;
	}

	public void addJuniors(List<Junior> juniors, Training training, int clubId) throws SQLException {
		JuniorsDao juniorsDao = new JuniorsDao(SQLSession.getConnection());
		String sTemp;
		for (Junior junior : juniors) {
			if (!juniorsDao.existsJunior(junior.getId())) {
				juniorsDao.addJunior(junior);
				juniorsDao.addJuniorSkills(junior.getId(), junior.getSkills()[0], training);
			} else {
				if (juniorsDao.existsJuniorHistory(junior.getId())) {
					juniorsDao.moveJunior(junior.getId(), Junior.STATUS_IN_SCHOOL, clubId);
				}
				if ((training.getStatus() & Training.NEW_TRAINING) != 0) {
					juniorsDao.addJuniorSkills(junior.getId(), junior.getSkills()[0], training);
				} else {
					juniorsDao.updateJuniorSkills(junior.getId(), junior.getSkills()[0], training.getDate());
				}
				juniorsDao.updateJunior(junior);
			}
		}

		sTemp = "("; //$NON-NLS-1$

		for (int i = 0; i < juniors.size(); i++) {
			// warunek dla ostatniego stringa zeby nie dodawac na koncu ','
			if (i == juniors.size() - 1) {
				sTemp += juniors.get(i).getId();
				break;
			}
			sTemp += juniors.get(i).getId() + ","; //$NON-NLS-1$
		}
		sTemp += ")"; //$NON-NLS-1$

		if (juniors.size() > 0) {
			juniorsDao.moveTrainedJuniors(sTemp, clubId);
			juniorsDao.removeTrainedJuniors(sTemp, clubId);
		} else {
			juniorsDao.moveTrainedJuniors(clubId);
			juniorsDao.removeTrainedJuniors(clubId);
		}
	}

	public void importJuniors(List<Junior> juniors, Training training, int clubId) throws SQLException {
		JuniorsDao juniorsDao = new JuniorsDao(SQLSession.getConnection());

		for (Junior junior : juniors) {

			if (!juniorsDao.existsJunior(junior.getId())) {
				juniorsDao.addJunior(junior);
				juniorsDao.addJuniorSkills(junior.getId(), junior.getSkills()[0], training);
			} else {
				if (juniorsDao.existsJuniorHistory(junior.getId())) {
					juniorsDao.moveJunior(junior.getId(), Junior.STATUS_IN_SCHOOL, clubId);
				}
				if ((training.getStatus() & Training.NEW_TRAINING) != 0 || juniorsDao.getJuniorSkills(junior, training) == null) {
					juniorsDao.addJuniorSkills(junior.getId(), junior.getSkills()[0], training);
				}
			}
		}
	}

	public void completeJuniorsAge(Date currentDay) throws SQLException {
		JuniorsDao juniorsDao = new JuniorsDao(SQLSession.getConnection());
		List<Junior> juniors = juniorsDao.getJuniors(Junior.STATUS_IN_SCHOOL);
		for (Junior junior : juniors) {
			JuniorSkills[] juniorSkills = juniorsDao.getJuniorsSkills(junior, new HashMap<Integer, Training>());
			if (juniorSkills[0].getAge() == 0 && juniorSkills[juniorSkills.length - 1].getAge() != 0) {
				int age = juniorSkills[juniorSkills.length - 1].getAge();
				for (JuniorSkills skills : juniorSkills) {
					if (skills.getAge() == 0) {
						skills.setAge(getJuniorAge(currentDay, skills.getDate(), age));
						juniorsDao.updateJuniorSkills(skills);
					}
				}
			}
		}
	}

	private int getJuniorAge(Date currentDay, Date oldDate, int age) {
		int w1 = oldDate.getSokkerDate().getSeason();
		if (oldDate.getSokkerDate().getSeasonWeek() == 15 && oldDate.getSokkerDate().getDay() == SokkerDate.FRIDAY) {
			w1 += 1;
		}
		int w2 = currentDay.getSokkerDate().getSeason();
		return age - (w2 - w1);
	}
}