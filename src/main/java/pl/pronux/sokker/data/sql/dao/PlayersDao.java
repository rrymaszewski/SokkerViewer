package pl.pronux.sokker.data.sql.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.data.sql.dto.PlayerDto;
import pl.pronux.sokker.data.sql.dto.PlayerNtSkillsDto;
import pl.pronux.sokker.data.sql.dto.PlayerSkillsDto;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.NtSkills;
import pl.pronux.sokker.model.Person;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.PlayerSkills;
import pl.pronux.sokker.model.PlayerStats;
import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.model.Training;

public class PlayersDao {
	private Connection connection;

	public PlayersDao(Connection connection) {
		this.connection = connection;
	}

	public boolean existsPlayer(int pid) throws SQLException {

		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT count(id_player) FROM player WHERE id_player = ?"); //$NON-NLS-1$
		ps.setInt(1, pid);
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

	public void updatePlayerStatsInjuryDays(PlayerStats stats) throws SQLException {
		PreparedStatement pstm;
		pstm = connection.prepareStatement("UPDATE players_stats SET injury_days = ? WHERE player_id = ? AND match_id = ? AND team_id = ?"); //$NON-NLS-1$
		pstm.setInt(1, stats.getInjuryDays());
		pstm.setInt(2, stats.getPlayerID());
		pstm.setInt(3, stats.getMatchID());
		pstm.setInt(4, stats.getTeamID());
		pstm.executeUpdate();
		pstm.close();
	}

	public void updatePlayerYouthTeamID(Player player) throws SQLException {
		PreparedStatement pstm;
		pstm = connection.prepareStatement("UPDATE player SET youth_team_id = ?, exists_in_sokker = ? WHERE id_player = ?"); //$NON-NLS-1$
		pstm.setInt(1, player.getYouthTeamID());
		pstm.setInt(2, Player.EXISTS_IN_SOKKER_TRUE);
		pstm.setInt(3, player.getId());
		pstm.executeUpdate();
		pstm.close();
	}

	public void updateNotExists(int id) throws SQLException {
		PreparedStatement pstm;
		pstm = connection.prepareStatement("UPDATE player SET exists_in_sokker = ? WHERE id_player = ?"); //$NON-NLS-1$
		pstm.setInt(1, Player.EXISTS_IN_SOKKER_FALSE);
		pstm.setInt(2, id);
		pstm.executeUpdate();
		pstm.close();

	}

	public ArrayList<Integer> getPlayersIDWithoutYouthTeamId() throws SQLException {
		ArrayList<Integer> uncompletedPlayersID = new ArrayList<Integer>();
		PreparedStatement ps;

		ps = connection.prepareStatement("SELECT id_player FROM player where id_player in (select id_player from player where youth_team_id = 0) and exists_in_sokker = ?"); //$NON-NLS-1$
		ps.setInt(1, Player.EXISTS_IN_SOKKER_UNCHECKED);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			int id = new PlayerDto(rs).getPlayerID();
			uncompletedPlayersID.add(id);
		}
		rs.close();
		ps.close();
		return uncompletedPlayersID;
	}

	public void addPlayer(Player player) throws SQLException {
		PreparedStatement pstm;
		pstm = connection.prepareStatement("INSERT INTO player(id_player,name,surname,countryfrom, id_club_fk, status, national, transfer_list, youth_team_id) VALUES (?, ?, ?, ?, ?, 0, ?, ?, ?)"); //$NON-NLS-1$
		pstm.setInt(1, player.getId());
		pstm.setString(2, player.getName());
		pstm.setString(3, player.getSurname());
		pstm.setInt(4, player.getCountryfrom());
		pstm.setInt(5, player.getTeamID());
		pstm.setInt(6, player.getNational());
		pstm.setInt(7, player.getTransferList());
		pstm.setInt(8, player.getYouthTeamID());
		pstm.executeUpdate();
		pstm.close();

	}

	public void addPlayerSkills(int id, PlayerSkills skills, Date date) throws SQLException {
		PreparedStatement ps;
		ps = connection
				.prepareStatement("INSERT INTO player_skills (id_player_fk,millis,age,value,salary,form,stamina,pace,technique,passing,keeper,defender,playmaker,scorer,matches,goals,assists,cards,injurydays,day,week,experience, teamwork, discipline) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)"); //$NON-NLS-1$
		ps.setInt(1, id);
		ps.setLong(2, date.getMillis());
		ps.setInt(3, skills.getAge());
		ps.setInt(4, skills.getValue().toInt());
		ps.setInt(5, skills.getSalary().toInt());
		ps.setInt(6, skills.getForm());
		ps.setInt(7, skills.getStamina());
		ps.setInt(8, skills.getPace());
		ps.setInt(9, skills.getTechnique());
		ps.setInt(10, skills.getPassing());
		ps.setInt(11, skills.getKeeper());
		ps.setInt(12, skills.getDefender());
		ps.setInt(13, skills.getPlaymaker());
		ps.setInt(14, skills.getScorer());
		ps.setInt(15, skills.getMatches());
		ps.setInt(16, skills.getGoals());
		ps.setInt(17, skills.getAssists());
		ps.setInt(18, skills.getCards());
		ps.setDouble(19, skills.getInjurydays());
		ps.setInt(20, date.getSokkerDate().getDay());
		ps.setInt(21, date.getSokkerDate().getWeek());
		ps.setInt(22, skills.getExperience());
		ps.setInt(23, skills.getTeamwork());
		ps.setInt(24, skills.getDiscipline());
		ps.executeUpdate();
		ps.close();

	}

	public void addPlayerSkills(int id, PlayerSkills skills, Date date, int training_id) throws SQLException {
		PreparedStatement ps;
		ps = connection
				.prepareStatement("INSERT INTO player_skills (id_player_fk,millis,age,value,salary,form,stamina,pace,technique,passing,keeper,defender,playmaker,scorer,matches,goals,assists,cards,injurydays, id_training_fk, day, week, experience, teamwork, discipline, pass_training) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"); //$NON-NLS-1$
		ps.setInt(1, id);
		ps.setLong(2, date.getMillis());
		ps.setInt(3, skills.getAge());
		ps.setInt(4, skills.getValue().toInt());
		ps.setInt(5, skills.getSalary().toInt());
		ps.setInt(6, skills.getForm());
		ps.setInt(7, skills.getStamina());
		ps.setInt(8, skills.getPace());
		ps.setInt(9, skills.getTechnique());
		ps.setInt(10, skills.getPassing());
		ps.setInt(11, skills.getKeeper());
		ps.setInt(12, skills.getDefender());
		ps.setInt(13, skills.getPlaymaker());
		ps.setInt(14, skills.getScorer());
		ps.setInt(15, skills.getMatches());
		ps.setInt(16, skills.getGoals());
		ps.setInt(17, skills.getAssists());
		ps.setInt(18, skills.getCards());
		ps.setDouble(19, skills.getInjurydays());
		ps.setInt(20, training_id);
		ps.setInt(21, date.getSokkerDate().getDay());
		ps.setInt(22, date.getSokkerDate().getWeek());
		ps.setInt(23, skills.getExperience());
		ps.setInt(24, skills.getTeamwork());
		ps.setInt(25, skills.getDiscipline());
		ps.setBoolean(26, skills.isPassTraining());
		ps.executeUpdate();
		ps.close();

	}

	public List<Player> getPlayers(int status) throws SQLException {
		return getPlayers(status, new HashMap<Integer, Junior>());
	}

	/**
	 * 
	 * @param n
	 *            numbers of players
	 * @param status
	 *            0 - engaged, 1 - sold/fired/away
	 * @param juniorTrainedMap
	 * @param transfersMap
	 * @return Player[]
	 * @throws SQLException
	 */

	public ArrayList<Player> getPlayers(int status, HashMap<Integer, Junior> juniorTrainedMap) throws SQLException {
		Player player;
		ArrayList<Player> alPlayer = new ArrayList<Player>();
		PreparedStatement ps;
		ps = connection
				.prepareStatement("SELECT id_player, name, surname, countryfrom,id_junior_fk, status, id_position, sold_price, buy_price, note, id_club_fk,transfer_list, national, youth_team_id FROM player WHERE status = ? ORDER BY surname"); //$NON-NLS-1$
		ps.setInt(1, status);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			player = new PlayerDto(rs).getPlayer();
			alPlayer.add(player);
		}
		rs.close();
		ps.close();
		return alPlayer;
	}

	public PlayerSkills[] getPlayerSkills(Player player, HashMap<Integer, Training> trainingMap) throws SQLException {
		PlayerSkills playerSkills;
		ArrayList<PlayerSkills> alPlayerSkills = new ArrayList<PlayerSkills>();

		PreparedStatement ps;
		ps = connection
				.prepareStatement("SELECT id_skill,id_player_fk,millis,age,value,salary,form,stamina,pace,technique,passing,keeper,defender,playmaker,scorer,matches,goals,assists,cards,injurydays,id_training_fk,experience,teamwork,discipline,day,week,pass_training FROM player_skills WHERE id_player_fk = ? order by week, day"); //$NON-NLS-1$
		ps.setInt(1, player.getId());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			playerSkills = new PlayerSkillsDto(rs).getPlayerSkills();
			playerSkills.setTraining(trainingMap.get(playerSkills.getTrainingID()));
			if (trainingMap.get(playerSkills.getTrainingID()) != null) {
				trainingMap.get(playerSkills.getTrainingID()).getPlayers().add(player);
			}
			alPlayerSkills.add(playerSkills);
		}
		rs.close();
		ps.close();
		return alPlayerSkills.toArray(new PlayerSkills[alPlayerSkills.size()]);
	}

	public ArrayList<PlayerSkills> getPlayerSkillsWithoutTrainingID() throws SQLException {
		PlayerSkills playerSkills;
		ArrayList<PlayerSkills> alPlayerSkills = new ArrayList<PlayerSkills>();

		PreparedStatement ps;
		ps = connection
				.prepareStatement("SELECT id_skill,id_player_fk,millis,age,value,salary,form,stamina,pace,technique,passing,keeper,defender,playmaker,scorer,matches,goals,assists,cards,injurydays,id_training_fk,experience,teamwork,discipline,day,week,pass_training FROM player_skills WHERE id_training_fk is null order by week, day"); //$NON-NLS-1$
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			playerSkills = new PlayerSkillsDto(rs).getPlayerSkills();
			alPlayerSkills.add(playerSkills);
		}
		rs.close();
		ps.close();
		return alPlayerSkills;
	}

	public NtSkills[] getPlayerNtSkills(Player player) throws SQLException {
		NtSkills ntSkills;
		ArrayList<NtSkills> listNtSkills = new ArrayList<NtSkills>();

		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT * FROM nt_player_skills WHERE id_player_fk = ? order by week, day"); //$NON-NLS-1$
		ps.setInt(1, player.getId());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ntSkills = new PlayerNtSkillsDto(rs).getNtSkill();
			listNtSkills.add(ntSkills);
		}
		rs.close();
		ps.close();
		return listNtSkills.toArray(new NtSkills[listNtSkills.size()]);
	}

	public void updatePlayerTrainingID(PlayerSkills skills) throws SQLException {
		PreparedStatement ps = null;
		ps = connection.prepareStatement("UPDATE player_skills SET id_training_fk = ? WHERE id_skill = ?"); //$NON-NLS-1$
		ps.setInt(1, skills.getTrainingID());
		ps.setInt(2, skills.getId());

		ps.executeUpdate();
		ps.close();
	}

	public void updatePlayerSkills(int id, PlayerSkills skills, Training training) throws SQLException {
		PreparedStatement ps = null;
		ps = connection.prepareStatement("UPDATE player_skills SET " + "millis = ?, " + "age = ?, " + "value = ?, " + "salary = ?, " + "form = ?, " + "stamina = ?, " + "pace = ?, " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
				+ "technique = ?, " + "passing = ?, " + "keeper = ?, " + "defender = ? ," + "playmaker = ?, " + "scorer = ?, " + "matches = ?, " + "goals = ?, " + "assists = ?, " + "cards = ?, " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
				+ "injurydays = ?, " + "day = ?, " + "week = ?, " + "experience = ?, " + "teamwork = ?, " + "discipline = ? " + "WHERE id_player_fk = ? AND id_training_fk = ?"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$

		ps.setLong(1, training.getDate().getMillis());
		ps.setInt(2, skills.getAge());
		ps.setInt(3, skills.getValue().toInt());
		ps.setInt(4, skills.getSalary().toInt());
		ps.setInt(5, skills.getForm());
		ps.setInt(6, skills.getStamina());
		ps.setInt(7, skills.getPace());
		ps.setInt(8, skills.getTechnique());
		ps.setInt(9, skills.getPassing());
		ps.setInt(10, skills.getKeeper());
		ps.setInt(11, skills.getDefender());
		ps.setInt(12, skills.getPlaymaker());
		ps.setInt(13, skills.getScorer());
		ps.setInt(14, skills.getMatches());
		ps.setInt(15, skills.getGoals());
		ps.setInt(16, skills.getAssists());
		ps.setInt(17, skills.getCards());
		ps.setDouble(18, skills.getInjurydays());
		ps.setInt(19, training.getDate().getSokkerDate().getDay());
		ps.setInt(20, training.getDate().getSokkerDate().getWeek());
		ps.setInt(21, skills.getExperience());
		ps.setInt(22, skills.getTeamwork());
		ps.setInt(23, skills.getDiscipline());
		ps.setInt(24, id);
		ps.setInt(25, training.getId());

		ps.executeUpdate();
		ps.close();
	}

	public void updatePlayerSkills(int id, PlayerSkills skills, Date date) throws SQLException {
		PreparedStatement ps = null;

		if (SQLSession.databaseType == SQLSession.POSTGRESQL) {
			ps = connection
					.prepareStatement("UPDATE player_skills SET " //$NON-NLS-1$
							+ "millis = ?, " //$NON-NLS-1$
							+ "age = ?, " //$NON-NLS-1$
							+ "value = ?, " //$NON-NLS-1$
							+ "salary = ?, " //$NON-NLS-1$
							+ "form = ?, " //$NON-NLS-1$
							+ "stamina = ?, " //$NON-NLS-1$
							+ "pace = ?, " //$NON-NLS-1$
							+ "technique = ?, " //$NON-NLS-1$
							+ "passing = ?, " //$NON-NLS-1$
							+ "keeper = ?, " //$NON-NLS-1$
							+ "defender = ? ," //$NON-NLS-1$
							+ "playmaker = ?, " //$NON-NLS-1$
							+ "scorer = ?, " //$NON-NLS-1$
							+ "matches = ?, " //$NON-NLS-1$
							+ "goals = ?, " //$NON-NLS-1$
							+ "assists = ?, " //$NON-NLS-1$
							+ "cards = ?, " //$NON-NLS-1$
							+ "injurydays = ?, " //$NON-NLS-1$
							+ "day = ?, " //$NON-NLS-1$
							+ "week = ?, " //$NON-NLS-1$
							+ "experience = ?, " //$NON-NLS-1$
							+ "teamwork = ?, " //$NON-NLS-1$
							+ "discipline = ? " //$NON-NLS-1$
							+ "WHERE id_player_fk = ? AND week = (select max(week) from player_skills WHERE id_player_fk = ?) AND day = (select max(p.day) from player_skills p where p.week = player_skills.week AND p.id_player_fk = ?)"); //$NON-NLS-1$
		} else if (SQLSession.databaseType == SQLSession.HSQLDB) {
			ps = connection
					.prepareStatement("UPDATE player_skills p SET " //$NON-NLS-1$
							+ "millis = ?, " //$NON-NLS-1$
							+ "age = ?, " //$NON-NLS-1$
							+ "value = ?, " //$NON-NLS-1$
							+ "salary = ?, " //$NON-NLS-1$
							+ "form = ?, " //$NON-NLS-1$
							+ "stamina = ?, " //$NON-NLS-1$
							+ "pace = ?, " //$NON-NLS-1$
							+ "technique = ?, " //$NON-NLS-1$
							+ "passing = ?, " //$NON-NLS-1$
							+ "keeper = ?, " //$NON-NLS-1$
							+ "defender = ? ," //$NON-NLS-1$
							+ "playmaker = ?, " //$NON-NLS-1$
							+ "scorer = ?, " //$NON-NLS-1$
							+ "matches = ?, " //$NON-NLS-1$
							+ "goals = ?, " //$NON-NLS-1$
							+ "assists = ?, " //$NON-NLS-1$
							+ "cards = ?, " //$NON-NLS-1$
							+ "injurydays = ?, " //$NON-NLS-1$
							+ "day = ?, " //$NON-NLS-1$
							+ "week = ?, " //$NON-NLS-1$
							+ "experience = ?, " //$NON-NLS-1$
							+ "teamwork = ?, " //$NON-NLS-1$
							+ "discipline = ? " //$NON-NLS-1$
							+ "WHERE id_player_fk = ? AND week = (select max(week) from player_skills WHERE id_player_fk = ?) AND day = (select max(day) from player_skills where week = p.week AND id_player_fk = ?)"); //$NON-NLS-1$
		}

		ps.setLong(1, date.getMillis());
		ps.setInt(2, skills.getAge());
		ps.setInt(3, skills.getValue().toInt());
		ps.setInt(4, skills.getSalary().toInt());
		ps.setInt(5, skills.getForm());
		ps.setInt(6, skills.getStamina());
		ps.setInt(7, skills.getPace());
		ps.setInt(8, skills.getTechnique());
		ps.setInt(9, skills.getPassing());
		ps.setInt(10, skills.getKeeper());
		ps.setInt(11, skills.getDefender());
		ps.setInt(12, skills.getPlaymaker());
		ps.setInt(13, skills.getScorer());
		ps.setInt(14, skills.getMatches());
		ps.setInt(15, skills.getGoals());
		ps.setInt(16, skills.getAssists());
		ps.setInt(17, skills.getCards());
		ps.setDouble(18, skills.getInjurydays());
		ps.setInt(19, date.getSokkerDate().getDay());
		ps.setInt(20, date.getSokkerDate().getWeek());
		ps.setInt(21, skills.getExperience());
		ps.setInt(22, skills.getTeamwork());
		ps.setInt(23, skills.getDiscipline());
		ps.setInt(24, id);
		ps.setInt(25, id);
		ps.setInt(26, id);

		ps.executeUpdate();
		ps.close();
	}

	public void updatePlayer(Player player) throws SQLException {
		PreparedStatement pstm = null;
		pstm = connection.prepareStatement("UPDATE player SET transfer_list = ?, national = ? WHERE id_player = ?"); //$NON-NLS-1$
		pstm.setInt(1, player.getTransferList());
		pstm.setInt(2, player.getNational());
		pstm.setInt(3, player.getId());
		pstm.executeUpdate();
		pstm.close();
	}

	public void addNtPlayerSkills(int id, NtSkills skills, Date date) throws SQLException {
		PreparedStatement ps;
		ps = connection.prepareStatement("INSERT INTO nt_player_skills (id_player_fk,millis,nt_matches,nt_goals,nt_assists,nt_cards,day,week) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"); //$NON-NLS-1$
		ps.setInt(1, id);
		ps.setLong(2, date.getMillis());
		ps.setInt(3, skills.getNtMatches());
		ps.setInt(4, skills.getNtGoals());
		ps.setInt(5, skills.getNtAssists());
		ps.setInt(6, skills.getNtCards());
		ps.setInt(7, date.getSokkerDate().getDay());
		ps.setInt(8, date.getSokkerDate().getWeek());
		ps.executeUpdate();
		ps.close();
	}

	public int getNumberOfNtMatch(int playerID) throws SQLException {
		int matches = 0;
		PreparedStatement ps;
		ps = connection
				.prepareStatement("select nt_matches from nt_player_skills t where week = (select max(week) from nt_player_skills) AND day = (select max(day) from nt_player_skills where week = t.week) AND id_player_fk = ?"); //$NON-NLS-1$
		ps.setInt(1, playerID);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			matches = rs.getInt(1);
			break;
		}
		rs.close();
		ps.close();

		return matches;
	}

	public SokkerDate getMaxPlayerSkillSokkerDate() throws SQLException {
		PreparedStatement ps;
		SokkerDate sokkerDate = null;
		ps = connection.prepareStatement("select day, week from player_skills t where week = (select max(week) from player_skills) and day = (select max(day) from player_skills where week = t.week)"); //$NON-NLS-1$

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			sokkerDate = new SokkerDate();
			sokkerDate.setDay(rs.getInt(1));
			sokkerDate.setWeek(rs.getInt(2));
		}

		rs.close();
		ps.close();

		return sokkerDate;

	}

	public PlayerSkills getPlayerSkills(Player player, Training training) throws SQLException {
		PreparedStatement ps = null;
		// SokkerDate sokkerDate = null;
		PlayerSkills playerSkills = null;
		int week = training.getDate().getSokkerDate().getWeek();
		int day = training.getDate().getSokkerDate().getDay();
		ps = connection.prepareStatement("select * from player_skills where ((day >= 5 and week = ?) or (day < 5 and week = ?)) and id_player_fk = ?"); //$NON-NLS-1$
		if (day >= SokkerDate.THURSDAY) {
			ps.setInt(1, week);
			ps.setInt(2, week + 1);

		} else {
			ps.setInt(1, week - 1);
			ps.setInt(2, week);
		}
		ps.setInt(3, player.getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			// sokkerDate = new SokkerDate(rs.getInt("day"), rs.getInt("week"));
			playerSkills = new PlayerSkillsDto(rs).getPlayerSkills();
		}

		rs.close();
		ps.close();

		return playerSkills;
	}

	public String removeSoldPlayer(String sTemp) throws SQLException {
		String deletedPlayers = ""; //$NON-NLS-1$
		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT id_player FROM player WHERE status = 0 AND id_player NOT IN " + sTemp); //$NON-NLS-1$
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			deletedPlayers = deletedPlayers + rs.getInt(1) + '\n';
			this.movePlayer(rs.getInt(1), Player.STATUS_HISTORY);
			// deletePlayer(rs.getInt(1));
		}
		rs.close();
		ps.close();
		return deletedPlayers;
	}

	public String removeSoldPlayer() throws SQLException {
		String deletedPlayers = ""; //$NON-NLS-1$
		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT id_player FROM player WHERE status = 0 "); //$NON-NLS-1$
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			deletedPlayers = deletedPlayers + rs.getInt(1) + '\n';
			this.movePlayer(rs.getInt(1), Player.STATUS_HISTORY);
			// deletePlayer(rs.getInt(1));
		}
		rs.close();
		ps.close();
		return deletedPlayers;
	}

	public void movePlayer(int id, int status) throws SQLException {
		PreparedStatement ps;

		ps = connection.prepareStatement("UPDATE player SET status = ? WHERE id_player = ?"); //$NON-NLS-1$
		ps.setInt(1, status);
		ps.setInt(2, id);
		ps.executeUpdate();
		ps.close();

	}

	public boolean existsPlayerHistory(int pid) throws SQLException {

		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT count(id_player) FROM player WHERE id_player = ? AND status > 0"); //$NON-NLS-1$
		ps.setInt(1, pid);
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

	public boolean existsPlayer(String name, String surname) throws SQLException {

		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT id_player FROM player WHERE name = ? AND surname = ?"); //$NON-NLS-1$
		ps.setString(1, name);
		ps.setString(2, surname);
		ResultSet rs = ps.executeQuery();
		// while (rs.next()) {
		// if (rs.getInt(1) == 1) {
		// rs.close();
		// ps.close();
		// return true;
		// } else {
		// rs.close();
		// ps.close();
		// return false;
		// }
		// }
		return rs.next();
	}

	public ArrayList<Long> getTrainingDates(int playerID) throws SQLException {
		PreparedStatement ps;
		ArrayList<Long> alMillis = new ArrayList<Long>();
		ps = connection.prepareStatement("SELECT millis FROM player_skills WHERE id_player_fk = ?"); //$NON-NLS-1$
		ps.setInt(1, playerID);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			alMillis.add(rs.getLong(1));
		}

		return alMillis;
	}

	public void updatePlayerBuyPrice(Player player) throws SQLException {
		PreparedStatement pstm;
		pstm = connection.prepareStatement("UPDATE player SET buy_price = ? WHERE id_player = ?"); //$NON-NLS-1$
		pstm.setDouble(1, player.getBuyPrice().getDoubleValue());
		pstm.setInt(2, player.getId());
		pstm.executeUpdate();
		pstm.close();
	}

	public void updatePlayerNote(Person player) throws SQLException {
		PreparedStatement ps;
		ps = connection.prepareStatement("UPDATE player SET " + "note = ? " + "WHERE id_player = ?"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		ps.setString(1, player.getNote());
		ps.setLong(2, player.getId());

		ps.executeUpdate();
		ps.close();
	}

	public void updatePlayerPosition(Player player) throws SQLException {
		PreparedStatement pstm;
		pstm = connection.prepareStatement("UPDATE player SET id_position = ? WHERE id_player = ?"); //$NON-NLS-1$
		pstm.setInt(1, player.getPosition());
		pstm.setInt(2, player.getId());
		pstm.executeUpdate();
		pstm.close();
	}

	public void updatePlayerPassTraining(PlayerSkills playerSkills) throws SQLException {
		PreparedStatement ps;
		ps = connection.prepareStatement("UPDATE player_skills SET pass_training = ? WHERE id_skill = ?"); //$NON-NLS-1$

		ps.setBoolean(1, playerSkills.isPassTraining());
		ps.setInt(2, playerSkills.getId());

		ps.executeUpdate();
		ps.close();

	}

	public void updatePlayerSoldPrice(Player player) throws SQLException {
		PreparedStatement pstm;
		pstm = connection.prepareStatement("UPDATE player SET sold_price = ? WHERE id_player = ?"); //$NON-NLS-1$
		pstm.setDouble(1, player.getSoldPrice().getDoubleValue());
		pstm.setInt(2, player.getId());
		pstm.executeUpdate();
		pstm.close();
	}

}
