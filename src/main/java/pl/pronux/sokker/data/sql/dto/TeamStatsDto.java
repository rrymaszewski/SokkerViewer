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
		this.setFouls(rs.getInt("fouls")); //$NON-NLS-1$
		this.setOffsides(rs.getInt("offsides")); //$NON-NLS-1$
		this.setRatingDefending(rs.getInt("rating_defending")); //$NON-NLS-1$
		this.setRatingPassing(rs.getInt("rating_passing")); //$NON-NLS-1$
		this.setRatingScoring(rs.getInt("rating_scoring")); //$NON-NLS-1$
		this.setRedCards(rs.getInt("red_cards")); //$NON-NLS-1$
		this.setYellowCards(rs.getInt("yellow_cards")); //$NON-NLS-1$
		this.setShoots(rs.getInt("shoots")); //$NON-NLS-1$
		this.setTacticName(rs.getString("tactic_name")); //$NON-NLS-1$
		this.setTimeOnHalf(rs.getInt("time_on_half")); //$NON-NLS-1$
		this.setTimePossession(rs.getInt("time_possession")); //$NON-NLS-1$
		this.setTeamID(rs.getInt("team_id")); //$NON-NLS-1$
		this.setAverageRating(rs.getDouble("rating_sum") / rs.getInt("players_count")); //$NON-NLS-1$ //$NON-NLS-2$
		return this;
	}
}
