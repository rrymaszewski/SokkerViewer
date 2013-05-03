package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.Country;

public class CountryDto extends Country {
	private ResultSet rs;
	public CountryDto(ResultSet rs) {
		this.rs = rs;
	}
	public Country getCountry() throws SQLException {
		this.setCountryId(rs.getInt("country_id")); 
		this.setName(rs.getString("name")); 
		this.setCurrencyRate(rs.getDouble("currency_rate")); 
		this.setCurrencyName(rs.getString("currency_name")); 
		return this;
	}
}
