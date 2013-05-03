package pl.pronux.sokker.data.sql.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.pronux.sokker.data.sql.dto.PlayerArchiveDto;
import pl.pronux.sokker.model.Person;
import pl.pronux.sokker.model.PlayerArchive;

public class PlayersArchiveDao {
	private Connection connection;

	public PlayersArchiveDao(Connection connection) {
		this.connection = connection;
	}

	public void addNotExists(int id) throws SQLException {
		PreparedStatement pstm = connection.prepareStatement("INSERT INTO player_archive (player_id,exists_in_sokker) VALUES (?,?)"); 
		pstm.setInt(1, id);
		pstm.setInt(2, PlayerArchive.EXISTS_IN_SOKKER_FALSE);
		pstm.executeUpdate();
		pstm.close();
	}

	public void addPlayer(PlayerArchive playerArchive) throws SQLException {
		PreparedStatement pstm = connection
				.prepareStatement("INSERT INTO player_archive(player_id,name,surname,country_id, team_id, national, transfer_list, youth_team_id,age,value ,wage ,cards,goals ,assists,matches ,nt_cards ,nt_matches,nt_assists ,nt_goals ,injury_days ,skill_form ,skill_experience ,skill_teamwork ,skill_discipline ,exists_in_sokker, height ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"); 

		pstm.setInt(1, playerArchive.getId());
		pstm.setString(2, playerArchive.getName());
		pstm.setString(3, playerArchive.getSurname());
		pstm.setInt(4, playerArchive.getCountryId());
		pstm.setInt(5, playerArchive.getTeamId());
		pstm.setInt(6, playerArchive.getNational());
		pstm.setInt(7, playerArchive.getTransferList());
		pstm.setInt(8, playerArchive.getYouthTeamId());
		pstm.setInt(9, playerArchive.getAge());
		pstm.setInt(10, playerArchive.getValue().toInt());
		pstm.setInt(11, playerArchive.getWage().toInt());
		pstm.setInt(12, playerArchive.getCards());
		pstm.setInt(13, playerArchive.getGoals());
		pstm.setInt(14, playerArchive.getAssists());
		pstm.setInt(15, playerArchive.getMatches());
		pstm.setInt(16, playerArchive.getNtCards());
		pstm.setInt(17, playerArchive.getNtMatches());
		pstm.setInt(18, playerArchive.getNtAssists());
		pstm.setInt(19, playerArchive.getNtGoals());
		pstm.setDouble(20, playerArchive.getInjuryDays());
		pstm.setInt(21, playerArchive.getSkillForm());
		pstm.setInt(22, playerArchive.getSkillExperience());
		pstm.setInt(23, playerArchive.getSkillTeamwork());
		pstm.setInt(24, playerArchive.getSkillDiscipline());
		pstm.setInt(25, PlayerArchive.EXISTS_IN_SOKKER_TRUE);
		pstm.setInt(26, playerArchive.getHeight());
		pstm.executeUpdate();
		pstm.close();

	}

	public Map<Integer, PlayerArchive> getPlayers() throws SQLException {
		Map<Integer, PlayerArchive> hsPlayer = new HashMap<Integer, PlayerArchive>();
		
		PreparedStatement ps = connection.prepareStatement("SELECT player_id,name,surname,country_id,exists_in_sokker,note,youth_team_id FROM player_archive"); 
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			PlayerArchive player = new PlayerArchiveDto(rs).getPlayerArchive();
			hsPlayer.put(player.getId(), player);
		}
		rs.close();
		ps.close();
		return hsPlayer;
	}

	public PlayerArchive getPlayerArchive(int playerID) throws SQLException {
		PlayerArchive player = null;
		PreparedStatement ps = connection.prepareStatement("SELECT player_id,name,surname,country_id,exists_in_sokker,note,youth_team_id FROM player_archive WHERE player_id = ?"); 
		ps.setInt(1, playerID);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			player = new PlayerArchiveDto(rs).getPlayerArchive();
		}
		rs.close();
		ps.close();
		return player;
	}

	public String getPlayerArchiveNote(int playerID) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT note FROM player_archive WHERE player_id = ?"); 
		ps.setInt(1, playerID);
		ResultSet rs = ps.executeQuery();
		StringBuilder note = new StringBuilder();
		while (rs.next()) {
			note.append(rs.getString("note")); 
		}
		rs.close();
		ps.close();
		return note.toString();
	}

	public List<Integer> getIdPlayersNotInArchive(int limit) throws SQLException {
		if (limit < 0) {
			limit = 0;
		}
		List<Integer> playersId = new ArrayList<Integer>();
		PreparedStatement ps = connection.prepareStatement("select distinct player_id from players_stats where player_id not in (select player_id from player_archive) and player_id > 0 order by player_id limit ?"); 
		ps.setInt(1, limit);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Integer integer = rs.getInt(1);
			playersId.add(integer);
		}
		rs.close();
		ps.close();
		return playersId;
	}

	public void updatePlayerArchiveNote(Person playerArchive) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE player_archive SET note = ? WHERE player_id = ?"); 

		ps.setString(1, playerArchive.getNote());
		ps.setLong(2, playerArchive.getId());

		ps.executeUpdate();
		ps.close();
	}

	public void updatePlayerArchive(PlayerArchive playerArchive) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE player_archive SET name = ?, surname = ?, youth_team_id = ?, country_id = ?, exists_in_sokker = ?, height = ? WHERE player_id = ?"); 

		ps.setString(1, playerArchive.getName());
		ps.setString(2, playerArchive.getSurname());
		ps.setInt(3, playerArchive.getYouthTeamId());
		ps.setInt(4, playerArchive.getCountryId());
		ps.setInt(5, playerArchive.getExistsInSokker());
		ps.setLong(6, playerArchive.getId());
		ps.setInt(7, playerArchive.getHeight());
		ps.executeUpdate();
		ps.close();

	}
}
