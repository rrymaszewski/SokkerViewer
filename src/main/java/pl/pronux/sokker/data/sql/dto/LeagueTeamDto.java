package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.LeagueTeam;

public class LeagueTeamDto extends LeagueTeam {
	private ResultSet rs;

	public LeagueTeamDto(ResultSet rs) {
		this.rs = rs;
	}

	public LeagueTeam getLeagueTeam() throws SQLException {
		this.setTeamId(rs.getInt("team_id")); 
		this.setPoints(rs.getInt("points")); 
		this.setWins(rs.getInt("wins")); 
		this.setDraws(rs.getInt("draws")); 
		this.setLosses(rs.getInt("losses")); 
		this.setGoalsScored(rs.getInt("goals_scored")); 
		this.setGoalsLost(rs.getInt("goals_lost")); 
		this.setRankTotal(rs.getString("rank_total")); 
		this.setTeamName(rs.getString("team_name")); 
		return this;
	}
}
