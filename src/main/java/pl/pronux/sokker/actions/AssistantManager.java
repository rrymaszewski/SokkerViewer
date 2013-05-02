package pl.pronux.sokker.actions;

import java.sql.SQLException;

import pl.pronux.sokker.data.sql.SQLQuery;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.data.sql.dao.AssistantDao;

public class AssistantManager {

	private static AssistantManager instance = new AssistantManager();

	private AssistantManager() {
	}

	public static AssistantManager instance() {
		return instance;
	}

	public int[][] getAssistantData() throws SQLException {
		boolean newConnection = SQLQuery.connect();
		AssistantDao assistantDao = new AssistantDao(SQLSession.getConnection());
		int[][] table = assistantDao.getAssistantData();
		SQLQuery.close(newConnection);
		return table;
	}

}
