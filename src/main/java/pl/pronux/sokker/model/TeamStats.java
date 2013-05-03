package pl.pronux.sokker.model;

import java.util.List;

public class TeamStats {

	private int teamId;

	private int timeOnHalf;

	private int timePossession;

	private int offsides;

	private int shoots;

	private int fouls;

	private int yellowCards;

	private int redCards;

	private String tacticName;

	private int ratingScoring;

	private int ratingPassing;

	private int ratingDefending;

	private boolean empty = false;

	private List<PlayerStats> playersStats;

	private Match match;

	private double averageRating;

	public TeamStats(boolean empty) {
		this.empty = empty;
	}

	public TeamStats() {
	}

	public double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}

	public int getFouls() {
		return fouls;
	}

	public void setFouls(int fouls) {
		this.fouls = fouls;
	}

	public int getOffsides() {
		return offsides;
	}

	public void setOffsides(int offsides) {
		this.offsides = offsides;
	}

	public int getRatingDefending() {
		return ratingDefending;
	}

	public void setRatingDefending(int ratingDefending) {
		this.ratingDefending = ratingDefending;
	}

	public int getRatingPassing() {
		return ratingPassing;
	}

	public void setRatingPassing(int ratingPassing) {
		this.ratingPassing = ratingPassing;
	}

	public int getRatingScoring() {
		return ratingScoring;
	}

	public void setRatingScoring(int ratingScoring) {
		this.ratingScoring = ratingScoring;
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

	public String getTacticName() {
		return tacticName;
	}

	public void setTacticName(String tacticName) {
		this.tacticName = tacticName;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public int getTimeOnHalf() {
		return timeOnHalf;
	}

	public void setTimeOnHalf(int timeOnHalf) {
		this.timeOnHalf = timeOnHalf;
	}

	public int getTimePossession() {
		return timePossession;
	}

	public void setTimePossession(int timePossession) {
		this.timePossession = timePossession;
	}

	public int getYellowCards() {
		return yellowCards;
	}

	public void setYellowCards(int yellowCards) {
		this.yellowCards = yellowCards;
	}

	public List<PlayerStats> getPlayersStats() {
		return playersStats;
	}

	public void setPlayersStats(List<PlayerStats> playersStats) {
		this.playersStats = playersStats;
	}

	public Match getMatch() {
		return match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

}
