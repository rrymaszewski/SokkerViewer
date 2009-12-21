package pl.pronux.sokker.model;


public class SokkerViewerSettings {

	private boolean savePassword;
	private String backupDirectory;
	private boolean startup;
	private String username;
	private String password;
	private String baseDirectory;
	private boolean checkProperties;
	private String langCode;
	private boolean update;
	private boolean infoClose;
	private boolean infoUpdate;
	private ProxySettings proxySettings;
	private DatabaseSettings databaseSettings;

	public boolean isSavePassword() {
		return savePassword;
	}

	public void setSavePassword(boolean savePassword) {
		this.savePassword = savePassword;
	}

	public String getBackupDirectory() {
		return backupDirectory;
	}

	public void setBackupDirectory(String backupDirectory) {
		this.backupDirectory = backupDirectory;
	}

	public boolean isStartup() {
		return startup;
	}

	public void setStartup(boolean startup) {
		this.startup = startup;
	}

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

	public String getBaseDirectory() {
		return baseDirectory;
	}

	public void setBaseDirectory(String baseDirectory) {
		this.baseDirectory = baseDirectory;
	}

	public boolean isCheckProperties() {
		return checkProperties;
	}

	public void setCheckProperties(boolean checkProperties) {
		this.checkProperties = checkProperties;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public boolean isInfoClose() {
		return infoClose;
	}

	public void setInfoClose(boolean infoClose) {
		this.infoClose = infoClose;
	}

	public boolean isInfoUpdate() {
		return infoUpdate;
	}

	public void setInfoUpdate(boolean infoUpdate) {
		this.infoUpdate = infoUpdate;
	}

	public ProxySettings getProxySettings() {
		return proxySettings;
	}

	public void setProxySettings(ProxySettings proxySettings) {
		this.proxySettings = proxySettings;
	}

	public DatabaseSettings getDatabaseSettings() {
		return databaseSettings;
	}

	public void setDatabaseSettings(DatabaseSettings databaseSettings) {
		this.databaseSettings = databaseSettings;
	}
}
