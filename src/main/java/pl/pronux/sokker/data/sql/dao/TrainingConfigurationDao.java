package pl.pronux.sokker.data.sql.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TrainingConfigurationDao {

	private Connection connection;

	public TrainingConfigurationDao(Connection connection) {
		this.connection = connection;
	}

	public void setValues(Map<Integer, Integer[][]> values) throws SQLException {
		PreparedStatement pstm = null;
		pstm = connection.prepareStatement("UPDATE configuration_trainings SET GK = ?, DEF = ?, MID = ?, ATT = ? WHERE TYPE = ? AND FORMATION = ?"); //$NON-NLS-1
		for (Integer key : values.keySet()) {
			for(int i = 0 ; i < values.get(key).length; i++) {
				pstm.setInt(1, values.get(key)[i][0]);
				pstm.setInt(2, values.get(key)[i][1]);
				pstm.setInt(3, values.get(key)[i][2]);
				pstm.setInt(4, values.get(key)[i][3]);
				pstm.setInt(5, key);
				pstm.setInt(6, i);
				pstm.executeUpdate();
			}
		}
		pstm.close();		
	}
	
	public Map<Integer, Integer[][]> getValues() throws SQLException {
		Map<Integer, Integer[][]> values = new HashMap<Integer, Integer[][]>();
		PreparedStatement ps;
		Integer[][] table = new Integer[4][4];
		int type = -1;
		int formation = 0;
		ps = connection.prepareStatement("SELECT * FROM configuration_trainings ORDER BY type, formation"); //$NON-NLS-1$
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			if(type != rs.getInt("type")) {
				type = rs.getInt("type");
				table = new Integer[4][4];
				values.put(type, table);
			}
			formation = rs.getInt("formation");
			values.get(type)[formation][0] = rs.getInt("gk");
			values.get(type)[formation][1] = rs.getInt("def");
			values.get(type)[formation][2] = rs.getInt("mid");
			values.get(type)[formation][3] = rs.getInt("att");
		}
		rs.close();
		ps.close();
		return values;
	}

}
