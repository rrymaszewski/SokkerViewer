package pl.pronux.sokker.model;

public class Country {
//	private static int[] id = {
//			1,
//			2,
//			3,
//			4,
//			5,
//			6,
//			7,
//			8,
//			9,
//			10,
//			11,
//			12,
//			13,
//			14,
//			15,
//			16,
//			17,
//			18,
//			19,
//			20,
//			21,
//			22,
//			23,
//			24,
//			25,
//			26,
//			27,
//			28,
//			29,
//			30,
//			31,
//			32,
//			33,
//			34,
//			35,
//			36,
//			37,
//			38,
//			39,
//			40,
//			41,
//			42,
//			43,
//			44,
//			45,
//			46,
//			47,
//			48,
//			49,
//			50,
//			51,
//			52,
//			53,
//			54,
//			55,
//			56,
//			57,
//			58,
//			59,
//			60,
//			61,
//			62,
//			63,
//			64,
//			65,
//			66,
//			67,
//			68,
//			69,
//			70,
//			71,
//			72
//	};
//
//	public static int[] getCountriesId() {
//		return id;
//	}

	int countryID;
	String name;
	String currencyName;
	Double currencyRate;

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
