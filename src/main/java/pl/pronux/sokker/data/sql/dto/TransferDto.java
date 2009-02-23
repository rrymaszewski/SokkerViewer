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
		this.setTransferID(rs.getInt("id")); //$NON-NLS-1$
		this.setSellerTeamID(rs.getInt("seller_team_id")); //$NON-NLS-1$
		this.setBuyerTeamID(rs.getInt("buyer_team_id")); //$NON-NLS-1$
		this.setSellerTeamName(rs.getString("seller_team_name")); //$NON-NLS-1$
		this.setBuyerTeamName(rs.getString("buyer_team_name")); //$NON-NLS-1$
		this.setPlayerID(rs.getInt("player_id")); //$NON-NLS-1$
		this.setDate(new Date(rs.getLong("millis"))); //$NON-NLS-1$
		this.getDate().setSokkerDate(new SokkerDate(rs.getInt("day"), rs.getInt("week"))); //$NON-NLS-1$ //$NON-NLS-2$
		this.setPrice(new Money(rs.getInt("price"))); //$NON-NLS-1$
		this.setPlayerValue(new Money(rs.getInt("player_value"))); //$NON-NLS-1$

		return this;
	}
}
