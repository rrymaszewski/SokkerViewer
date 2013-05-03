package pl.pronux.sokker.model;

import java.util.List;

/**
 * @author  rymek
 */
public class League {
	public static final int TYPE_LEAGUE = 0;
	public static final int TYPE_CUP = 1;
	public static final int TYPE_PLAYOFF = 2;
	public static final int TYPE_FRIENDLY_MATCH = 3;
	public static final int TYPE_NT_U21_LEAGUE = 4;
	public static final int TYPE_NT_WORLD_CUP = 5;
	public static final int TYPE_U21_WORLD_CUP = 6;
	public static final int TYPE_JUNIOR_LEAGUE = 7;
	public static final int TYPE_CHAMPIONS_CUP = 9;
	public static final int TYPE_NT_U21_FRIENDLY_MATCH = 10;
	public static final int OFFICIAL = 1;
	public static final int NOT_OFFICIAL = 0;
	public static final int CUP = 1;
	public static final int NOT_CUP = 0;

	private List<LeagueTeam> leagueTeams;

	private List<LeagueSeason> leagueSeasons;

	private int leagueId;

	private String name;

	private int countryId;

	private int division;

	private int round;

	private int season;

	private int type;

	private int isOfficial;

	private int isCup;

	private int userId;

	private int dataId;


	public int getCountryId() {
		return countryId;
	}


	public void setCountryId(int countryId) {
		this.countryId = countryId;
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

	public int getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(int leagueId) {
		this.leagueId = leagueId;
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

	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}

	public List<LeagueTeam> getLeagueTeams() {
		return leagueTeams;
	}


	public void setLeagueTeams(List<LeagueTeam> leagueTeams) {
		this.leagueTeams = leagueTeams;
	}


	public int getDataId() {
		return dataId;
	}


	public void setDataId(int dataId) {
		this.dataId = dataId;
	}


	public List<LeagueSeason> getLeagueSeasons() {
		return leagueSeasons;
	}


	public void setLeagueSeasons(List<LeagueSeason> leagueSeasons) {
		this.leagueSeasons = leagueSeasons;
	}

}
