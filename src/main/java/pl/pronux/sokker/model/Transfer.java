package pl.pronux.sokker.model;

public class Transfer {
	public static final int IN = 0;
	public static final int OUT = 1;
	
	private int transferId;

	private int sellerTeamId;

	private int buyerTeamId;

	private String sellerTeamName;

	private String buyerTeamName;

	private int playerId;

	private Date date;

	private Money price;

	private Money playerValue;

	private Player player;

	private int isInOut;

	private Money earned;

	public Money getEarned() {
		return earned;
	}

	public void setEarned(Money earned) {
		this.earned = earned;
	}

	public int getBuyerTeamId() {
		return buyerTeamId;
	}

	public void setBuyerTeamId(int buyerTeamId) {
		this.buyerTeamId = buyerTeamId;
	}

	public String getBuyerTeamName() {
		return buyerTeamName;
	}

	public void setBuyerTeamName(String buyerTeamName) {
		this.buyerTeamName = buyerTeamName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public Money getPlayerValue() {
		return playerValue;
	}

	public void setPlayerValue(Money playerValue) {
		this.playerValue = playerValue;
	}

	public Money getPrice() {
		return price;
	}

	public void setPrice(Money price) {
		this.price = price;
	}

	public int getSellerTeamId() {
		return sellerTeamId;
	}

	public void setSellerTeamId(int sellerTeamId) {
		this.sellerTeamId = sellerTeamId;
	}

	public String getSellerTeamName() {
		return sellerTeamName;
	}

	public void setSellerTeamName(String sellerTeamName) {
		this.sellerTeamName = sellerTeamName;
	}

	public int getTransferId() {
		return transferId;
	}

	public void setTransferId(int transferId) {
		this.transferId = transferId;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getIsInOut() {
		return isInOut;
	}

	public void setIsInOut(int isInOut) {
		this.isInOut = isInOut;
	}


}
