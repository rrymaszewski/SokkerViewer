package pl.pronux.sokker.data.sql.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationDao {

	private Connection connection;

	public ConfigurationDao(Connection connection) {
		this.connection = connection;
	}

	public void setValues(Map<String, String> values) throws SQLException {
		PreparedStatement pstm = null;
		pstm = connection.prepareStatement("UPDATE configuration SET value = ? WHERE key = ?"); //$NON-NLS-1$
		for (String key : values.keySet()) {
			pstm.setString(1, values.get(key));
			pstm.setString(2, key);
			pstm.executeUpdate();
		}
		pstm.close();
	}
	
	public Map<String, String> getTrainingSettings() throws SQLException {
		Map<String, String> settings = new HashMap<String, String>();
		PreparedStatement ps;

		ps = connection.prepareStatement("SELECT key,value FROM configuration WHERE key LIKE 'TrainingsConfiguration%'"); //$NON-NLS-1$
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			settings.put(rs.getString("key"), rs.getString("value"));
		}
		rs.close();
		ps.close();
		return settings;
	}
}
