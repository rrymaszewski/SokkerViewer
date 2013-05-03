package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.Region;

public class RegionDto extends Region {
private ResultSet rs;

public RegionDto(ResultSet rs) {
	this.rs = rs;
}

public Region getRegion() throws SQLException {
	this.setName(rs.getString("name")); 
	this.setRegionId(rs.getInt("region_id")); 
	this.setWeather(rs.getInt("weather")); 
	this.setCountryId(rs.getInt("country_id")); 
	return this;
}

}
