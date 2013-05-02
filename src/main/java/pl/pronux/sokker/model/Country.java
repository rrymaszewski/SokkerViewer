package pl.pronux.sokker.model;

public class Country {

	private int countryID;
	private String name;
	private String currencyName;
	private Double currencyRate;

	public int getCountryID() {
		return countryID;
	}

	public void setCountryID(int countryID) {
		this.countryID = countryID;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public Double getCurrencyRate() {
		return currencyRate;
	}

	public void setCurrencyRate(Double currencyRate) {
		this.currencyRate = currencyRate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
