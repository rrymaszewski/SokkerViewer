package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.LeagueRound;

public class LeagueRoundDto extends LeagueRound {
	private ResultSet rs;

	public LeagueRoundDto(ResultSet rs){ 
		this.rs = rs;
	}
	
	public LeagueRound getSeasonRound() throws SQLException {
		this.setRound(rs.getInt("round")); //$NON-NLS-1$
		return this;
	}
}
