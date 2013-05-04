package pl.pronux.sokker.actions;

import java.sql.SQLException;
import java.util.List;

import pl.pronux.sokker.data.sql.SQLQuery;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.data.sql.dao.NotesDao;
import pl.pronux.sokker.model.Note;

public final class SchedulerManager {

	private static SchedulerManager instance = new SchedulerManager();
	
	private SchedulerManager() {
	}
	
	public static SchedulerManager getInstance() {
		return instance;
	}
	
	public void insertNote(Note note) throws SQLException {
		try {
			SQLSession.connect();
			NotesDao notesDao = new NotesDao(SQLSession.getConnection());
			notesDao.insertNote(note);
			note.setId(notesDao.getMaxNoteId());
		} finally {
			SQLSession.close();
		}
	}

	public void updateNote(Note note) throws SQLException {
		try {
			SQLSession.connect();
			new NotesDao(SQLSession.getConnection()).updateNote(note);
		} finally {
			SQLSession.close();
		}
	}

	public void dropNote(int id) throws SQLException {
		boolean newConnection = SQLQuery.connect();
		new NotesDao(SQLSession.getConnection()).deleteNote(id);
		SQLQuery.close(newConnection);
	}

	public List<Note> getNoteData() throws SQLException {
		boolean newConnection = SQLQuery.connect();
		List<Note> notes = new NotesDao(SQLSession.getConnection()).getNotes();
		SQLQuery.close(newConnection);
		return notes;
	}

}
