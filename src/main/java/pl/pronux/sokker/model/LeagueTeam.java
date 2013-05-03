package pl.pronux.sokker.model;

/**
 * @author rymek
 */
public class LeagueTeam {
	
	private Club club; 
	
	private int leagueId;

	private int teamId;

	private int round;

	private int points;

	private int wins;

	private int draws;

	private int losses;

	private int goalsScored;

	private int goalsLost;

	private int beginPlace;

	private String rankTotal;

	private int position;
	
	private int season;
	
	private String teamName;

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public int getSeason() {
		return season;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return
	 * @uml.property name="draws"
	 */
	public int getDraws() {
		return draws;
	}

	/**
	 * @param draws
	 * @uml.property name="draws"
	 */
	public void setDraws(int draws) {
		this.draws = draws;
	}

	/**
	 * @return
	 * @uml.property name="goalsLost"
	 */
	public int getGoalsLost() {
		return goalsLost;
	}

	/**
	 * @param goalsLost
	 * @uml.property name="goalsLost"
	 */
	public void setGoalsLost(int goalsLost) {
		this.goalsLost = goalsLost;
	}

	/**
	 * @return
	 * @uml.property name="goalsScored"
	 */
	public int getGoalsScored() {
		return goalsScored;
	}

	/**
	 * @param goalsScored
	 * @uml.property name="goalsScored"
	 */
	public void setGoalsScored(int goalsScored) {
		this.goalsScored = goalsScored;
	}

	/**
	 * @return
	 * @uml.property name="leagueID"
	 */
	public int getLeagueId() {
		return leagueId;
	}

	/**
	 * @param leagueId
	 * @uml.property name="leagueID"
	 */
	public void setLeagueId(int leagueId) {
		this.leagueId = leagueId;
	}

	/**
	 * @return
	 * @uml.property name="losses"
	 */
	public int getLosses() {
		return losses;
	}

	/**
	 * @param losses
	 * @uml.property name="losses"
	 */
	public void setLosses(int losses) {
		this.losses = losses;
	}

	/**
	 * @return
	 * @uml.property name="points"
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * @param points
	 * @uml.property name="points"
	 */
	public void setPoints(int points) {
		this.points = points;
	}

	/**
	 * @return
	 * @uml.property name="rankTotal"
	 */
	public String getRankTotal() {
		return rankTotal;
	}

	/**
	 * @param rankTotal
	 * @uml.property name="rankTotal"
	 */
	public void setRankTotal(String rankTotal) {
		this.rankTotal = rankTotal;
	}

	/**
	 * @return
	 * @uml.property name="round"
	 */
	public int getRound() {
		return round;
	}

	/**
	 * @param round
	 * @uml.property name="round"
	 */
	public void setRound(int round) {
		this.round = round;
	}

	/**
	 * @return
	 * @uml.property name="teamID"
	 */
	public int getTeamId() {
		return teamId;
	}

	/**
	 * @param teamId
	 * @uml.property name="teamID"
	 */
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	/**
	 * @return
	 * @uml.property name="wins"
	 */
	public int getWins() {
		return wins;
	}

	/**
	 * @param wins
	 * @uml.property name="wins"
	 */
	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getBeginPlace() {
		if (beginPlace == 0) {
			setBeginPlace(Integer.valueOf(rankTotal.substring(rankTotal.length() - 1)));
		}
		return beginPlace;
	}

	public void setBeginPlace(int beginPlace) {
		this.beginPlace = beginPlace;
	}

	public Club getClub() {
		return club;
	}

	public void setClub(Club club) {
		this.club = club;
	}

}
