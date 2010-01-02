package pl.pronux.sokker.actions;

import java.sql.SQLException;

import pl.pronux.sokker.data.sql.SQLQuery;
import pl.pronux.sokker.data.sql.dao.AssistantDao;

public class AssistantManager {
	private final static AssistantManager _instance = new AssistantManager();
	
	private AssistantManager() {
	}
	
	public static AssistantManager instance() {
		return _instance;
	}
	
	public int[][] getAssistantData() throws SQLException {
		boolean newConnection = SQLQuery.connect();
		int[][] table = AssistantDao.getAssistantData();
		SQLQuery.close(newConnection);
		return table;
	}

}
