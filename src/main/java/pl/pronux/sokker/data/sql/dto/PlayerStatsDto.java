package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerStatsDto extends pl.pronux.sokker.model.PlayerStats {
	private ResultSet rs;

	public PlayerStatsDto(ResultSet rs) {
		this.rs = rs;
	}

	public PlayerStatsDto getPlayerStats() throws SQLException {
		this.setAssists(rs.getInt("assists")); 
		this.setFormation(rs.getInt("formation")); 
		this.setFouls(rs.getInt("fouls")); 
		this.setGoals(rs.getInt("goals")); 
		this.setIsInjured(rs.getInt("is_injured")); 
		this.setNumber(rs.getInt("number")); 
		this.setPlayerId(rs.getInt("player_id")); 
		this.setRating(rs.getInt("rating")); 
		this.setRedCards(rs.getInt("red_cards")); 
		this.setYellowCards(rs.getInt("yellow_cards")); 
		this.setShoots(rs.getInt("shoots")); 
		this.setTimeDefending(rs.getInt("time_defending")); 
		this.setTimeIn(rs.getInt("time_in")); 
		this.setTimeOut(rs.getInt("time_out")); 
		this.setTimePlaying(rs.getInt("time_playing")); 
		this.setInjuryDays(rs.getInt("injury_days")); 
		this.setMatchId(rs.getInt("match_id")); 
		this.setTeamId(rs.getInt("team_id")); 
		return this;
	}
}
