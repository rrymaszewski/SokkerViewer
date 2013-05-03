package pl.pronux.sokker.data.sql.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.pronux.sokker.data.sql.dto.CoachDto;
import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.Person;
import pl.pronux.sokker.model.Training;

public class TrainersDao {
	private Connection connection;

	public TrainersDao(Connection connection) {
		this.connection = connection;
	}

	public void clearCoaches() throws SQLException {
		PreparedStatement ps = connection.prepareStatement("DELETE FROM coach"); 
		ps.executeUpdate();
		ps.close();

	}

	public void deleteCoachesAtTraining(Training training) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("DELETE FROM coaches_at_trainings WHERE id_training = ?"); 
		ps.setInt(1, training.getId());
		ps.executeUpdate();
		ps.close();
	}

	/**
	 * 
	 * @param status
	 *            0 - engaged, 1 - fired
	 * @return
	 * @throws SQLException
	 */
	public List<Coach> getCoaches(int status) throws SQLException {
		List<Coach> alCoach = new ArrayList<Coach>();
		PreparedStatement ps = connection
				.prepareStatement("SELECT id_coach, name, surname,job, signed, countryfrom, age, salary, generalskill, stamina, pace, technique, passing, keepers, defenders, playmakers, scorers, status, note  FROM coach WHERE status = ? ORDER BY surname"); 
		ps.setInt(1, status);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Coach coach = new CoachDto(rs).getCoach();
			alCoach.add(coach);
		}
		rs.close();
		ps.close();
		return alCoach;
	}

	public Training getCoachesAtTraining(Training training, Map<Integer, Coach> coachMap) throws SQLException {

		training.setAssistants(new ArrayList<Coach>());

		PreparedStatement pstm = connection.prepareStatement("SELECT id_coach,id_job FROM coaches_at_trainings WHERE id_training = ?"); 
		pstm.setInt(1, training.getId());
		ResultSet rs = pstm.executeQuery();

		while (rs.next()) {
			int coachId = rs.getInt(1);
			int job = rs.getInt(2);
			switch (job) {
			case Coach.JOB_HEAD:
				training.setHeadCoach(coachMap.get(coachId));
				break;

			case Coach.JOB_JUNIORS:
				training.setJuniorCoach(coachMap.get(coachId));
				break;

			case Coach.JOB_ASSISTANT:
				training.getAssistants().add(coachMap.get(coachId));
				break;

			default:
				break;
			}
		}
		rs.close();
		pstm.close();

		return training;
	}

	public boolean existsCoach(int id) throws SQLException {

		PreparedStatement ps = connection.prepareStatement("SELECT count(id_coach) FROM coach WHERE id_coach = ?"); 
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

	public boolean existsCoachHistory(int id) throws SQLException {

		PreparedStatement ps = connection.prepareStatement("SELECT count(id_coach) FROM coach WHERE id_coach = ? AND status > 0"); 
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

	public void addCoach(Coach coach) throws SQLException {
		PreparedStatement pstm = connection
				.prepareStatement("INSERT INTO coach(id_coach,signed,name,surname,job,countryfrom,generalskill,stamina,pace,technique,passing,keepers,defenders,playmakers,scorers,salary,age, status) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,0)"); 

		pstm.setInt(1, coach.getId());
		pstm.setInt(2, coach.getSigned());
		pstm.setString(3, coach.getName());
		pstm.setString(4, coach.getSurname());
		pstm.setInt(5, coach.getJob());
		pstm.setInt(6, coach.getCountryfrom());

		if (coach.getGeneralskill() >= 0) {
			pstm.setInt(7, coach.getGeneralskill());
		} else {
			pstm.setInt(7, 0);
		}

		if (coach.getStamina() >= 0) {
			pstm.setInt(8, coach.getStamina());
		} else {
			pstm.setInt(8, 0);
		}
		if (coach.getPace() >= 0) {
			pstm.setInt(9, coach.getPace());
		} else {
			pstm.setInt(9, 0);
		}

		if (coach.getTechnique() >= 0) {
			pstm.setInt(10, coach.getTechnique());
		} else {
			pstm.setInt(10, 0);
		}

		if (coach.getPassing() >= 0) {
			pstm.setInt(11, coach.getPassing());
		} else {
			pstm.setInt(11, 0);
		}

		if (coach.getKeepers() >= 0) {
			pstm.setInt(12, coach.getKeepers());
		} else {
			pstm.setInt(12, 0);
		}

		if (coach.getDefenders() >= 0) {
			pstm.setInt(13, coach.getDefenders());
		} else {
			pstm.setInt(13, 0);
		}

		if (coach.getPlaymakers() >= 0) {
			pstm.setInt(14, coach.getPlaymakers());
		} else {
			pstm.setInt(14, 0);
		}

		if (coach.getScorers() >= 0) {
			pstm.setInt(15, coach.getScorers());
		} else {
			pstm.setInt(15, 0);
		}

		pstm.setInt(16, coach.getSalary().toInt());
		pstm.setInt(17, coach.getAge());
		pstm.executeUpdate();
		pstm.close();

	}

	public void addCoachesAtTraining(Training training) throws SQLException {
		PreparedStatement pstm = null;

		if (training.getHeadCoach() != null) {
			pstm = connection.prepareStatement("INSERT INTO coaches_at_trainings(id_training, id_coach, id_job) VALUES (?,?,?)"); 
			pstm.setInt(1, training.getId());
			pstm.setInt(2, training.getHeadCoach().getId());
			pstm.setInt(3, Coach.JOB_HEAD);
			pstm.executeUpdate();
		}

		if (training.getJuniorCoach() != null) {
			pstm = connection.prepareStatement("INSERT INTO coaches_at_trainings(id_training, id_coach, id_job) VALUES (?,?,?)"); 
			pstm.setInt(1, training.getId());
			pstm.setInt(2, training.getJuniorCoach().getId());
			pstm.setInt(3, Coach.JOB_JUNIORS);
			pstm.executeUpdate();
		}

		List<Coach> assistants = training.getAssistants();

		for (Coach coach : assistants) {
			pstm = connection.prepareStatement("INSERT INTO coaches_at_trainings(id_training, id_coach, id_job) VALUES (?,?,?)"); 
			pstm.setInt(1, training.getId());
			pstm.setInt(2, coach.getId());
			pstm.setInt(3, Coach.JOB_ASSISTANT);
			pstm.executeUpdate();
		}

		if (pstm != null) {
			pstm.close();
		}

	}

	public void moveCoach(int id, int status) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE coach SET status = ? WHERE id_coach = ?"); 
		ps.setInt(1, status);
		ps.setInt(2, id);
		ps.executeUpdate();
		ps.close();
	}

	public String removeCoaches() throws SQLException {
		String deletedCoaches = ""; 
		PreparedStatement ps = connection.prepareStatement("SELECT id_coach FROM coach WHERE status = 0"); 

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			deletedCoaches = deletedCoaches + rs.getInt(1) + '\n';
			moveCoach(rs.getInt(1), Coach.STATUS_SACKED);
		}
		rs.close();
		ps.close();
		return deletedCoaches;
	}

	public String removeCoaches(String sTemp) throws SQLException {
		String deletedCoaches = ""; 
		PreparedStatement ps = connection.prepareStatement("SELECT id_coach FROM coach WHERE status = 0 AND id_coach NOT IN " + sTemp); 

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			deletedCoaches = deletedCoaches + rs.getInt(1) + '\n';
			moveCoach(rs.getInt(1), Coach.STATUS_SACKED);
		}
		rs.close();
		ps.close();
		return deletedCoaches;
	}

	public void updateCoach(Coach coach) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE coach SET job = ?, age = ?, signed = ? WHERE id_coach = ?"); 

		ps.setInt(1, coach.getJob());
		ps.setInt(2, coach.getAge());
		ps.setInt(3, coach.getSigned());
		ps.setInt(4, coach.getId());

		ps.executeUpdate();
		ps.close();
	}

	public void updateCoachNote(Person coach) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE coach SET note = ? WHERE id_coach = ?"); 

		ps.setString(1, coach.getNote());
		ps.setLong(2, coach.getId());

		ps.executeUpdate();
		ps.close();
	}

	public void repairCoach(Coach coach) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE coach SET keepers = ? WHERE id_coach = ?"); 

		if (coach.getKeepers() >= 0) {
			ps.setInt(1, coach.getKeepers());
		} else {
			ps.setInt(1, 0);
		}
		ps.setInt(2, coach.getId());

		ps.executeUpdate();
		ps.close();
	}
}
