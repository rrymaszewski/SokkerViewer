package pl.pronux.sokker.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player extends Person implements Serializable, PlayerInterface {

	public Player() {
		this.playerMatchStatistics = new ArrayList<PlayerStats>();
	}
	/**
	 *
	 */
	private static final long serialVersionUID = 1525870309479485957L;

	public static final int STATUS_INCLUB = 0;

	public static final int STATUS_HISTORY = 1;

	public static final int STATUS_TRASH = 11;

	public static final int STATUS_DELETED = 21;

	public static final int EXISTS_IN_SOKKER_UNCHECKED = 0; 
	
	public static final int EXISTS_IN_SOKKER_TRUE = 1;
	
	public static final int EXISTS_IN_SOKKER_FALSE = 2;
	
	public static final int EXISTS_IN_SOKKER_COMPLETED = 3;
	
	public static final int POSITION_GK = 1;
	public static final int POSITION_DEF = 2;
	public static final int POSITION_WINGBACK = 3;
	public static final int POSITION_DEF_OFF = 4;
	public static final int POSITION_MID = 5;
	public static final int POSITION_DEF_MID = 6;
	public static final int POSITION_OFF_MID = 7;
	public static final int POSITION_WINGER = 8;
	public static final int POSITION_DEF_ATT = 9;
	public static final int POSITION_ATT = 10;
	public static final int POSITION_PERSONAL = 11;
	
	public static final int MATCH_GK = 0;
	public static final int MATCH_DEF = 1;
	public static final int MATCH_MID = 2;
	public static final int MATCH_ATT = 3;
	
	private int preferredPosition;
	
	private int countryfrom;

	private Junior junior;

	private int transferList;

	private int national;

	private int position;

	private Money soldPrice;

	private Money buyPrice;

	private double[] positionTable;

	private boolean nt;

	private int youthTeamId;

	private PlayerSkills[] skills;

	private NtSkills[] ntSkills;

	private Transfer transferSell;

	private Transfer transferBuy;

	private List<PlayerStats> playerMatchStatistics;
	
	private int juniorId;
	
	private int existsInSokker = EXISTS_IN_SOKKER_TRUE;
	
	private int avgRating;
	
	private Club team;

	private int height = 0;
	
	public Club getTeam() {
		return team;
	}

	public void setTeam(Club team) {
		this.team = team;
	}

	public int getAvgRating() {
		return avgRating;
	}

	public void setAvgRating(int averageRating) {
		this.avgRating = averageRating;
	}

	public int getExistsInSokker() {
		return existsInSokker;
	}

	public void setExistsInSokker(int existsInSokker) {
		this.existsInSokker = existsInSokker;
	}

	public int getJuniorId() {
		return juniorId;
	}

	public void setJuniorId(int juniorId) {
		this.juniorId = juniorId;
	}

	public List<PlayerStats> getPlayerMatchStatistics() {
		return playerMatchStatistics;
	}

	public void setPlayerMatchStatistics(List<PlayerStats> alPlayerStats) {
		this.playerMatchStatistics = alPlayerStats;
	}

	public Transfer getTransferSell() {
		return transferSell;
	}

	public void setTransferSell(Transfer transfer) {
		this.transferSell = transfer;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see pl.pronux.sokker.PlayerInterface#getCountryfrom()
	 */
	public int getCountryfrom() {
		return countryfrom;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see pl.pronux.sokker.PlayerInterface#getPosition()
	 */
	public int getPosition() {
		return position;
	}

	// na wszelki wypadek gdyby w przyszlosci trzeba by bylo inicjalizowac tablice
	// skilli
	// public Player(int numberOfTrainings) {
	// skills = new Skills[numberOfTrainings];
	// }
	//
	// public Player() {
	// this(1);
	// }

	/*
	 * (non-Javadoc)
	 *
	 * @see pl.pronux.sokker.PlayerInterface#getSkills()
	 */
	public PlayerSkills[] getSkills() {
		return skills;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see pl.pronux.sokker.PlayerInterface#setCountryfrom(int)
	 */
	public void setCountryfrom(int countryfrom) {
		this.countryfrom = countryfrom;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see pl.pronux.sokker.PlayerInterface#setPosition(int)
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see pl.pronux.sokker.PlayerInterface#getPositionTable()
	 */
	public double[] getPositionTable() {
		return positionTable;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see pl.pronux.sokker.PlayerInterface#setPositionTable(double[])
	 */
	public void setPositionTable(double[] positionTable) {
		this.positionTable = positionTable;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see pl.pronux.sokker.PlayerInterface#setSkills(pl.pronux.sokker.PlayerSkills[])
	 */
	public void setSkills(PlayerSkills[] skills) {
		this.skills = skills;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see pl.pronux.sokker.PlayerInterface#getSoldPrice()
	 */
	public Money getSoldPrice() {
		return soldPrice;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see pl.pronux.sokker.PlayerInterface#setSoldPrice(double)
	 */
	public void setSoldPrice(double soldPrice) {
		if (this.soldPrice == null) {
			this.soldPrice = new Money(soldPrice);
		} else {
			this.soldPrice.setDoubleValue(soldPrice);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see pl.pronux.sokker.PlayerInterface#getBuyPrice()
	 */
	public Money getBuyPrice() {
		return buyPrice;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see pl.pronux.sokker.PlayerInterface#setBuyPrice(double)
	 */
	public void setBuyPrice(double buyPrice) {
		if (this.buyPrice == null) {
			this.buyPrice = new Money(buyPrice);
		} else {
			this.buyPrice.setDoubleValue(buyPrice);
		}
	}

	public boolean isNt() {
		return nt;
	}

	public void setNt(boolean nt) {
		this.nt = nt;
	}

	public int getYouthTeamId() {
		return youthTeamId;
	}

	public void setYouthTeamId(int youthTeamId) {
		this.youthTeamId = youthTeamId;
	}

	public NtSkills[] getNtSkills() {
		return ntSkills;
	}

	public void setNtSkills(NtSkills[] ntSkills) {
		this.ntSkills = ntSkills;
	}

	public int getTransferList() {
		return transferList;
	}

	public void setTransferList(int transferList) {
		this.transferList = transferList;
	}

	public int getNational() {
		return national;
	}

	public void setNational(int national) {
		this.national = national;
	}

	public Junior getJunior() {
		return junior;
	}

	public void setJunior(Junior junior) {
		this.junior = junior;
	}

	public Transfer getTransferBuy() {
		return transferBuy;
	}

	public void setTransferBuy(Transfer transferBuy) {
		this.transferBuy = transferBuy;
	}

	public int getBestPosition() {
	
		double bestPosition = 0;
		int position = 0;
	
		double[] data = this.getPositionTable();
		for (int i = 0; i < data.length; i++) {
			if (data[i] > bestPosition) {
				bestPosition = data[i];
				position = i + 1;
			}
		}
		return position;
	}

	public int getPreferredPosition() {
		return preferredPosition;
	}

	public void setPreferredPosition(int avgPosition) {
		this.preferredPosition = avgPosition;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}
	

}
