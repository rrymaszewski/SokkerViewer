package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.Player;

public class PlayerBaseDto extends Player {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9144189004823636939L;
	private ResultSet rs;

	public PlayerBaseDto(ResultSet rs) {
		this.rs = rs;
	}
	
	public Player getPlayer() throws SQLException {
		this.setName(rs.getString("name")); 
		this.setSurname(rs.getString("surname")); 
		this.setCountryfrom(rs.getInt("countryfrom")); 
		return this;
	}
}
