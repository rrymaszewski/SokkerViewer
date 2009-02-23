package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.ClubArenaName;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.SokkerDate;

public class ClubArenaNameDto extends ClubArenaName {
private ResultSet rs;

public ClubArenaNameDto(ResultSet rs) {
	this.rs = rs;
}

public ClubArenaName getClubArenaName() throws SQLException {
	this.setArenaName(rs.getString("arena_name")); //$NON-NLS-1$
	this.setDate(new Date(rs.getLong("millis"))); //$NON-NLS-1$
	this.getDate().setSokkerDate(new SokkerDate(rs.getInt("day"), rs.getInt("week"))); //$NON-NLS-1$ //$NON-NLS-2$
	return this;
}
}
