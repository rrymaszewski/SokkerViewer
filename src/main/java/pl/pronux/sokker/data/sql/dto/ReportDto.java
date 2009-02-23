package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Report;

public class ReportDto extends Report {

	private ResultSet rs;

	public ReportDto(ResultSet rs) {
		this.rs = rs;
	}
	
	public Report getReport() throws SQLException {
		this.setPersonID(rs.getInt("player_id")); //$NON-NLS-1$
		this.setReportID(rs.getInt("id_report")); //$NON-NLS-1$
		this.setType(rs.getInt("type")); //$NON-NLS-1$
		this.setValue(rs.getInt("value")); //$NON-NLS-1$
		this.setDate(new Date(rs.getLong("millis"))); //$NON-NLS-1$
		this.setWeek(rs.getInt("week")); //$NON-NLS-1$
		this.setChecked(rs.getBoolean("checked")); //$NON-NLS-1$
		return this;
	}
}
