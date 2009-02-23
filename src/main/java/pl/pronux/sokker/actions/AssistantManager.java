package pl.pronux.sokker.actions;

import java.sql.SQLException;

import pl.pronux.sokker.data.sql.SQLQuery;
import pl.pronux.sokker.data.sql.dao.AssistantDao;

public class AssistantManager {

	public static int[][] getAssistantData() throws SQLException {
		boolean newConnection = SQLQuery.connect();
		int[][] table = AssistantDao.getAssistantData();
		SQLQuery.close(newConnection);
		return table;
	}

}
