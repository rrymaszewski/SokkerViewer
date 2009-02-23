package pl.pronux.sokker.model;

public class DbProperties {
	private int dbVersion;

	private Date lastModification;

	private boolean checkCountries;

	private boolean checkDbUpdate;

	private boolean repairDB;
	
	private int scanCounter;

	public int getScanCounter() {
		return scanCounter;
	}

	public void setScanCounter(int scanCounter) {
		this.scanCounter = scanCounter;
	}

	public boolean isCheckDbUpdate() {
		return checkDbUpdate;
	}

	public void setCheckDbUpdate(boolean checkDbUpdate) {
		this.checkDbUpdate = checkDbUpdate;
	}

	public boolean isCheckCountries() {
		return checkCountries;
	}

	public void setCheckCountries(boolean checkCountries) {
		this.checkCountries = checkCountries;
	}

	public int getDbVersion() {
		return dbVersion;
	}

	public void setDbVersion(int dbVersion) {
		this.dbVersion = dbVersion;
	}

	public Date getLastModification() {
		return lastModification;
	}

	public void setLastModification(Date lastModification) {
		this.lastModification = lastModification;
	}

	public boolean isRepairDB() {
		return repairDB;
	}

	public void setRepairDB(boolean repairDB) {
		this.repairDB = repairDB;
	}

}
