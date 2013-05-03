package pl.pronux.sokker.data.sql.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public boolean existsPlayer(int playerId) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT count(id_player) FROM player WHERE id_player = ?");
		ps.setInt(1, playerId);
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
		PreparedStatement pstm = connection.prepareStatement("UPDATE players_stats SET injury_days = ? WHERE player_id = ? AND match_id = ? AND team_id = ?");
		pstm.setInt(1, stats.getInjuryDays());
		pstm.setInt(2, stats.getPlayerId());
		pstm.setInt(3, stats.getMatchId());
		pstm.setInt(4, stats.getTeamId());
		pstm.executeUpdate();
		pstm.close();
	}

	public void updateUncompletedPlayer(Player player) throws SQLException {
		PreparedStatement pstm = connection.prepareStatement("UPDATE player SET height = ?, youth_team_id = ?, exists_in_sokker = ? WHERE id_player = ?");
		pstm.setInt(1, player.getHeight());
		pstm.setInt(2, player.getYouthTeamId());
		pstm.setInt(3, Player.EXISTS_IN_SOKKER_TRUE);
		pstm.setInt(4, player.getId());
		pstm.executeUpdate();
		pstm.close();
	}

	public void updateNotExists(int playerId) throws SQLException {
		PreparedStatement pstm = connection.prepareStatement("UPDATE player SET exists_in_sokker = ? WHERE id_player = ?");
		pstm.setInt(1, Player.EXISTS_IN_SOKKER_FALSE);
		pstm.setInt(2, playerId);
		pstm.executeUpdate();
		pstm.close();

	}

	public List<Integer> getUncompletePlayers() throws SQLException {
		List<Integer> uncompletedPlayersID = new ArrayList<Integer>();
		PreparedStatement ps = connection
			.prepareStatement("SELECT id_player FROM player where id_player in (select id_player from player where youth_team_id = 0 or height = 0) and exists_in_sokker = ?");
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
		PreparedStatement pstm = connection
			.prepareStatement("INSERT INTO player(id_player,name,surname,countryfrom, id_club_fk, status, national, transfer_list, youth_team_id, height) VALUES (?, ?, ?, ?, ?, 0, ?, ?, ?, ?)");
		pstm.setInt(1, player.getId());
		pstm.setString(2, player.getName());
		pstm.setString(3, player.getSurname());
		pstm.setInt(4, player.getCountryfrom());
		pstm.setInt(5, player.getTeamId());
		pstm.setInt(6, player.getNational());
		pstm.setInt(7, player.getTransferList());
		pstm.setInt(8, player.getYouthTeamId());
		pstm.setInt(9, player.getHeight());
		pstm.executeUpdate();
		pstm.close();

	}

	public void addPlayerSkills(int id, PlayerSkills skills, Date date) throws SQLException {
		PreparedStatement ps = connection
			.prepareStatement("INSERT INTO player_skills (id_player_fk,millis,age,value,salary,form,stamina,pace,technique,passing,keeper,defender,playmaker,scorer,matches,goals,assists,cards,injurydays,day,week,experience, teamwork, discipline) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)"); 
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

	public void addPlayerSkills(int id, PlayerSkills skills, Date date, int trainingId) throws SQLException {
		PreparedStatement ps = connection
			.prepareStatement("INSERT INTO player_skills (id_player_fk,millis,age,value,salary,form,stamina,pace,technique,passing,keeper,defender,playmaker,scorer,matches,goals,assists,cards,injurydays, id_training_fk, day, week, experience, teamwork, discipline, pass_training) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"); 
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
		ps.setInt(20, trainingId);
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
	 *        numbers of players
	 * @param status
	 *        0 - engaged, 1 - sold/fired/away
	 * @param juniorTrainedMap
	 * @param transfersMap
	 * @return Player[]
	 * @throws SQLException
	 */

	public List<Player> getPlayers(int status, Map<Integer, Junior> juniorTrainedMap) throws SQLException {
		List<Player> alPlayer = new ArrayList<Player>();
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM player WHERE status = ? ORDER BY surname");
		ps.setInt(1, status);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Player player = new PlayerDto(rs).getPlayer();
			alPlayer.add(player);
		}
		rs.close();
		ps.close();
		return alPlayer;
	}

	public PlayerSkills[] getPlayerSkills(Player player, Map<Integer, Training> trainingMap) throws SQLException {
		List<PlayerSkills> alPlayerSkills = new ArrayList<PlayerSkills>();
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM player_skills WHERE id_player_fk = ? order by week, day");
		ps.setInt(1, player.getId());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			PlayerSkills playerSkills = new PlayerSkillsDto(rs).getPlayerSkills();
			playerSkills.setTraining(trainingMap.get(playerSkills.getTrainingId()));
			if (trainingMap.get(playerSkills.getTrainingId()) != null) {
				trainingMap.get(playerSkills.getTrainingId()).getPlayers().add(player);
			}
			alPlayerSkills.add(playerSkills);
		}
		rs.close();
		ps.close();
		return alPlayerSkills.toArray(new PlayerSkills[alPlayerSkills.size()]);
	}

	public List<PlayerSkills> getPlayerSkillsWithoutTrainingId() throws SQLException {
		List<PlayerSkills> alPlayerSkills = new ArrayList<PlayerSkills>();
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM player_skills WHERE id_training_fk is null order by week, day"); 
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			PlayerSkills playerSkills = new PlayerSkillsDto(rs).getPlayerSkills();
			alPlayerSkills.add(playerSkills);
		}
		rs.close();
		ps.close();
		return alPlayerSkills;
	}

	public NtSkills[] getPlayerNtSkills(Player player) throws SQLException {
		List<NtSkills> listNtSkills = new ArrayList<NtSkills>();

		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT * FROM nt_player_skills WHERE id_player_fk = ? order by week, day"); 
		ps.setInt(1, player.getId());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			NtSkills ntSkills = new PlayerNtSkillsDto(rs).getNtSkill();
			listNtSkills.add(ntSkills);
		}
		rs.close();
		ps.close();
		return listNtSkills.toArray(new NtSkills[listNtSkills.size()]);
	}

	public void updatePlayerTrainingId(PlayerSkills skills) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE player_skills SET id_training_fk = ? WHERE id_skill = ?");
		ps.setInt(1, skills.getTrainingId());
		ps.setInt(2, skills.getId());

		ps.executeUpdate();
		ps.close();
	}

	public void updatePlayerSkills(int id, PlayerSkills skills, Training training) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE player_skills SET " + "millis = ?, " + "age = ?, " + "value = ?, " + "salary = ?, " + "form = ?, "
										 + "stamina = ?, " + "pace = ?, " + "technique = ?, " + "passing = ?, " + "keeper = ?, " + "defender = ? ,"
										 + "playmaker = ?, " + "scorer = ?, " + "matches = ?, " + "goals = ?, " + "assists = ?, " + "cards = ?, "
										 + "injurydays = ?, " + "day = ?, " + "week = ?, " + "experience = ?, " + "teamwork = ?, " + "discipline = ? "
										 + "WHERE id_player_fk = ? AND id_training_fk = ?");

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
		PreparedStatement ps = connection
			.prepareStatement("UPDATE player_skills p SET "
							  + "millis = ?, "
							  + "age = ?, "
							  + "value = ?, "
							  + "salary = ?, "
							  + "form = ?, "
							  + "stamina = ?, "
							  + "pace = ?, "
							  + "technique = ?, "
							  + "passing = ?, "
							  + "keeper = ?, "
							  + "defender = ? ,"
							  + "playmaker = ?, "
							  + "scorer = ?, "
							  + "matches = ?, "
							  + "goals = ?, "
							  + "assists = ?, "
							  + "cards = ?, "
							  + "injurydays = ?, "
							  + "day = ?, "
							  + "week = ?, "
							  + "experience = ?, "
							  + "teamwork = ?, "
							  + "discipline = ? "
							  + "WHERE id_player_fk = ? AND week = (select max(week) from player_skills WHERE id_player_fk = ?) AND day = (select max(day) from player_skills where week = p.week AND id_player_fk = ?)");

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
		PreparedStatement pstm = connection.prepareStatement("UPDATE player SET transfer_list = ?, national = ? WHERE id_player = ?");
		pstm.setInt(1, player.getTransferList());
		pstm.setInt(2, player.getNational());
		pstm.setInt(3, player.getId());
		pstm.executeUpdate();
		pstm.close();
	}

	public void addNtPlayerSkills(int id, NtSkills skills, Date date) throws SQLException {
		PreparedStatement ps = connection
			.prepareStatement("INSERT INTO nt_player_skills (id_player_fk,millis,nt_matches,nt_goals,nt_assists,nt_cards,day,week) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
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
		PreparedStatement ps = connection
			.prepareStatement("select nt_matches from nt_player_skills t where week = (select max(week) from nt_player_skills) AND day = (select max(day) from nt_player_skills where week = t.week) AND id_player_fk = ?"); 
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
		PreparedStatement ps = connection.prepareStatement("select day, week from player_skills t where week = (select max(week) from player_skills) and day = (select max(day) from player_skills where week = t.week)"); 
		SokkerDate sokkerDate = null;
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
		PreparedStatement ps = connection.prepareStatement("select * from player_skills where ((day >= 5 and week = ?) or (day < 5 and week = ?)) and id_player_fk = ?"); 
		// SokkerDate sokkerDate = null;
		PlayerSkills playerSkills = null;
		int week = training.getDate().getSokkerDate().getWeek();
		int day = training.getDate().getSokkerDate().getDay();
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
		String deletedPlayers = ""; 
		PreparedStatement ps = connection.prepareStatement("SELECT id_player FROM player WHERE status = 0 AND id_player NOT IN " + sTemp);
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
		String deletedPlayers = ""; 
		PreparedStatement ps = connection.prepareStatement("SELECT id_player FROM player WHERE status = 0 "); 
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
		PreparedStatement ps = connection.prepareStatement("UPDATE player SET status = ? WHERE id_player = ?"); 
		ps.setInt(1, status);
		ps.setInt(2, id);
		ps.executeUpdate();
		ps.close();

	}

	public boolean existsPlayerHistory(int pid) throws SQLException {

		PreparedStatement ps = connection.prepareStatement("SELECT count(id_player) FROM player WHERE id_player = ? AND status > 0"); 
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

		PreparedStatement ps = connection.prepareStatement("SELECT id_player FROM player WHERE name = ? AND surname = ?"); 
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

	public List<Long> getTrainingDates(int playerId) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT millis FROM player_skills WHERE id_player_fk = ?"); 
		List<Long> alMillis = new ArrayList<Long>();
		ps.setInt(1, playerId);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			alMillis.add(rs.getLong(1));
		}

		return alMillis;
	}

	public void updatePlayerBuyPrice(Player player) throws SQLException {
		PreparedStatement pstm = connection.prepareStatement("UPDATE player SET buy_price = ? WHERE id_player = ?"); 
		pstm.setDouble(1, player.getBuyPrice().getDoubleValue());
		pstm.setInt(2, player.getId());
		pstm.executeUpdate();
		pstm.close();
	}

	public void updatePlayerNote(Person player) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE player SET " + "note = ? " + "WHERE id_player = ?");   
		ps.setString(1, player.getNote());
		ps.setLong(2, player.getId());

		ps.executeUpdate();
		ps.close();
	}

	public void updatePlayerPosition(Player player) throws SQLException {
		PreparedStatement pstm = connection.prepareStatement("UPDATE player SET id_position = ? WHERE id_player = ?"); 
		pstm.setInt(1, player.getPosition());
		pstm.setInt(2, player.getId());
		pstm.executeUpdate();
		pstm.close();
	}

	public void updatePlayerPassTraining(PlayerSkills playerSkills) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE player_skills SET pass_training = ? WHERE id_skill = ?"); 
		ps.setBoolean(1, playerSkills.isPassTraining());
		ps.setInt(2, playerSkills.getId());

		ps.executeUpdate();
		ps.close();

	}

	public void updatePlayerSoldPrice(Player player) throws SQLException {
		PreparedStatement pstm = connection.prepareStatement("UPDATE player SET sold_price = ? WHERE id_player = ?"); 
		pstm.setDouble(1, player.getSoldPrice().getDoubleValue());
		pstm.setInt(2, player.getId());
		pstm.executeUpdate();
		pstm.close();
	}

}
