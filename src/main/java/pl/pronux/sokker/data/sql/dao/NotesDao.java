package pl.pronux.sokker.data.sql.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import pl.pronux.sokker.data.sql.dto.NoteDto;
import pl.pronux.sokker.model.Note;

public class NotesDao {
	private Connection connection;

	public NotesDao(Connection connection) {
		this.connection = connection;
	}

	public void updateNote(Note note) throws SQLException {
		PreparedStatement ps;
		ps = connection.prepareStatement("UPDATE note SET " + "millis = ?, " + "title = ?, " + "text = ?, " + "alert_millis = ?, " + "mod_millis = ?, " + "checked = ? " + "WHERE id_note = ?"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$

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
		PreparedStatement pstm;
		pstm = connection.prepareStatement("INSERT INTO note(millis, title, text, alert_millis, mod_millis, checked) VALUES (?,?,?,?,?,?)"); //$NON-NLS-1$
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
		PreparedStatement ps;
		ps = connection.prepareStatement("DELETE FROM note WHERE id_note = ?"); //$NON-NLS-1$
		ps.setInt(1, id);
		ps.executeUpdate();
		ps.close();

	}

	public int getMaxNoteID() throws SQLException {
		int id = 0;
		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT max(id_note) FROM note"); //$NON-NLS-1$
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			id = rs.getInt(1);
		}
		rs.close();
		ps.close();
		return id;
	}

	public ArrayList<Note> getNotes() throws SQLException {
		Note note;
		ArrayList<Note> alNote = new ArrayList<Note>();
		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT * FROM note ORDER BY millis DESC "); //$NON-NLS-1$
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			note = new NoteDto(rs).getNote();
			alNote.add(note);
		}
		rs.close();
		ps.close();
		return alNote;
	}
}
