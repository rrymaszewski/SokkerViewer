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
		this.setId(rs.getInt("id_training")); //$NON-NLS-1$
		this.setDate(new Date(rs.getLong("millis"))); //$NON-NLS-1$
		this.setType(rs.getInt("type")); //$NON-NLS-1$
		this.setFormation(rs.getInt("formation")); //$NON-NLS-1$
		this.setNote(rs.getString("note")); //$NON-NLS-1$
		this.getDate().setSokkerDate(new SokkerDate(rs.getInt("day"), rs.getInt("week"))); //$NON-NLS-1$ //$NON-NLS-2$
		this.setReported(rs.getBoolean("reported")); //$NON-NLS-1$
		return this;
	}
}
