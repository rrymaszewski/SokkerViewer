package pl.pronux.sokker.model;

public class ClubBudget {

	private Date date;

	private Money money;
	
	private int id;

	public Money getMoney() {
		return money;
	}

	public void setMoney(Money money) {
		this.money = money;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
