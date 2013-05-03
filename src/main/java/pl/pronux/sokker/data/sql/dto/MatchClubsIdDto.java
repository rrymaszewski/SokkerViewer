package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MatchClubsIdDto {
	private ResultSet rs;

	public MatchClubsIdDto(ResultSet rs) {
		this.rs = rs;
	}
	
	public Integer getId() throws SQLException {
		return rs.getInt("team_id"); 
	}
}
