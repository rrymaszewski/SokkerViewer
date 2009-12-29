package pl.pronux.sokker.model;

import java.util.List;

public class LeagueRound {
	
	private LeagueSeason leagueSeason;

	private List<LeagueTeam> leagueTeams;

	private List<Match> matches;

	private int round;

	public LeagueSeason getLeagueSeason() {
		return leagueSeason;
	}

	public List<LeagueTeam> getLeagueTeams() {
		return leagueTeams;
	}

	public List<Match> getMatches() {
		return matches;
	}

	public int getRoundNumber() {
		return round;
	}

	public void setLeagueSeason(LeagueSeason leagueSeason) {
		this.leagueSeason = leagueSeason;
	}

	public void setLeagueTeams(List<LeagueTeam> leagueTeams) {
		this.leagueTeams = leagueTeams;
	}

	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}

	public void setRound(int round) {
		this.round = round;
	}
}
