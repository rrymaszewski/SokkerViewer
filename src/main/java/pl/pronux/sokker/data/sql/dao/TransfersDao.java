
package pl.pronux.sokker.data.sql.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pl.pronux.sokker.data.sql.dto.TransferDto;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.Transfer;

public class TransfersDao {

	private Connection connection;

	public TransfersDao(Connection connection) {
		this.connection = connection;
	}

	public List<Integer> getUncompletedTransfers() throws SQLException {
		List<Integer> uncompletedTransfers = new ArrayList<Integer>();
		PreparedStatement ps = connection.prepareStatement("SELECT player_id FROM transfers where player_id not in (select player_id from player_archive)"); 

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Integer playerId = rs.getInt("player_id"); 
			uncompletedTransfers.add(playerId);
		}
		rs.close();
		ps.close();
		return uncompletedTransfers;
	}

	public void clearTransfers() throws SQLException {
		PreparedStatement ps = connection.prepareStatement("DELETE FROM transfers"); 
		ps.executeUpdate();
		ps.close();
	}

	public void addTransfer(Transfer transfer) throws SQLException {
		PreparedStatement pstm = connection
				.prepareStatement("INSERT INTO transfers(id,SELLER_TEAM_ID,BUYER_TEAM_ID,SELLER_TEAM_NAME,BUYER_TEAM_NAME,PLAYER_ID,MILLIS,DAY,WEEK,PRICE,PLAYER_VALUE) VALUES (?, ?, ?,?,?,?,?,?,?,?,?)"); 
		pstm.setInt(1, transfer.getTransferId());
		pstm.setInt(2, transfer.getSellerTeamId());
		pstm.setInt(3, transfer.getBuyerTeamId());
		pstm.setString(4, transfer.getSellerTeamName());
		pstm.setString(5, transfer.getBuyerTeamName());
		pstm.setInt(6, transfer.getPlayerId());
		pstm.setLong(7, transfer.getDate().getMillis());
		pstm.setInt(8, transfer.getDate().getSokkerDate().getDay());
		pstm.setInt(9, transfer.getDate().getSokkerDate().getWeek());
		pstm.setInt(10, transfer.getPrice().toInt());
		pstm.setInt(11, transfer.getPlayerValue().toInt());
		pstm.executeUpdate();
		pstm.close();

	}

	public boolean existsTransfer(int transferId) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT count(id) FROM transfers WHERE id = ?"); 
		ps.setInt(1, transferId);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			if (rs.getInt(1) > 0) {
				rs.close();
				ps.close();
				return true;
			} else {
				rs.close();
				ps.close();
				return false;
			}
		}

		return false;
	}

	public List<Transfer> getTransfers(Club club) throws SQLException {
		List<Transfer> transfers = new ArrayList<Transfer>();
		PreparedStatement ps = connection.prepareStatement("SELECT id,seller_team_id,buyer_team_id,seller_team_name,buyer_team_name,player_id,millis,day,week,price,player_value FROM transfers ORDER BY millis DESC"); 
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {

			Transfer transfer = new TransferDto(rs).getTransfer();
			if (transfer.getSellerTeamId() == club.getId()) {
				transfer.setIsInOut(Transfer.OUT);
			} else {
				transfer.setIsInOut(Transfer.IN);
			}

			transfers.add(transfer);
		}
		rs.close();
		ps.close();
		return transfers;
	}
}
