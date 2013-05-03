package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.model.Transfer;

public class TransferDto extends Transfer {
	private ResultSet rs;
	public TransferDto(ResultSet rs) {
		this.rs = rs;
	}
	public Transfer getTransfer() throws SQLException {
		this.setTransferId(rs.getInt("id")); 
		this.setSellerTeamId(rs.getInt("seller_team_id")); 
		this.setBuyerTeamId(rs.getInt("buyer_team_id")); 
		this.setSellerTeamName(rs.getString("seller_team_name")); 
		this.setBuyerTeamName(rs.getString("buyer_team_name")); 
		this.setPlayerId(rs.getInt("player_id")); 
		this.setDate(new Date(rs.getLong("millis"))); 
		this.getDate().setSokkerDate(new SokkerDate(rs.getInt("day"), rs.getInt("week")));  
		this.setPrice(new Money(rs.getInt("price"))); 
		this.setPlayerValue(new Money(rs.getInt("player_value"))); 

		return this;
	}
}
