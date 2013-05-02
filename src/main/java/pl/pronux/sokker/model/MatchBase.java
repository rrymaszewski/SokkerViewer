package pl.pronux.sokker.model;

public class MatchBase {

	public static final int FINISHED = 1;

	public static final int NOT_FINISHED = 0;

	private League league;

	private int leagueID;

	private int round;

	private int season;

	private int matchID;

	private int homeTeamID;

	private int awayTeamID;

	private int homeTeamScore;

	private int awayTeamScore;

	private int isFinished;

	public int getAwayTeamID() {
		return awayTeamID;
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

	public void setAwayTeamID(int awayTeamID) {
		this.awayTeamID = awayTeamID;
	}

	public int getAwayTeamScore() {
		return awayTeamScore;
	}

	public void setAwayTeamScore(int awayTeamScore) {
		this.awayTeamScore = awayTeamScore;
	}

	public int getHomeTeamID() {
		return homeTeamID;
	}

	public void setHomeTeamID(int homeTeamID) {
		this.homeTeamID = homeTeamID;
	}

	public int getHomeTeamScore() {
		return homeTeamScore;
	}

	public void setHomeTeamScore(int homeTeamScore) {
		this.homeTeamScore = homeTeamScore;
	}

	public int getIsFinished() {
		return isFinished;
	}

	public void setIsFinished(int isFinished) {
		this.isFinished = isFinished;
	}

	public int getMatchID() {
		return matchID;
	}

	public void setMatchID(int matchID) {
		this.matchID = matchID;
	}

	public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
		this.league = league;
	}
}
