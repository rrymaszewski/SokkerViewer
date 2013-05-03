package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.ClubSupporters;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.SokkerDate;

public class ClubFanclubDto extends ClubSupporters {
	private ResultSet rs;

	public ClubFanclubDto(ResultSet rs) {
		this.rs = rs;
	}
	
	public ClubSupporters getClubFanclub() throws SQLException {
		this.setId(rs.getInt("id_data")); 
		this.setDate(new Date(rs.getLong("millis"))); 
		this.setFanclubcount(rs.getInt("fanclubcount")); 
		this.setFanclubmood(rs.getByte("fanclubmood")); 
		this.getDate().setSokkerDate(new SokkerDate(rs.getInt("day"), rs.getInt("week")));  
		return this;
	}
}
