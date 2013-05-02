package pl.pronux.sokker.model;

import java.io.Serializable;

public abstract class Person implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6288575437262792754L;

	private int id;
	
	private int status;

	private String name = ""; //$NON-NLS-1$

	private String surname = ""; //$NON-NLS-1$
	
	private String clubName;

	private String note = ""; //$NON-NLS-1$
	
	private int teamID;
	
	public int getTeamID() {
		return teamID;
	}

	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}

	/* (non-Javadoc)
	 * @see pl.pronux.sokker.PersonInterface#getClub()
	 */
	public String getClubName() {
		return clubName;
	}

	/* (non-Javadoc)
	 * @see pl.pronux.sokker.PersonInterface#setClub(java.lang.String)
	 */
	public void setClubName(String club) {
		this.clubName = club;
	}

	public void moveToTrash() {
		status += 10;
	}
	
	public void restoreFromTrash() {
		status -= 10;
	}
	
	public void removeFromTrash() {
		status += 10;
	}
	
	/* (non-Javadoc)
	 * @see pl.pronux.sokker.PersonInterface#getId()
	 */
	public int getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see pl.pronux.sokker.PersonInterface#getName()
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see pl.pronux.sokker.PersonInterface#setId(int)
	 */
	public void setId(int id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see pl.pronux.sokker.PersonInterface#setName(java.lang.String)
	 */
	public void setName(String name) {
		if(name == null) {
			this.name = ""; //$NON-NLS-1$
		} else {
			this.name = name;	
		}
	}
	
	/* (non-Javadoc)
	 * @see pl.pronux.sokker.PersonInterface#getSurname()
	 */
	public String getSurname() {
		return surname;
	}

	/* (non-Javadoc)
	 * @see pl.pronux.sokker.PersonInterface#setSurname(java.lang.String)
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/* (non-Javadoc)
	 * @see pl.pronux.sokker.PersonInterface#getStatus()
	 */
	public int getStatus() {
		return status;
	}

	/* (non-Javadoc)
	 * @see pl.pronux.sokker.PersonInterface#setStatus(int)
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		if(note == null) { 
			this.note = ""; //$NON-NLS-1$
		} else {
			this.note = note;	
		}
	}
}
