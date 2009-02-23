package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.Player;

public class PlayerDto extends Player {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2410891080024790234L;

	private ResultSet rs;

	public PlayerDto(ResultSet rs) {
		this.rs = rs;
	}

	public Player getPlayer() throws SQLException {
		this.setId(rs.getInt("id_player")); //$NON-NLS-1$
		this.setName(rs.getString("name")); //$NON-NLS-1$
		this.setSurname(rs.getString("surname")); //$NON-NLS-1$
		this.setCountryfrom(rs.getInt("countryfrom")); //$NON-NLS-1$
		this.setIdJuniorFK(rs.getInt("id_junior_fk")); //$NON-NLS-1$
		this.setStatus(rs.getInt("status")); //$NON-NLS-1$
		this.setNote(rs.getString("note")); //$NON-NLS-1$
		this.setPosition(rs.getInt("id_position")); //$NON-NLS-1$
		this.setSoldPrice(rs.getDouble("sold_price")); //$NON-NLS-1$
		this.setBuyPrice(rs.getDouble("buy_price")); //$NON-NLS-1$
		this.setTeamID(rs.getInt("id_club_fk")); //$NON-NLS-1$
		this.setTransferList(rs.getInt("transfer_list")); //$NON-NLS-1$
		this.setNational(rs.getInt("national")); //$NON-NLS-1$
		this.setYouthTeamID(rs.getInt("youth_team_id")); //$NON-NLS-1$
		return this;
	}

	public int getPlayerID() throws SQLException {
		return rs.getInt("id_player"); //$NON-NLS-1$
	}
}
