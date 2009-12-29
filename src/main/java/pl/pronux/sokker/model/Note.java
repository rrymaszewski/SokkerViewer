package pl.pronux.sokker.model;

import java.util.GregorianCalendar;

public class Note {
	
	private Date alertDate;

	private Date date;
	
	private Date modificationDate;

	private int id;

	private String title;

	private String text;
	
	private boolean checked;
	
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Note() {
		GregorianCalendar cal = new GregorianCalendar();
		date = new Date(cal.getTimeInMillis());
		modificationDate = new Date(cal.getTimeInMillis());
	}
	
	public Date getAlertDate() {
		return alertDate;
	}

	public Date getDate() {
		return date;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}

	public void setAlertDate(Date alertDate) {
		this.alertDate = alertDate;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}
}
