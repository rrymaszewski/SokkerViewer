package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.model.Training;

public class TrainingDto extends Training {
	private ResultSet rs;
	public TrainingDto(ResultSet rs) {
		this.rs = rs;
	}

	public Training getTraining() throws SQLException {
		this.setId(rs.getInt("id_training")); 
		this.setDate(new Date(rs.getLong("millis"))); 
		this.setType(rs.getInt("type")); 
		this.setFormation(rs.getInt("formation")); 
		this.setNote(rs.getString("note")); 
		this.getDate().setSokkerDate(new SokkerDate(rs.getInt("day"), rs.getInt("week")));  
		this.setReported(rs.getBoolean("reported")); 
		return this;
	}
}
