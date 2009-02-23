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
		this.setId(rs.getInt("player_id")); //$NON-NLS-1$
		this.setName(rs.getString("name")); //$NON-NLS-1$
		this.setSurname(rs.getString("surname")); //$NON-NLS-1$
		this.setCountryID(rs.getInt("country_id")); //$NON-NLS-1$
		this.setYouthTeamID(rs.getInt("youth_team_id")); //$NON-NLS-1$
		this.setNote(rs.getString("note")); //$NON-NLS-1$
		this.setExistsInSokker(rs.getInt("exists_in_sokker")); //$NON-NLS-1$
		return this;
	}
	
	
}
