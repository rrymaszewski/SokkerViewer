package pl.pronux.sokker.model;

public interface PlayerInterface {

	public int getCountryfrom();

	public int getPosition();

	public PlayerSkills[] getSkills();

	public void setCountryfrom(int countryfrom);

	public void setPosition(int position);

	public double[] getPositionTable();

	public void setPositionTable(double[] positionTable);

	public void setSkills(PlayerSkills[] skills);

	public Money getSoldPrice();

	public void setSoldPrice(double soldPrice);

	public Money getBuyPrice();

	public void setBuyPrice(double buyPrice);

	public void setJunior(Junior junior);

	public Junior getJunior();

	public Transfer getTransferSell();

	public void setTransferSell(Transfer transferSell);

	public Transfer getTransferBuy();

	public void setTransferBuy(Transfer transferBuy);

	public int getId();

}