package pl.pronux.sokker.data.properties.dao;

import pl.pronux.sokker.data.properties.PropertiesSession;

public class LoginsMapDao {
	private PropertiesSession properties;

	public LoginsMapDao(PropertiesSession properties) {
		this.properties = properties;
	}
	
	public String getTeamID(String username) {
		return properties.getProperty(username);
	}
	
	public void addTeamID(String username, String teamID) {
		properties.setProperty(username, teamID);
		
	}
}
