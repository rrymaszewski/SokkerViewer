package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.TeamStats;

public class TeamStatsDto extends TeamStats {
	private ResultSet rs;

	public TeamStatsDto(ResultSet rs) {
		this.rs = rs;
	}

	public TeamStats getTeamStats() throws SQLException {
		this.setFouls(rs.getInt("fouls")); 
		this.setOffsides(rs.getInt("offsides")); 
		this.setRatingDefending(rs.getInt("rating_defending")); 
		this.setRatingPassing(rs.getInt("rating_passing")); 
		this.setRatingScoring(rs.getInt("rating_scoring")); 
		this.setRedCards(rs.getInt("red_cards")); 
		this.setYellowCards(rs.getInt("yellow_cards")); 
		this.setShoots(rs.getInt("shoots")); 
		this.setTacticName(rs.getString("tactic_name")); 
		this.setTimeOnHalf(rs.getInt("time_on_half")); 
		this.setTimePossession(rs.getInt("time_possession")); 
		this.setTeamId(rs.getInt("team_id")); 
		this.setAverageRating(rs.getDouble("rating_sum") / rs.getInt("players_count"));  
		return this;
	}
}
