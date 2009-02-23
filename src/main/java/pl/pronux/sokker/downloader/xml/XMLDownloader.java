package pl.pronux.sokker.downloader.xml;

import java.io.IOException;

import pl.pronux.sokker.downloader.SokkerAuthentication;

public class XMLDownloader extends SokkerAuthentication {

	public XMLDownloader() {
	}

	public String getTrainers() throws IOException {
		String trainers = this.getXML("http://217.17.40.90/xml/trainers.xml"); //$NON-NLS-1$
		return trainers;
	}

	public String getCountries() throws IOException {
		String countries = this.getXML("http://217.17.40.90/xml/countries.xml"); //$NON-NLS-1$
		return countries;
	}

	public String getCountry(String countryID) throws IOException  {
		String country = this.getXML("http://217.17.40.90/xml/country-" + countryID + ".xml"); //$NON-NLS-1$ //$NON-NLS-2$
		return country;
	}

	public String getJuniors() throws IOException  {
		String juniors = this.getXML("http://217.17.40.90/xml/juniors.xml"); //$NON-NLS-1$
		return juniors;
	}

	public String getLeague(String leagueID) throws IOException  {
		String league = this.getXML("http://217.17.40.90/xml/league-" + leagueID + ".xml"); //$NON-NLS-1$ //$NON-NLS-2$
		return league;
	}

	public String getLeague(String countryID, String division, String number) throws IOException  {
		String league = this.getXML("http://217.17.40.90/xml/league-" + countryID + "-" + division + "-" + number + ".xml"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		return league;
	}

	public String getMatch(String matchID) throws IOException {
		String match = this.getXML("http://217.17.40.90/xml/match-" + matchID + ".xml"); //$NON-NLS-1$ //$NON-NLS-2$
		return match;
	}

	public String getLeagueMatches(String leagueID, String round) throws IOException  {
		String match = this.getXML("http://217.17.40.90/xml/matches-league-" + leagueID + "-" + round + ".xml"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return match;
	}

	public String getMatchesTeam(String teamID) throws IOException  {
		String matches = this.getXML("http://217.17.40.90/xml/matches-team-" + teamID + ".xml"); //$NON-NLS-1$ //$NON-NLS-2$
		return matches;
	}

	public String getPlayer(String playerID) throws IOException {
		String player = this.getXML("http://217.17.40.90/xml/player-" + playerID + ".xml"); //$NON-NLS-1$ //$NON-NLS-2$
		return player;
	}

	public String getPlayers(String teamID) throws IOException  {
		String players = this.getXML("http://217.17.40.90/xml/players-" + teamID + ".xml"); //$NON-NLS-1$ //$NON-NLS-2$
		return players;
	}

	public String getRegion(String regionID) throws IOException {
		String region = this.getXML("http://217.17.40.90/xml/region-" + regionID + ".xml"); //$NON-NLS-1$ //$NON-NLS-2$
		return region;
	}

	public String getRegions(String countryID) throws IOException{
		String regions = this.getXML("http://217.17.40.90/xml/regions-" + countryID + ".xml"); //$NON-NLS-1$ //$NON-NLS-2$
		return regions;
	}

	public String getReports() throws IOException {
		String reports = this.getXML("http://217.17.40.90/xml/reports.xml"); //$NON-NLS-1$
		return reports;
	}

	public String getTeam(String teamID) throws IOException {
		String team = this.getXML("http://217.17.40.90/xml/team-" + teamID + ".xml"); //$NON-NLS-1$ //$NON-NLS-2$
		return team;
	}

	public String getTransfers() throws IOException {
		String transfers = this.getXML("http://217.17.40.90/xml/transfers.xml"); //$NON-NLS-1$
		return transfers;
	}

	public String getVars() throws IOException {
		String vars = this.getXML("http://217.17.40.90/xml/vars.xml"); //$NON-NLS-1$
		return vars;
	}

}