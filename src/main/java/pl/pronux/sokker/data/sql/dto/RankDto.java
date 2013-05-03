package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Rank;
import pl.pronux.sokker.model.SokkerDate;

public class RankDto extends Rank {
	private ResultSet rs;

	public RankDto(ResultSet rs) {
		this.rs = rs;
	}
	
	public Rank getRankDto() throws SQLException {
		this.setId(rs.getInt("id_data")); 
		this.setRank(rs.getDouble("rank")); 
		this.setDate(new Date(rs.getLong("millis"))); 
		this.getDate().setSokkerDate(new SokkerDate(rs.getInt("day"), rs.getInt("week")));  
		return this;
	}
}
