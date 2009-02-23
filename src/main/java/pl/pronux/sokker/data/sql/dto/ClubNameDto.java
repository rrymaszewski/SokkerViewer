package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.ClubName;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.SokkerDate;

public class ClubNameDto extends ClubName {
	private ResultSet rs;

	public ClubNameDto(ResultSet rs) {
		this.rs = rs;
	}

	public ClubName getClubName() throws SQLException {
		this.setName(rs.getString("name")); //$NON-NLS-1$
		this.setDate(new Date(rs.getLong("millis"))); //$NON-NLS-1$
		this.getDate().setSokkerDate(new SokkerDate(rs.getInt("day"), rs.getInt("week"))); //$NON-NLS-1$ //$NON-NLS-2$
		return this;
	}
}
