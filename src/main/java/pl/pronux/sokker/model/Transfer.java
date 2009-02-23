package pl.pronux.sokker.model;

public class Transfer {
	public final static String IDENTIFIER = "TRANSFER"; //$NON-NLS-1$
	public final static int IN = 0;
	public final static int OUT = 1;
	private int transferID;

	private int sellerTeamID;

	private int buyerTeamID;

	private String sellerTeamName;

	private String buyerTeamName;

	private int playerID;

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

	public int getBuyerTeamID() {
		return buyerTeamID;
	}

	public void setBuyerTeamID(int buyerTeamID) {
		this.buyerTeamID = buyerTeamID;
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

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
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

	public int getSellerTeamID() {
		return sellerTeamID;
	}

	public void setSellerTeamID(int sellerTeamID) {
		this.sellerTeamID = sellerTeamID;
	}

	public String getSellerTeamName() {
		return sellerTeamName;
	}

	public void setSellerTeamName(String sellerTeamName) {
		this.sellerTeamName = sellerTeamName;
	}

	public int getTransferID() {
		return transferID;
	}

	public void setTransferID(int transferID) {
		this.transferID = transferID;
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
