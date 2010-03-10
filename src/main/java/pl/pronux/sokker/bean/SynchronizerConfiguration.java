package pl.pronux.sokker.bean;


public class SynchronizerConfiguration {

	private boolean downloadBase = false;
	private boolean downloadCountries = false;
	private boolean repairDb = false;
	private boolean repairDbJuniorsAge = false;
	
	public boolean isDownloadBase() {
		return downloadBase;
	}
	
	public void setDownloadBase(boolean downloadBase) {
		this.downloadBase = downloadBase;
	}
	
	public boolean isDownloadCountries() {
		return downloadCountries;
	}
	
	public void setDownloadCountries(boolean downloadCountries) {
		this.downloadCountries = downloadCountries;
	}
	
	public boolean isRepairDb() {
		return repairDb;
	}
	
	public void setRepairDb(boolean repairDb) {
		this.repairDb = repairDb;
	}
	
	public void checkDownloadAll() {
		this.downloadBase = true;
		this.downloadCountries = true;
	}
	
	public boolean isDownloadAll() {
		return isDownloadBase() && isDownloadCountries();
	}

	
	public boolean isRepairDbJuniorsAge() {
		return repairDbJuniorsAge;
	}

	
	public void setRepairDbJuniorsAge(boolean repairDbJuniorsAge) {
		this.repairDbJuniorsAge = repairDbJuniorsAge;
	}
}
