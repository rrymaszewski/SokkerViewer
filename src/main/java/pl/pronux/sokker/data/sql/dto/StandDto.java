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
		this.setLocation(rs.getInt("location")); //$NON-NLS-1$
		this.setCapacity(rs.getInt("capacity")); //$NON-NLS-1$
		this.setType(rs.getInt("type")); //$NON-NLS-1$
		this.setIsRoof(rs.getInt("roof")); //$NON-NLS-1$
		this.setConstructionDays(rs.getDouble("days")); //$NON-NLS-1$
		return this;
	}
}
