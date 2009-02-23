package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.LeagueSeason;

public class LeagueSeasonDto extends LeagueSeason {

	private ResultSet rs;

	public LeagueSeasonDto(ResultSet rs) {
		this.rs = rs;
	}

	public LeagueSeason getLeagueSeason() throws SQLException {
		this.setLeagueID(rs.getInt("league_id")); //$NON-NLS-1$
		this.setSeason(rs.getInt("season")); //$NON-NLS-1$
		this.setRawSeason(rs.getInt("raw_season")); //$NON-NLS-1$
//		this.setSeasonRoundID(rs.getInt("season_round_id"));
		return this;
	}
}
