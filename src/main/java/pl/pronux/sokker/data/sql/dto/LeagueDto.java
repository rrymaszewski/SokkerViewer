package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.League;

public class LeagueDto extends League {
	private ResultSet rs;

	public LeagueDto(ResultSet rs) {
		this.rs = rs;
	}

	public League getLeague() throws SQLException {
		this.setCountryID(rs.getInt("country_id")); //$NON-NLS-1$
		this.setDivision(rs.getInt("division")); //$NON-NLS-1$
		this.setIsCup(rs.getInt("is_cup")); //$NON-NLS-1$
		this.setLeagueID(rs.getInt("league_id")); //$NON-NLS-1$
		this.setName(rs.getString("name")); //$NON-NLS-1$
		this.setIsOfficial(rs.getInt("is_official")); //$NON-NLS-1$
		this.setUserID(rs.getInt("user_id")); //$NON-NLS-1$
		this.setType(rs.getInt("type")); //$NON-NLS-1$
		return this;
	}
}
