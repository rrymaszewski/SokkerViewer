package pl.pronux.sokker.model;

public class Match extends MatchBase {
	public static final String IDENTIFIER = "MATCH"; //$NON-NLS-1$
	
	public static final int WEATHER_SUN = 1;

	private String homeTeamName;

	private String awayTeamName;

	private int week;

	private int day;

	private Date dateExpected;

	private Date dateStarted;

	private int supporters;

	private int weather;

	private int teamID;

	private TeamStats awayTeamStats;

	private TeamStats homeTeamStats;

	private Club awayTeam;

	private Club homeTeam;

	public Club getAwayTeam() {
		return awayTeam;
	}

	public void setAwayTeam(Club awayTeam) {
		this.awayTeam = awayTeam;
	}

	public Club getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(Club homeTeam) {
		this.homeTeam = homeTeam;
	}

	public int getTeamID() {
		return teamID;
	}

	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}

	public String getAwayTeamName() {
		return awayTeamName;
	}

	public void setAwayTeamName(String awayTeamName) {
		this.awayTeamName = awayTeamName;
	}

	public Date getDateExpected() {
		return dateExpected;
	}

	public void setDateExpected(Date dateExpected) {
		this.dateExpected = dateExpected;
	}

	public Date getDateStarted() {
		return dateStarted;
	}

	public void setDateStarted(Date dateStarted) {
		this.dateStarted = dateStarted;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getHomeTeamName() {
		return homeTeamName;
	}

	public void setHomeTeamName(String homeTeamName) {
		this.homeTeamName = homeTeamName;
	}

	public int getSupporters() {
		return supporters;
	}

	public void setSupporters(int supporters) {
		this.supporters = supporters;
	}

	public int getWeather() {
		return weather;
	}

	public void setWeather(int weather) {
		this.weather = weather;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public TeamStats getAwayTeamStats() {
		return awayTeamStats;
	}

	public void setAwayTeamStats(TeamStats awayTeamStats) {
		this.awayTeamStats = awayTeamStats;
	}

	public TeamStats getHomeTeamStats() {
		return homeTeamStats;
	}

	public void setHomeTeamStats(TeamStats homeTeamStats) {
		this.homeTeamStats = homeTeamStats;
	}

}
