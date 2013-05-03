package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.Player;

public class PlayerDto extends Player {

	private static final long serialVersionUID = -2410891080024790234L;

	private ResultSet rs;

	public PlayerDto(ResultSet rs) {
		this.rs = rs;
	}

	public Player getPlayer() throws SQLException {
		this.setId(rs.getInt("id_player"));
		this.setName(rs.getString("name"));
		this.setSurname(rs.getString("surname"));
		this.setCountryfrom(rs.getInt("countryfrom"));
		this.setJuniorId(rs.getInt("id_junior_fk"));
		this.setStatus(rs.getInt("status"));
		this.setNote(rs.getString("note"));
		this.setPosition(rs.getInt("id_position"));
		this.setSoldPrice(rs.getDouble("sold_price"));
		this.setBuyPrice(rs.getDouble("buy_price"));
		this.setTeamId(rs.getInt("id_club_fk"));
		this.setTransferList(rs.getInt("transfer_list"));
		this.setNational(rs.getInt("national"));
		this.setYouthTeamId(rs.getInt("youth_team_id"));
		this.setHeight(rs.getInt("height"));
		return this;
	}

	public int getPlayerID() throws SQLException {
		return rs.getInt("id_player");
	}
}
