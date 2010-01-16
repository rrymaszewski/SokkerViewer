package pl.pronux.sokker.data.sql.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AssistantDao {

	private Connection connection;

	public AssistantDao(Connection connection) {
		this.connection = connection;
	}

	public int[][] getAssistantData() throws SQLException {
		ArrayList<int[]> alInt = new ArrayList<int[]>();
		PreparedStatement ps;
		ps = connection
			.prepareStatement("SELECT id_position, form, stamina, pace, technique, passing, keeper, defender, playmaker, scorer FROM assistant ORDER BY id_position"); //$NON-NLS-1$
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			alInt.add(new int[] { rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9),
								 rs.getInt(10) });
		}
		rs.close();
		ps.close();

		return alInt.toArray(new int[alInt.size()][10]);
	}

	public void updateAssistantData(int[][] data) throws SQLException {
		PreparedStatement pstm;

		for (int i = 0; i < data.length; i++) {
			pstm = connection
				.prepareStatement("UPDATE assistant SET " + "form = ?, " + "stamina = ?, " + "pace = ?, " + "technique = ?, " + "passing = ?," + "keeper = ?, " + "defender = ?, " + "playmaker = ?, " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
								  + "scorer = ? " + "WHERE id_position = ?"); //$NON-NLS-1$ //$NON-NLS-2$
			int c = 1;
			int j = 1;

			pstm.setInt(c++, data[i][j++]);
			pstm.setInt(c++, data[i][j++]);
			pstm.setInt(c++, data[i][j++]);
			pstm.setInt(c++, data[i][j++]);
			pstm.setInt(c++, data[i][j++]);
			pstm.setInt(c++, data[i][j++]);
			pstm.setInt(c++, data[i][j++]);
			pstm.setInt(c++, data[i][j++]);
			pstm.setInt(c++, data[i][j++]);
			pstm.setInt(c++, data[i][0]);

			pstm.executeUpdate();
			pstm.close();
		}
	}
}