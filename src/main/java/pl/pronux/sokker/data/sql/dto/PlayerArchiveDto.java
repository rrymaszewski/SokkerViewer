package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.PlayerArchive;

public class PlayerArchiveDto extends PlayerArchive {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6345599353096070122L;
	
	private ResultSet rs;

	public PlayerArchiveDto(ResultSet rs) {
		this.rs = rs;
	}
	
	public PlayerArchive getPlayerArchive() throws SQLException {
		this.setId(rs.getInt("player_id")); 
		this.setName(rs.getString("name")); 
		this.setSurname(rs.getString("surname")); 
		this.setCountryId(rs.getInt("country_id")); 
		this.setYouthTeamId(rs.getInt("youth_team_id")); 
		this.setNote(rs.getString("note")); 
		this.setExistsInSokker(rs.getInt("exists_in_sokker")); 
		return this;
	}
	
	
}
