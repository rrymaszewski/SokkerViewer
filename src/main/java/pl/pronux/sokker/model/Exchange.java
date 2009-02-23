package pl.pronux.sokker.model;

public class Exchange {
	private int id;
	private String name;
	private String originalName;
	private String currency;
	private double value;
	private double exchange;
	
	public double getExchange() {
		return exchange;
	}
	public void setExchange(double exchange) {
		this.exchange = exchange;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOriginalName() {
		return originalName;
	}
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	
}
