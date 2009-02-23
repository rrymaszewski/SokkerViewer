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
		this.setTeamID(rs.getInt("team_id")); //$NON-NLS-1$
		this.setPoints(rs.getInt("points")); //$NON-NLS-1$
		this.setWins(rs.getInt("wins")); //$NON-NLS-1$
		this.setDraws(rs.getInt("draws")); //$NON-NLS-1$
		this.setLosses(rs.getInt("losses")); //$NON-NLS-1$
		this.setGoalsScored(rs.getInt("goals_scored")); //$NON-NLS-1$
		this.setGoalsLost(rs.getInt("goals_lost")); //$NON-NLS-1$
		this.setRankTotal(rs.getString("rank_total")); //$NON-NLS-1$
		this.setTeamName(rs.getString("team_name")); //$NON-NLS-1$
		return this;
	}
}
