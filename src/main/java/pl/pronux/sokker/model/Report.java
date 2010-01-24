package pl.pronux.sokker.model;


public class Report {
	public final static int INFO = 1;
	public final static int COST = 2;
	public final static int INCOME = 3;

	private int status;
	
	private long reportID;

	private int type;

	private int personID;

	private Date date;

	private int value;

	private int week;

	private Person person;

	private boolean checked;

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getPersonID() {
		return personID;
	}

	public void setPersonID(int playerId) {
		this.personID = playerId;
	}

	public long getReportID() {
		return reportID;
	}

	public void setReportID(long reportId) {
		this.reportID = reportId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
