package pl.pronux.sokker.model;

import java.util.List;

/**
 * @author  rymek
 */
public class League {
	public final static int TYPE_LEAGUE = 0;
	public final static int TYPE_CUP = 1;
	public final static int TYPE_PLAYOFF = 2;
	public final static int TYPE_FRIENDLY_MATCH = 3;
	public final static int TYPE_NT_LEAGUE = 4;
	public final static int OFFICIAL = 1;
	public final static int NOT_OFFICIAL = 0;
	public final static int CUP = 1;
	public final static int NOT_CUP = 0;

	private List<LeagueTeam> leagueTeams;

	private List<LeagueSeason> leagueSeasons;

	private int leagueID;

	private String name;

	private int countryID;

	private int division;

	private int round;

	private int season;

	private int type;

	private int isOfficial;

	private int isCup;

	private int userID;

	private int idData;


	public int getCountryID() {
		return countryID;
	}


	public void setCountryID(int countryID) {
		this.countryID = countryID;
	}


	public int getDivision() {
		return division;
	}


	public void setDivision(int division) {
		this.division = division;
	}


	public int getIsCup() {
		return isCup;
	}


	public void setIsCup(int isCup) {
		this.isCup = isCup;
	}

	public int getIsOfficial() {
		return isOfficial;
	}

	public void setIsOfficial(int isOfficial) {
		this.isOfficial = isOfficial;
	}

	public int getLeagueID() {
		return leagueID;
	}

	public void setLeagueID(int leagueID) {
		this.leagueID = leagueID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}

	public int getUserID() {
		return userID;
	}


	public void setUserID(int userID) {
		this.userID = userID;
	}

	public List<LeagueTeam> getLeagueTeams() {
		return leagueTeams;
	}


	public void setLeagueTeams(List<LeagueTeam> leagueTeams) {
		this.leagueTeams = leagueTeams;
	}


	public int getIdData() {
		return idData;
	}


	public void setIdData(int idData) {
		this.idData = idData;
	}


	public List<LeagueSeason> getLeagueSeasons() {
		return leagueSeasons;
	}


	public void setLeagueSeasons(List<LeagueSeason> alLeagueSeasons) {
		this.leagueSeasons = alLeagueSeasons;
	}

}
