package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.Stand;

public class StandDto extends Stand {
	private ResultSet rs;

	public StandDto(ResultSet rs) {
		this.rs = rs;
	}
	
	public Stand getStand() throws SQLException {
		this.setLocation(rs.getInt("location")); 
		this.setCapacity(rs.getInt("capacity")); 
		this.setType(rs.getInt("type")); 
		this.setIsRoof(rs.getInt("roof")); 
		this.setConstructionDays(rs.getDouble("days")); 
		return this;
	}
}
