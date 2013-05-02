package pl.pronux.sokker.model;

public interface PlayerInterface {

	int getCountryfrom();

	int getPosition();

	PlayerSkills[] getSkills();

	void setCountryfrom(int countryfrom);

	void setPosition(int position);

	double[] getPositionTable();

	void setPositionTable(double[] positionTable);

	void setSkills(PlayerSkills[] skills);

	Money getSoldPrice();

	void setSoldPrice(double soldPrice);

	Money getBuyPrice();

	void setBuyPrice(double buyPrice);

	void setJunior(Junior junior);

	Junior getJunior();

	Transfer getTransferSell();

	void setTransferSell(Transfer transferSell);

	Transfer getTransferBuy();

	void setTransferBuy(Transfer transferBuy);

	int getId();

}