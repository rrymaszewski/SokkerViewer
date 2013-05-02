package pl.pronux.sokker.model;

public class Region {
	private int idCountryFK;

	private int regionID;

	private String name;

	private int weather;

	public int getIdCountryFK() {
		return idCountryFK;
	}

	public void setIdCountryFK(int idCountryFk) {
		this.idCountryFK = idCountryFk;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRegionID() {
		return regionID;
	}

	public void setRegionID(int regionID) {
		this.regionID = regionID;
	}

	public int getWeather() {
		return weather;
	}

	public void setWeather(int weather) {
		this.weather = weather;
	}
}
