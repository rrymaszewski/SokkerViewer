package pl.pronux.sokker.model;

import java.util.List;

public class DatabaseSettings {
	private String username;
	private String password;
	private String name;
	private String server;
	private List<String> types;
	private String type;
	
	public final static String HSQLDB = "HSQLDB"; //$NON-NLS-1$
	public final static String POSTGRESQL = "PostgreSQL"; //$NON-NLS-1$
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
