package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.Date;

public class ClubDto extends Club {
private ResultSet rs;

public ClubDto(ResultSet rs) {
	this.rs = rs;
}

public Club getClub() throws SQLException {
	this.setId(rs.getInt("id")); //$NON-NLS-1$
	this.setCountry(rs.getInt("country")); //$NON-NLS-1$
	this.setRegionID(rs.getInt("region")); //$NON-NLS-1$
	this.setImagePath(rs.getString("image_path")); //$NON-NLS-1$
	this.setDateCreated(new Date(rs.getLong("date_created"))); //$NON-NLS-1$
	this.setJuniorsMax(rs.getInt("juniors_max")); //$NON-NLS-1$
	return this;
}
}
