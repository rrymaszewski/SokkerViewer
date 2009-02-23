package pl.pronux.sokker.model;

public class PlayerStats {
	
	
	
	public static final String IDENTIFIER = "PLAYER_STATS";//$NON-NLS-1$
	public static final int GK = 0;

	public static final int DEF = 1;

	public static final int MID = 2;

	public static final int ATT = 3;

	public static final int INJURED = 1;

	public static final int NOT_INJURED = 0;

	private int playerID;
	
	private int matchID;
	
	private int teamID;

	private int number;

	private int formation;

	private int timeIn;

	private int timeOut;

	private int yellowCards;

	private int redCards;

	private int isInjured;

	private int goals;

	private int assists;

	private int fouls;

	private int shoots;

	private int rating;

	private int timePlaying;

	private int timeDefending;

	private int injuryDays;

	private Player player;

	private Match match;

	public Match getMatch() {
		return match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	public int getAssists() {
		return assists;
	}

	public void setAssists(int assists) {
		this.assists = assists;
	}

	public int getFormation() {
		return formation;
	}

	public void setFormation(int formation) {
		this.formation = formation;
	}

	public int getFouls() {
		return fouls;
	}

	public void setFouls(int fouls) {
		this.fouls = fouls;
	}

	public int getGoals() {
		return goals;
	}

	public void setGoals(int goals) {
		this.goals = goals;
	}

	public int getIsInjured() {
		return isInjured;
	}

	public void setIsInjured(int isInjured) {
		this.isInjured = isInjured;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getRedCards() {
		return redCards;
	}

	public void setRedCards(int redCards) {
		this.redCards = redCards;
	}

	public int getShoots() {
		return shoots;
	}

	public void setShoots(int shoots) {
		this.shoots = shoots;
	}

	public int getTimeDefending() {
		return timeDefending;
	}

	public void setTimeDefending(int timeDefending) {
		this.timeDefending = timeDefending;
	}

	public int getTimeIn() {
		return timeIn;
	}

	public void setTimeIn(int timeIn) {
		this.timeIn = timeIn;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public int getTimePlaying() {
		return timePlaying;
	}

	public void setTimePlaying(int timePlaying) {
		this.timePlaying = timePlaying;
	}

	public int getYellowCards() {
		return yellowCards;
	}

	public void setYellowCards(int yellowCards) {
		this.yellowCards = yellowCards;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	private int timePlayed = -1;

	public int getTimePlayed() {
		if (timePlayed == -1) {
			if (this.getTimeOut() > 0) {
				timePlayed = this.getTimeOut() - this.getTimeIn() - 1;
			} else if (this.getTimeIn() > 0 && this.getTimeOut() <= 0) {
				timePlayed = 90 - this.getTimeIn() + 1;
			} else if (this.getNumber() < 12 && this.getTimeIn() <= 0 && this.getTimeOut() <= 0) {
				timePlayed = 90;
			} else {
				timePlayed = 0;
			}
		}
		return timePlayed;
	}

	public int getInjuryDays() {
		return injuryDays;
	}

	public void setInjuryDays(int injury) {
		this.injuryDays = injury;
	}

	public int getMatchID() {
		return matchID;
	}

	public void setMatchID(int matchID) {
		this.matchID = matchID;
	}

	public int getTeamID() {
		return teamID;
	}

	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}
}
