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
		this.setAwayTeamID(rs.getInt("away_team_id")); //$NON-NLS-1$
		this.setAwayTeamName(rs.getString("away_team_name")); //$NON-NLS-1$
		this.setAwayTeamScore(rs.getInt("away_team_score")); //$NON-NLS-1$
		this.setDay(rs.getInt("day")); //$NON-NLS-1$
		this.setWeek(rs.getInt("week")); //$NON-NLS-1$
		this.setHomeTeamID(rs.getInt("home_team_id")); //$NON-NLS-1$
		this.setHomeTeamName(rs.getString("home_team_name")); //$NON-NLS-1$
		this.setHomeTeamScore(rs.getInt("home_team_score")); //$NON-NLS-1$
		this.setIsFinished(rs.getInt("is_finished")); //$NON-NLS-1$
		this.setWeather(rs.getInt("weather")); //$NON-NLS-1$
		this.setRound(rs.getInt("round")); //$NON-NLS-1$
		this.setSeason(rs.getInt("season")); //$NON-NLS-1$
		this.setSupporters(rs.getInt("supporters")); //$NON-NLS-1$
		this.setMatchID(rs.getInt("match_id")); //$NON-NLS-1$
		this.setLeagueID(rs.getInt("league_id")); //$NON-NLS-1$
		this.setDateExpected(new Date(rs.getTimestamp("date_expected"))); //$NON-NLS-1$
		this.setDateStarted(new Date(rs.getTimestamp("date_started"))); //$NON-NLS-1$
				
		return this;
	}
}
