package pl.pronux.sokker.data.sql.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.data.sql.dto.JuniorDto;
import pl.pronux.sokker.data.sql.dto.JuniorSkillsDto;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.JuniorSkills;
import pl.pronux.sokker.model.Person;
import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.model.Training;

public class JuniorsDao {
	private Connection connection;

	public JuniorsDao(Connection connection) {
		this.connection = connection;
	}

	public void addJunior(Junior junior) throws SQLException {
		PreparedStatement pstm;
		pstm = connection.prepareStatement("INSERT INTO junior(id_junior, name, surname, status) VALUES (?,?,?,0)"); //$NON-NLS-1$
		pstm.setInt(1, junior.getId());
		pstm.setString(2, junior.getName());
		pstm.setString(3, junior.getSurname());
		pstm.executeUpdate();
		pstm.close();

	}

	public void addJuniorSkills(int id, JuniorSkills jSkills, Date date) throws SQLException {
		PreparedStatement ps;
		ps = connection.prepareStatement("INSERT INTO junior_skills (id_junior_fk,millis,weeks,skill, day, week) VALUES (?, ?, ?, ?, ?, ?)"); //$NON-NLS-1$
		ps.setInt(1, id);
		ps.setLong(2, date.getMillis());
		ps.setInt(3, jSkills.getWeeks());
		ps.setInt(4, jSkills.getSkill());
		ps.setInt(5, date.getSokkerDate().getDay());
		ps.setInt(6, date.getSokkerDate().getWeek());
		ps.executeUpdate();
		ps.close();

	}

	public void updateJuniorSkills(int id, JuniorSkills jSkills, Date date) throws SQLException {
		PreparedStatement ps = null;

		if (SQLSession.databaseType == SQLSession.POSTGRESQL) {
			ps = connection
					.prepareStatement("UPDATE junior_skills SET " //$NON-NLS-1$
							+ "weeks = ?, " //$NON-NLS-1$
							+ "skill = ?, " //$NON-NLS-1$
							+ "millis = ?, day = ?, week = ?  " //$NON-NLS-1$
							+ "WHERE id_junior_fk = ? " //$NON-NLS-1$
							+ "AND week = (select max(week) from junior_skills WHERE id_junior_fk = ?) AND day = (select max(j.day) from junior_skills j where j.week = junior_skills.week AND j.id_junior_fk = ?)"); //$NON-NLS-1$
		} else if (SQLSession.databaseType == SQLSession.HSQLDB) {
			ps = connection.prepareStatement("UPDATE junior_skills j SET " //$NON-NLS-1$
					+ "weeks = ?, " //$NON-NLS-1$
					+ "skill = ?, " //$NON-NLS-1$
					+ "millis = ?, day = ?, week = ?  " //$NON-NLS-1$
					+ "WHERE id_junior_fk = ? " //$NON-NLS-1$
					+ "AND week = (select max(week) from junior_skills WHERE id_junior_fk = ?) AND day = (select max(day) from junior_skills where week = j.week AND id_junior_fk = ?)"); //$NON-NLS-1$
		}

		ps.setInt(1, jSkills.getWeeks());
		ps.setInt(2, jSkills.getSkill());
		ps.setLong(3, date.getMillis());
		ps.setInt(4, date.getSokkerDate().getDay());
		ps.setInt(5, date.getSokkerDate().getWeek());
		ps.setInt(6, id);
		ps.setInt(7, id);
		ps.setInt(8, id);
		ps.executeUpdate();
		ps.close();
	}

	public void addJuniorSkills(int id, JuniorSkills jSkills, Training training) throws SQLException {
		PreparedStatement ps;
		ps = connection.prepareStatement("INSERT INTO junior_skills (id_junior_fk,millis,weeks,skill, id_training_fk, day, week) VALUES (?, ?, ?, ?, ?, ?, ?)"); //$NON-NLS-1$
		ps.setInt(1, id);
		ps.setLong(2, training.getDate().getMillis());
		ps.setInt(3, jSkills.getWeeks());
		ps.setInt(4, jSkills.getSkill());
		ps.setInt(5, training.getId());
		ps.setInt(6, training.getDate().getSokkerDate().getDay());
		ps.setInt(7, training.getDate().getSokkerDate().getWeek());
		ps.executeUpdate();
		ps.close();

	}

	public int getJuniorID(String name) throws SQLException {
		int id = 0;
		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT id_junior FROM junior WHERE name = ? AND status = 0"); //$NON-NLS-1$
		ps.setString(1, name);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			id = rs.getInt(1);
		}
		rs.close();
		ps.close();

		return id;
	}

	/**
	 * @param status
	 *            0 - in school, 1 - trained
	 * @return ArrayList<Junior>
	 * @throws SQLException
	 */
	public List<Junior> getJuniors(int status) throws SQLException {
		List<Junior> juniors = new ArrayList<Junior>();
		Junior junior;
		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT id_junior,name,surname, status, note FROM junior WHERE status = ? ORDER BY surname"); //$NON-NLS-1$
		ps.setInt(1, status);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			junior = new JuniorDto(rs).getJunior();
			juniors.add(junior);
		}
		rs.close();
		ps.close();
		return juniors;
	}

	public List<Junior> getJuniorsFromTrash() throws SQLException {
		List<Junior> juniors = new ArrayList<Junior>();
		Junior junior;
		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT id_junior,name,surname, status, note FROM junior WHERE status > 10 AND status < 20 ORDER BY surname"); //$NON-NLS-1$
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			junior = new JuniorDto(rs).getJunior();
			juniors.add(junior);
		}
		rs.close();
		ps.close();
		return juniors;
	}

	public JuniorSkills[] getJuniorsSkills(Junior junior, Map<Integer, Training> trainingMap) throws SQLException {
		JuniorSkills juniorSkills;
		List<JuniorSkills> juniorsSkills = new ArrayList<JuniorSkills>();

		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT id_skill,id_junior_fk,millis,weeks,skill, id_training_fk, day, week FROM junior_skills WHERE id_junior_fk = ? order by week, day"); //$NON-NLS-1$
		ps.setInt(1, junior.getId());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			juniorSkills = new JuniorSkillsDto(rs).getJuniorSkills();
			juniorSkills.setTraining(trainingMap.get(rs.getInt("id_training_fk"))); //$NON-NLS-1$
			if (trainingMap.get(rs.getInt("id_training_fk")) != null) { //$NON-NLS-1$
				trainingMap.get(rs.getInt("id_training_fk")).getJuniors() //$NON-NLS-1$
						.add(junior);
			}
			juniorsSkills.add(juniorSkills);
		}
		rs.close();
		ps.close();
		return juniorsSkills.toArray(new JuniorSkills[juniorsSkills.size()]);
	}

	public boolean existsJunior(int id) throws SQLException {

		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT count(id_junior) FROM junior WHERE id_junior = ?"); //$NON-NLS-1$
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			if (rs.getInt(1) == 1) {
				rs.close();
				ps.close();
				return true;
			} else {
				rs.close();
				ps.close();
				return false;
			}
		}

		return false;
	}

	public boolean existsJuniorHistory(int id) throws SQLException {

		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT count(id_junior) FROM junior WHERE id_junior = ? AND status > 0"); //$NON-NLS-1$
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			if (rs.getInt(1) == 1) {
				rs.close();
				ps.close();
				return true;
			} else {
				rs.close();
				ps.close();
				return false;
			}
		}

		return false;
	}

	public List<JuniorSkills> getJuniorSkillsWithoutTrainingID() throws SQLException {
		JuniorSkills juniorSkills;
		List<JuniorSkills> juniorsSkills = new ArrayList<JuniorSkills>();

		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT id_skill,id_junior_fk,millis,weeks,skill, id_training_fk, day, week FROM junior_skills WHERE id_training_fk is null order by week, day"); //$NON-NLS-1$
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			juniorSkills = new JuniorSkillsDto(rs).getJuniorSkills();
			juniorsSkills.add(juniorSkills);
		}
		rs.close();
		ps.close();
		return juniorsSkills;
	}

	public void updateJuniorTrainingID(JuniorSkills juniorSkills) throws SQLException {
		PreparedStatement ps = null;
		ps = connection.prepareStatement("UPDATE junior_skills SET id_training_fk = ? WHERE id_skill = ?"); //$NON-NLS-1$
		ps.setInt(1, juniorSkills.getTraining().getId());
		ps.setInt(2, juniorSkills.getId());

		ps.executeUpdate();
		ps.close();
	}

	public JuniorSkills getJuniorSkills(Junior junior, Training training) throws SQLException {
		PreparedStatement ps = null;
		// SokkerDate sokkerDate = null;
		JuniorSkills juniorSkills = null;
		int week = training.getDate().getSokkerDate().getWeek();
		int day = training.getDate().getSokkerDate().getDay();
		ps = connection.prepareStatement("select * from junior_skills where ((day >= 5 and week = ?) or (day < 5 and week = ?)) and id_junior_fk = ?"); //$NON-NLS-1$
		if (day >= SokkerDate.THURSDAY) {
			ps.setInt(1, week);
			ps.setInt(2, week + 1);

		} else {
			ps.setInt(1, week - 1);
			ps.setInt(2, week);
		}
		ps.setInt(3, junior.getId());

		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			juniorSkills = new JuniorSkillsDto(rs).getJuniorSkills();
		}

		rs.close();
		ps.close();

		return juniorSkills;
	}

	public String moveTrainedJuniors(int clubId) throws SQLException {
		String movedJuniors = ""; //$NON-NLS-1$
		PreparedStatement ps;
		ps = connection
				.prepareStatement("SELECT j.id_junior FROM junior j, junior_skills s WHERE status = 0 AND j.id_junior = s.id_junior_fk AND s.weeks = 0 AND s.weeks = (select min(weeks) from junior_skills WHERE id_junior_fk = j.id_junior)"); //$NON-NLS-1$

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			movedJuniors = movedJuniors + rs.getInt(1) + '\n';
			this.moveJunior(rs.getInt(1), Junior.STATUS_TRAINED, clubId);
		}
		rs.close();
		ps.close();
		return movedJuniors;
	}

	public String moveTrainedJuniors(String sTemp, int clubId) throws SQLException {
		StringBuffer movedJuniors = new StringBuffer(); //$NON-NLS-1$
		PreparedStatement ps;
		ps = connection
				.prepareStatement("SELECT j.id_junior FROM junior j, junior_skills s WHERE status = 0 AND j.id_junior = s.id_junior_fk AND s.weeks = 0 AND s.weeks = (select min(weeks) from junior_skills WHERE id_junior_fk = j.id_junior) AND j.id_junior NOT IN " //$NON-NLS-1$
						+ sTemp);

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			movedJuniors.append(rs.getInt(1)).append('\n');
			this.moveJunior(rs.getInt(1), Junior.STATUS_TRAINED, clubId);
		}
		rs.close();
		ps.close();
		return movedJuniors.toString();
	}

	public String removeTrainedJuniors(int clubId) throws SQLException {
		StringBuffer deletedJuniors = new StringBuffer(); //$NON-NLS-1$
		PreparedStatement ps;
		PlayersDao playersDao = new PlayersDao(connection);
		ps = connection.prepareStatement("SELECT j.id_junior,j.name,j.surname FROM junior j WHERE status = 0"); //$NON-NLS-1$
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {

			if (playersDao.existsPlayer(rs.getString(2), rs.getString(3))) {
				this.moveJunior(rs.getInt(1), Junior.STATUS_TRAINED, clubId);
			} else {
				deletedJuniors.append(rs.getInt(1)).append('\n');
				// deleteJunior(rs.getInt(1));
				this.moveJunior(rs.getInt(1), Junior.STATUS_SACKED, clubId);
			}
		}
		rs.close();
		ps.close();
		return deletedJuniors.toString();
	}

	public String removeTrainedJuniors(String sTemp, int clubId) throws SQLException {
		String deletedJuniors = ""; //$NON-NLS-1$
		PreparedStatement ps;
		PlayersDao playersDao = new PlayersDao(connection);
		ps = connection.prepareStatement("SELECT j.id_junior,j.name,j.surname FROM junior j WHERE status = 0 AND j.id_junior NOT IN " + sTemp); //$NON-NLS-1$

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {

			if (playersDao.existsPlayer(rs.getString(2), rs.getString(3))) {
				// System.out.println("Przenosze juniora do
				// wytrenowanych = " +
				// rs.getInt(1));
				this.moveJunior(rs.getInt(1), Junior.STATUS_TRAINED, clubId);
			} else {
				deletedJuniors = deletedJuniors + rs.getInt(1) + '\n';
				// deleteJunior(rs.getInt(1));
				this.moveJunior(rs.getInt(1), Junior.STATUS_SACKED, clubId);
			}

		}
		rs.close();
		ps.close();
		return deletedJuniors;
	}

	public void moveJunior(int id, int status, int clubId) throws SQLException {
		PreparedStatement ps;

		ps = connection.prepareStatement("UPDATE junior SET status = ? WHERE id_junior = ?"); //$NON-NLS-1$
		ps.setInt(1, status);
		ps.setInt(2, id);
		ps.executeUpdate();
		ps.close();

		ps = SQLSession.getConnection().prepareStatement(
				"UPDATE player SET id_junior_fk = ? WHERE name = (SELECT name FROM junior WHERE id_junior = ?) AND surname = (SELECT surname FROM junior WHERE id_junior = ? ) AND youth_team_id = ?"); //$NON-NLS-1$
		ps.setInt(1, id);
		ps.setInt(2, id);
		ps.setInt(3, id);
		ps.setInt(4, clubId);
		ps.executeUpdate();
		ps.close();

	}

	public static void updateJuniorNote(Person junior) throws SQLException {
		PreparedStatement ps;
		ps = SQLSession.getConnection().prepareStatement("UPDATE junior SET " + "note = ? " + "WHERE id_junior = ?"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		ps.setString(1, junior.getNote());
		ps.setLong(2, junior.getId());

		ps.executeUpdate();
		ps.close();
	}

	public static boolean usingJunior(int id) throws SQLException {

		PreparedStatement ps;
		ps = SQLSession.getConnection().prepareStatement("SELECT count(id_junior_fk) FROM player WHERE id_junior_fk = ? AND status = 0"); //$NON-NLS-1$
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			if (rs.getInt(1) == 1) {
				rs.close();
				ps.close();
				return true;
			} else {
				rs.close();
				ps.close();
				return false;
			}
		}

		return false;
	}
}
