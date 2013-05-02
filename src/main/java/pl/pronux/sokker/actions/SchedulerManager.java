package pl.pronux.sokker.actions;

import java.sql.SQLException;
import java.util.ArrayList;

import pl.pronux.sokker.data.sql.SQLQuery;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.data.sql.dao.NotesDao;
import pl.pronux.sokker.model.Note;

public class SchedulerManager {

	private static SchedulerManager instance = new SchedulerManager();
	
	private SchedulerManager() {
	}
	
	public static SchedulerManager instance() {
		return instance;
	}
	
	public void insertNote(Note note) throws SQLException {
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

	public void updateNote(Note note) throws SQLException {
		try {
			SQLSession.connect();
			new NotesDao(SQLSession.getConnection()).updateNote(note);
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLSession.close();
		}
	}

	public void dropNote(int id) throws SQLException {
		try {
			boolean newConnection = SQLQuery.connect();
			new NotesDao(SQLSession.getConnection()).deleteNote(id);
			SQLQuery.close(newConnection);
		} catch (SQLException e) {
			throw e;
		}
	}

	public ArrayList<Note> getNoteData() throws SQLException {
		ArrayList<Note> notes;
		boolean newConnection = SQLQuery.connect();
		notes = new NotesDao(SQLSession.getConnection()).getNotes();
		SQLQuery.close(newConnection);
		return notes;
	}

}
