package pl.pronux.sokker.data.sql.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import pl.pronux.sokker.data.sql.dto.PlayerArchiveDto;
import pl.pronux.sokker.model.Person;
import pl.pronux.sokker.model.PlayerArchive;

public class PlayersArchiveDao {
	private Connection connection;

	public PlayersArchiveDao(Connection connection) {
		this.connection = connection;
	}

	public void addNotExists(int id) throws SQLException {
		PreparedStatement pstm;
		pstm = connection.prepareStatement("INSERT INTO player_archive (player_id,exists_in_sokker) VALUES (?,?)"); //$NON-NLS-1$
		pstm.setInt(1, id);
		pstm.setInt(2, PlayerArchive.EXISTS_IN_SOKKER_FALSE);
		pstm.executeUpdate();
		pstm.close();
	}

	public void addPlayer(PlayerArchive playerArchive) throws SQLException {
		PreparedStatement pstm;
		pstm = connection
				.prepareStatement("INSERT INTO player_archive(player_id,name,surname,country_id, team_id, national, transfer_list, youth_team_id,AGE,VALUE ,WAGE ,CARDS,GOALS ,ASSISTS,MATCHES ,NT_CARDS ,NT_MATCHES,NT_ASSISTS ,NT_GOALS ,INJURY_DAYS ,SKILL_FORM ,SKILL_EXPERIENCE ,SKILL_TEAMWORK ,SKILL_DISCIPLINE ,EXISTS_IN_SOKKER ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"); //$NON-NLS-1$

		pstm.setInt(1, playerArchive.getId());
		pstm.setString(2, playerArchive.getName());
		pstm.setString(3, playerArchive.getSurname());
		pstm.setInt(4, playerArchive.getCountryID());
		pstm.setInt(5, playerArchive.getTeamID());
		pstm.setInt(6, playerArchive.getNational());
		pstm.setInt(7, playerArchive.getTransferList());
		pstm.setInt(8, playerArchive.getYouthTeamID());
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
		pstm.executeUpdate();
		pstm.close();

	}

	public HashMap<Integer, PlayerArchive> getPlayers() throws SQLException {
		HashMap<Integer, PlayerArchive> hsPlayer = new HashMap<Integer, PlayerArchive>();
		PlayerArchive player;
		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT player_id,name,surname,country_id,exists_in_sokker,note,youth_team_id FROM player_archive"); //$NON-NLS-1$
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			player = new PlayerArchiveDto(rs).getPlayerArchive();
			hsPlayer.put(player.getId(), player);
		}
		rs.close();
		ps.close();
		return hsPlayer;
	}

	public PlayerArchive getPlayerArchive(int playerID) throws SQLException {
		PlayerArchive player = null;
		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT player_id,name,surname,country_id,exists_in_sokker,note,youth_team_id FROM player_archive WHERE player_id = ?"); //$NON-NLS-1$
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
		String note = ""; //$NON-NLS-1$
		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT note FROM player_archive WHERE player_id = ?"); //$NON-NLS-1$
		ps.setInt(1, playerID);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			note = rs.getString("note"); //$NON-NLS-1$
		}
		rs.close();
		ps.close();
		return note;
	}

	public ArrayList<Integer> getIdPlayersNotInArchive(int limit) throws SQLException {
		if (limit < 0) {
			limit = 0;
		}

		ArrayList<Integer> alPlayersId = new ArrayList<Integer>();
		Integer integer;
		PreparedStatement ps;
		ps = connection.prepareStatement("select distinct player_id from players_stats where player_id not in (select player_id from player_archive) order by player_id limit ?"); //$NON-NLS-1$
		ps.setInt(1, limit);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			integer = rs.getInt(1);
			alPlayersId.add(integer);
		}
		rs.close();
		ps.close();
		return alPlayersId;
	}

	public void updatePlayerArchiveNote(Person playerArchive) throws SQLException {
		PreparedStatement ps;
		ps = connection.prepareStatement("UPDATE player_archive SET note = ? WHERE player_id = ?"); //$NON-NLS-1$

		ps.setString(1, playerArchive.getNote());
		ps.setLong(2, playerArchive.getId());

		ps.executeUpdate();
		ps.close();
	}

	public void updatePlayerArchive(PlayerArchive playerArchive) throws SQLException {
		PreparedStatement ps;
		ps = connection.prepareStatement("UPDATE player_archive SET name = ?, surname = ?, youth_team_id = ?, country_id = ?, exists_in_sokker = ? WHERE player_id = ?"); //$NON-NLS-1$

		ps.setString(1, playerArchive.getName());
		ps.setString(2, playerArchive.getSurname());
		ps.setInt(3, playerArchive.getYouthTeamID());
		ps.setInt(4, playerArchive.getCountryID());
		ps.setInt(5, playerArchive.getExistsInSokker());
		ps.setLong(6, playerArchive.getId());

		ps.executeUpdate();
		ps.close();

	}
}
