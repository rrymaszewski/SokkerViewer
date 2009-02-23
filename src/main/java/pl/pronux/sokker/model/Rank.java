package pl.pronux.sokker.model;

public class Rank {
	private Date date;
	private int id;
	private double rank;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getRank() {
		return rank;
	}

	public void setRank(double rank) {
		this.rank = rank;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
