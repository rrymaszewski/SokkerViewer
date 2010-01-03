package pl.pronux.sokker.bean;

import java.util.HashMap;

public class LeagueStats {
	private int supporters;
	private HashMap<Integer, Integer> teamRating;
	private HashMap<Integer, Integer> averateTeamRating;
	private HashMap<Integer, Integer> averagePlayerRating;
	private HashMap<Integer, Integer> playersGoals;
	private HashMap<Integer, Integer> playersAssists;
	private HashMap<Integer, Integer> playersShoots;
	private HashMap<Integer, Integer> playersFouls;

	public int getSupporters() {
		return supporters;
	}

	public void setSupporters(int supporters) {
		this.supporters = supporters;
	}

	public HashMap<Integer, Integer> getTeamRating() {
		return teamRating;
	}

	public void setTeamRating(HashMap<Integer, Integer> teamRating) {
		this.teamRating = teamRating;
	}

	public HashMap<Integer, Integer> getAverateTeamRating() {
		return averateTeamRating;
	}

	public void setAverateTeamRating(HashMap<Integer, Integer> averateTeamRating) {
		this.averateTeamRating = averateTeamRating;
	}

	public HashMap<Integer, Integer> getAveragePlayerRating() {
		return averagePlayerRating;
	}

	public void setAveragePlayerRating(HashMap<Integer, Integer> averatePlayerRating) {
		this.averagePlayerRating = averatePlayerRating;
	}

	public HashMap<Integer, Integer> getPlayersGoals() {
		return playersGoals;
	}

	public void setPlayersGoals(HashMap<Integer, Integer> playersGoals) {
		this.playersGoals = playersGoals;
	}

	public HashMap<Integer, Integer> getPlayersAssists() {
		return playersAssists;
	}

	public void setPlayersAssists(HashMap<Integer, Integer> playersAssists) {
		this.playersAssists = playersAssists;
	}

	public HashMap<Integer, Integer> getPlayersShoots() {
		return playersShoots;
	}

	public void setPlayersShoots(HashMap<Integer, Integer> playersShoots) {
		this.playersShoots = playersShoots;
	}

	public HashMap<Integer, Integer> getPlayersFouls() {
		return playersFouls;
	}

	public void setPlayersFouls(HashMap<Integer, Integer> playersFouls) {
		this.playersFouls = playersFouls;
	}
}
