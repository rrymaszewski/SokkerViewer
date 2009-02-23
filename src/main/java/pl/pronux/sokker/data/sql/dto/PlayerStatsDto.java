package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerStatsDto extends pl.pronux.sokker.model.PlayerStats {
	private ResultSet rs;

	public PlayerStatsDto(ResultSet rs) {
		this.rs = rs;
	}

	public PlayerStatsDto getPlayerStats() throws SQLException {
		this.setAssists(rs.getInt("assists")); //$NON-NLS-1$
		this.setFormation(rs.getInt("formation")); //$NON-NLS-1$
		this.setFouls(rs.getInt("fouls")); //$NON-NLS-1$
		this.setGoals(rs.getInt("goals")); //$NON-NLS-1$
		this.setIsInjured(rs.getInt("is_injured")); //$NON-NLS-1$
		this.setNumber(rs.getInt("number")); //$NON-NLS-1$
		this.setPlayerID(rs.getInt("player_id")); //$NON-NLS-1$
		this.setRating(rs.getInt("rating")); //$NON-NLS-1$
		this.setRedCards(rs.getInt("red_cards")); //$NON-NLS-1$
		this.setYellowCards(rs.getInt("yellow_cards")); //$NON-NLS-1$
		this.setShoots(rs.getInt("shoots")); //$NON-NLS-1$
		this.setTimeDefending(rs.getInt("time_defending")); //$NON-NLS-1$
		this.setTimeIn(rs.getInt("time_in")); //$NON-NLS-1$
		this.setTimeOut(rs.getInt("time_out")); //$NON-NLS-1$
		this.setTimePlaying(rs.getInt("time_playing")); //$NON-NLS-1$
		this.setInjuryDays(rs.getInt("injury_days")); //$NON-NLS-1$
		this.setMatchID(rs.getInt("match_id")); //$NON-NLS-1$
		this.setTeamID(rs.getInt("team_id")); //$NON-NLS-1$
		return this;
	}
}
