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
		this.setId(rs.getInt("id_note")); //$NON-NLS-1$
		this.setDate(new Date(rs.getLong("millis"))); //$NON-NLS-1$
		this.setTitle(rs.getString("title")); //$NON-NLS-1$
		this.setText(rs.getString("text")); //$NON-NLS-1$
		long time = rs.getLong("alert_millis"); //$NON-NLS-1$
		if (time != 0) {
			this.setAlertDate(new Date(time));
		}
		this.setModificationDate(new Date(rs.getLong("mod_millis"))); //$NON-NLS-1$
		this.setChecked(rs.getBoolean("checked")); //$NON-NLS-1$
		return this;
	}
}
