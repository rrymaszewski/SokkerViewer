package pl.pronux.sokker.model;

import java.util.List;

public class LeagueSeason {
	
	private int round;

	private int leagueId;

	private int season;
	
	private int rawSeason;

	private List<LeagueRound> rounds;

	private League league;

	public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
		this.league = league;
	}

	public int getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(int leagueId) {
		this.leagueId = leagueId;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getSeason() {
		return season;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public List<LeagueRound> getRounds() {
		return rounds;
	}

	public void setRounds(List<LeagueRound> rounds) {
		this.rounds = rounds;
	}

	public int getRawSeason() {
		return rawSeason;
	}

	public void setRawSeason(int rawSeason) {
		this.rawSeason = rawSeason;
	}

}
