package pl.pronux.sokker.model;

public class ClubSupporters {
	
	Date date;
	
	int fanclubcount;

	byte fanclubmood;
	
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFanclubcount() {
		return fanclubcount;
	}

	public void setFanclubcount(int fanclubcount) {
		this.fanclubcount = fanclubcount;
	}

	public byte getFanclubmood() {
		return fanclubmood;
	}

	public void setFanclubmood(byte fanclubmood) {
		this.fanclubmood = fanclubmood;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
