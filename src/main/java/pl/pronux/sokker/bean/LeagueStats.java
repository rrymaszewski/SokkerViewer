package pl.pronux.sokker.bean;

import java.util.Map;

public class LeagueStats {
	private int supporters;
	private Map<Integer, Integer> teamRating;
	private Map<Integer, Integer> averateTeamRating;
	private Map<Integer, Integer> averagePlayerRating;
	private Map<Integer, Integer> playersGoals;
	private Map<Integer, Integer> playersAssists;
	private Map<Integer, Integer> playersShoots;
	private Map<Integer, Integer> playersFouls;

	public int getSupporters() {
		return supporters;
	}

	public void setSupporters(int supporters) {
		this.supporters = supporters;
	}

	public Map<Integer, Integer> getTeamRating() {
		return teamRating;
	}

	public void setTeamRating(Map<Integer, Integer> teamRating) {
		this.teamRating = teamRating;
	}

	public Map<Integer, Integer> getAverateTeamRating() {
		return averateTeamRating;
	}

	public void setAverateTeamRating(Map<Integer, Integer> averateTeamRating) {
		this.averateTeamRating = averateTeamRating;
	}

	public Map<Integer, Integer> getAveragePlayerRating() {
		return averagePlayerRating;
	}

	public void setAveragePlayerRating(Map<Integer, Integer> averatePlayerRating) {
		this.averagePlayerRating = averatePlayerRating;
	}

	public Map<Integer, Integer> getPlayersGoals() {
		return playersGoals;
	}

	public void setPlayersGoals(Map<Integer, Integer> playersGoals) {
		this.playersGoals = playersGoals;
	}

	public Map<Integer, Integer> getPlayersAssists() {
		return playersAssists;
	}

	public void setPlayersAssists(Map<Integer, Integer> playersAssists) {
		this.playersAssists = playersAssists;
	}

	public Map<Integer, Integer> getPlayersShoots() {
		return playersShoots;
	}

	public void setPlayersShoots(Map<Integer, Integer> playersShoots) {
		this.playersShoots = playersShoots;
	}

	public Map<Integer, Integer> getPlayersFouls() {
		return playersFouls;
	}

	public void setPlayersFouls(Map<Integer, Integer> playersFouls) {
		this.playersFouls = playersFouls;
	}
}
