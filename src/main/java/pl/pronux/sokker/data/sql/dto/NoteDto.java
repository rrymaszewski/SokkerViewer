package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Note;

public class NoteDto extends Note {
	private ResultSet rs;

	public NoteDto(ResultSet rs) {
		this.rs = rs;
	}
	
	public Note getNote() throws SQLException {
		this.setId(rs.getInt("id_note")); 
		this.setDate(new Date(rs.getLong("millis"))); 
		this.setTitle(rs.getString("title")); 
		this.setText(rs.getString("text")); 
		long time = rs.getLong("alert_millis"); 
		if (time != 0) {
			this.setAlertDate(new Date(time));
		}
		this.setModificationDate(new Date(rs.getLong("mod_millis"))); 
		this.setChecked(rs.getBoolean("checked")); 
		return this;
	}
}
