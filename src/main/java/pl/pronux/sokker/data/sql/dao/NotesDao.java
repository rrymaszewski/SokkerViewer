package pl.pronux.sokker.data.sql.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pl.pronux.sokker.data.sql.dto.NoteDto;
import pl.pronux.sokker.model.Note;

public class NotesDao {
	private Connection connection;

	public NotesDao(Connection connection) {
		this.connection = connection;
	}

	public void updateNote(Note note) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE note SET " + "millis = ?, " + "title = ?, " + "text = ?, " + "alert_millis = ?, " + "mod_millis = ?, " + "checked = ? " + "WHERE id_note = ?");        

		ps.setLong(1, note.getDate().getMillis());
		ps.setString(2, note.getTitle());
		ps.setString(3, note.getText());
		if (note.getAlertDate() != null) {
			ps.setLong(4, note.getAlertDate().getMillis());
		} else {
			ps.setNull(4, java.sql.Types.BIGINT);
		}
		ps.setLong(5, note.getModificationDate().getMillis());
		ps.setBoolean(6, note.isChecked());
		ps.setInt(7, note.getId());

		ps.executeUpdate();
		ps.close();
	}

	public void insertNote(Note note) throws SQLException {
		PreparedStatement pstm = connection.prepareStatement("INSERT INTO note(millis, title, text, alert_millis, mod_millis, checked) VALUES (?,?,?,?,?,?)"); 
		pstm.setLong(1, note.getDate().getMillis());
		pstm.setString(2, note.getTitle());
		pstm.setString(3, note.getText());
		if (note.getAlertDate() != null) {
			pstm.setLong(4, note.getAlertDate().getMillis());
		} else {
			pstm.setNull(4, java.sql.Types.BIGINT);
		}
		pstm.setLong(5, note.getModificationDate().getMillis());
		pstm.setBoolean(6, note.isChecked());
		pstm.executeUpdate();
		pstm.close();
	}

	public void deleteNote(int id) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("DELETE FROM note WHERE id_note = ?"); 
		ps.setInt(1, id);
		ps.executeUpdate();
		ps.close();

	}

	public int getMaxNoteId() throws SQLException {
		int id = 0;
		PreparedStatement ps = connection.prepareStatement("SELECT max(id_note) FROM note"); 
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			id = rs.getInt(1);
		}
		rs.close();
		ps.close();
		return id;
	}

	public List<Note> getNotes() throws SQLException {
		List<Note> notes = new ArrayList<Note>();
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM note ORDER BY millis DESC "); 
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Note note = new NoteDto(rs).getNote();
			notes.add(note);
		}
		rs.close();
		ps.close();
		return notes;
	}
}
