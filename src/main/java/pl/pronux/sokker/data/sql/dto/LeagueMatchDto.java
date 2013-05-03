package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Match;

public class LeagueMatchDto extends Match {
	private ResultSet rs;

	public LeagueMatchDto(ResultSet rs) {
		this.rs = rs;
	}

	public Match getMatch() throws SQLException {
		this.setAwayTeamId(rs.getInt("away_team_id")); 
		this.setAwayTeamName(rs.getString("away_team_name")); 
		this.setAwayTeamScore(rs.getInt("away_team_score")); 
		this.setDay(rs.getInt("day")); 
		this.setWeek(rs.getInt("week")); 
		this.setHomeTeamId(rs.getInt("home_team_id")); 
		this.setHomeTeamName(rs.getString("home_team_name")); 
		this.setHomeTeamScore(rs.getInt("home_team_score")); 
		this.setIsFinished(rs.getInt("is_finished")); 
		this.setWeather(rs.getInt("weather")); 
		this.setRound(rs.getInt("round")); 
		this.setSeason(rs.getInt("season")); 
		this.setSupporters(rs.getInt("supporters")); 
		this.setMatchId(rs.getInt("match_id")); 
		this.setLeagueId(rs.getInt("league_id")); 
		this.setDateExpected(new Date(rs.getTimestamp("date_expected"))); 
		this.setDateStarted(new Date(rs.getTimestamp("date_started"))); 
				
		return this;
	}
}
