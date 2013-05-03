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
		this.setCountryId(rs.getInt("country_id")); 
		this.setDivision(rs.getInt("division")); 
		this.setIsCup(rs.getInt("is_cup")); 
		this.setLeagueId(rs.getInt("league_id")); 
		this.setName(rs.getString("name")); 
		this.setIsOfficial(rs.getInt("is_official")); 
		this.setUserId(rs.getInt("user_id")); 
		this.setType(rs.getInt("type")); 
		return this;
	}
}
