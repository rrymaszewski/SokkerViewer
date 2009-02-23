package pl.pronux.sokker.actions;

import java.sql.SQLException;
import java.util.ArrayList;

import pl.pronux.sokker.data.sql.SQLQuery;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.data.sql.dao.NotesDao;
import pl.pronux.sokker.model.Note;

public class SchedulerManager {

	public static void insertNote(Note note) throws SQLException {
		try {
			SQLSession.connect();
			NotesDao notesDao = new NotesDao(SQLSession.getConnection());
			notesDao.insertNote(note);
			note.setId(notesDao.getMaxNoteID());
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLSession.close();
		}
	}

	public static void updateNote(Note note) throws SQLException {
		try {
			SQLSession.connect();
			new NotesDao(SQLSession.getConnection()).updateNote(note);
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLSession.close();
		}
	}

	public static void dropNote(int id) throws SQLException {
		try {
			boolean newConnection = SQLQuery.connect();
			new NotesDao(SQLSession.getConnection()).deleteNote(id);
			SQLQuery.close(newConnection);
		} catch (SQLException e) {
			throw e;
		}
	}

	public static ArrayList<Note> getNoteData() throws SQLException {
		ArrayList<Note> notes;
		boolean newConnection = SQLQuery.connect();
		notes = new NotesDao(SQLSession.getConnection()).getNotes();
		SQLQuery.close(newConnection);
		return notes;
	}

}
