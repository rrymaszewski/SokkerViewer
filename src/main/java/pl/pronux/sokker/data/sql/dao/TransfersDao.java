package pl.pronux.sokker.data.sql.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import pl.pronux.sokker.data.sql.dto.TransferDto;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.Transfer;

public class TransfersDao {

	private Connection connection;

	public TransfersDao(Connection connection) {
		this.connection = connection;
	}

	public ArrayList<Integer> getUncompletedTransfers() throws SQLException {
		ArrayList<Integer> alUncompletedTransfers = new ArrayList<Integer>();
		PreparedStatement ps;
		Integer player_id;
		ps = connection.prepareStatement("SELECT player_id FROM transfers where player_id not in (select player_id from player_archive)"); //$NON-NLS-1$

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			player_id = rs.getInt("player_id"); //$NON-NLS-1$
			alUncompletedTransfers.add(player_id);
		}
		rs.close();
		ps.close();
		return alUncompletedTransfers;
	}

	public void clearTransfers() throws SQLException {
		PreparedStatement ps;
		ps = connection.prepareStatement("DELETE FROM transfers"); //$NON-NLS-1$
		ps.executeUpdate();
		ps.close();
	}

	public void addTransfer(Transfer transfer) throws SQLException {
		PreparedStatement pstm;
		pstm = connection
				.prepareStatement("INSERT INTO transfers(id,SELLER_TEAM_ID,BUYER_TEAM_ID,SELLER_TEAM_NAME,BUYER_TEAM_NAME,PLAYER_ID,MILLIS,DAY,WEEK,PRICE,PLAYER_VALUE) VALUES (?, ?, ?,?,?,?,?,?,?,?,?)"); //$NON-NLS-1$
		pstm.setInt(1, transfer.getTransferID());
		pstm.setInt(2, transfer.getSellerTeamID());
		pstm.setInt(3, transfer.getBuyerTeamID());
		pstm.setString(4, transfer.getSellerTeamName());
		pstm.setString(5, transfer.getBuyerTeamName());
		pstm.setInt(6, transfer.getPlayerID());
		pstm.setLong(7, transfer.getDate().getMillis());
		pstm.setInt(8, transfer.getDate().getSokkerDate().getDay());
		pstm.setInt(9, transfer.getDate().getSokkerDate().getWeek());
		pstm.setInt(10, transfer.getPrice().toInt());
		pstm.setInt(11, transfer.getPlayerValue().toInt());
		pstm.executeUpdate();
		pstm.close();

	}

	public boolean existsTransfer(int idTransfer) throws SQLException {

		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT count(id) FROM transfers WHERE id = ?"); //$NON-NLS-1$
		ps.setInt(1, idTransfer);
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

	public ArrayList<Transfer> getTransfers(Club club) throws SQLException {
		ArrayList<Transfer> alTransfers = new ArrayList<Transfer>();
		Transfer transfer;
		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT id,seller_team_id,buyer_team_id,seller_team_name,buyer_team_name,player_id,millis,day,week,price,player_value FROM transfers ORDER BY millis DESC"); //$NON-NLS-1$
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {

			transfer = new TransferDto(rs).getTransfer();

			if (transfer.getSellerTeamID() == club.getId()) {
				transfer.setIsInOut(Transfer.OUT);
			} else {
				transfer.setIsInOut(Transfer.IN);
			}

			alTransfers.add(transfer);
		}
		rs.close();
		ps.close();
		return alTransfers;
	}
}
