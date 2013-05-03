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
		this.setLeagueId(rs.getInt("league_id")); 
		this.setSeason(rs.getInt("season")); 
		this.setRawSeason(rs.getInt("raw_season")); 
//		this.setSeasonRoundID(rs.getInt("season_round_id"));
		return this;
	}
}
