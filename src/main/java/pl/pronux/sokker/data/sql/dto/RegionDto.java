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
	this.setName(rs.getString("name")); //$NON-NLS-1$
	this.setRegionID(rs.getInt("region_id")); //$NON-NLS-1$
	this.setWeather(rs.getInt("weather")); //$NON-NLS-1$
	this.setIdCountryFK(rs.getInt("country_id")); //$NON-NLS-1$
	return this;
}

}
