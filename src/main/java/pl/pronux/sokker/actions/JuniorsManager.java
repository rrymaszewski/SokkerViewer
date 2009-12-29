package pl.pronux.sokker.actions;

import java.sql.SQLException;
import java.util.List;

import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.data.sql.dao.JuniorsDao;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.Training;

public class JuniorsManager {
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
				if ((training.getStatus() & Training.NEW_TRAINING) != 0 || juniorsDao.getJuniorSkills(junior,training) == null) {
					juniorsDao.addJuniorSkills(junior.getId(), junior.getSkills()[0], training);
				} 
			}
		}
	}
}