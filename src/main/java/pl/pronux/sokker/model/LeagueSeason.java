package pl.pronux.sokker.model;

import java.util.List;

public class LeagueSeason {
	
	final public static String IDENTIFIER = "leagueSeason"; //$NON-NLS-1$
	
	private int round;

	private int leagueID;

	private int season;
	
	private int rawSeason;

	private List<LeagueRound> alRounds;

	private League league;

	public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
		this.league = league;
	}

	public int getLeagueID() {
		return leagueID;
	}

	public void setLeagueID(int leagueID) {
		this.leagueID = leagueID;
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

	public List<LeagueRound> getAlRounds() {
		return alRounds;
	}

	public void setAlRounds(List<LeagueRound> rounds) {
		this.alRounds = rounds;
	}

	public int getRawSeason() {
		return rawSeason;
	}

	public void setRawSeason(int rawSeason) {
		this.rawSeason = rawSeason;
	}

}
