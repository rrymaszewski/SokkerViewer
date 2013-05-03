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
		this.setPersonId(rs.getInt("player_id")); 
		this.setReportId(rs.getInt("id_report")); 
		this.setType(rs.getInt("type")); 
		this.setValue(rs.getInt("value")); 
		this.setDate(new Date(rs.getLong("millis"))); 
		this.setWeek(rs.getInt("week")); 
		this.setChecked(rs.getBoolean("checked")); 
		return this;
	}
}
