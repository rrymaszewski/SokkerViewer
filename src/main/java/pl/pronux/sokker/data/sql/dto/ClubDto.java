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
	this.setId(rs.getInt("id")); 
	this.setCountry(rs.getInt("country")); 
	this.setRegionId(rs.getInt("region")); 
	this.setImagePath(rs.getString("image_path")); 
	this.setDateCreated(new Date(rs.getLong("date_created"))); 
	this.setJuniorsMax(rs.getInt("juniors_max")); 
	return this;
}
}
