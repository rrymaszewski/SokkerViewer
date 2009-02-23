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
		this.setCountryID(rs.getInt("country_id")); //$NON-NLS-1$
		this.setName(rs.getString("name")); //$NON-NLS-1$
		this.setCurrencyRate(rs.getDouble("currency_rate")); //$NON-NLS-1$
		this.setCurrencyName(rs.getString("currency_name")); //$NON-NLS-1$
		return this;
	}
}
