package pl.pronux.sokker.downloader.xml;

import java.io.IOException;

import pl.pronux.sokker.downloader.SokkerAuthentication;

public class XMLDownloader extends SokkerAuthentication {

	public XMLDownloader() {
	}

	public String getTrainers() throws IOException {
		String trainers = this.getXML("http://217.17.40.90/xml/trainers.xml"); 
		return trainers;
	}

	public String getCountries() throws IOException {
		String countries = this.getXML("http://217.17.40.90/xml/countries.xml"); 
		return countries;
	}

	public String getCountry(String countryID) throws IOException  {
		String country = this.getXML("http://217.17.40.90/xml/country-" + countryID + ".xml");  
		return country;
	}

	public String getJuniors() throws IOException  {
		String juniors = this.getXML("http://217.17.40.90/xml/juniors.xml"); 
		return juniors;
	}

	public String getLeague(String leagueID) throws IOException  {
		String league = this.getXML("http://217.17.40.90/xml/league-" + leagueID + ".xml");  
		return league;
	}

	public String getLeague(String countryID, String division, String number) throws IOException  {
		String league = this.getXML("http://217.17.40.90/xml/league-" + countryID + "-" + division + "-" + number + ".xml");    
		return league;
	}

	public String getMatch(String matchID) throws IOException {
		String match = this.getXML("http://217.17.40.90/xml/match-" + matchID + ".xml");  
		return match;
	}

	public String getLeagueMatches(String leagueID, String round) throws IOException  {
		String match = this.getXML("http://217.17.40.90/xml/matches-league-" + leagueID + "-" + round + ".xml");   
		return match;
	}

	public String getMatchesTeam(String teamID) throws IOException  {
		String matches = this.getXML("http://217.17.40.90/xml/matches-team-" + teamID + ".xml");  
		return matches;
	}

	public String getPlayer(String playerID) throws IOException {
		String player = this.getXML("http://217.17.40.90/xml/player-" + playerID + ".xml");  
		return player;
	}

	public String getPlayers(String teamID) throws IOException  {
		String players = this.getXML("http://217.17.40.90/xml/players-" + teamID + ".xml");  
		return players;
	}

	public String getRegion(String regionID) throws IOException {
		String region = this.getXML("http://217.17.40.90/xml/region-" + regionID + ".xml");  
		return region;
	}

	public String getRegions(String countryID) throws IOException{
		String regions = this.getXML("http://217.17.40.90/xml/regions-" + countryID + ".xml");  
		return regions;
	}

	public String getReports() throws IOException {
		String reports = this.getXML("http://217.17.40.90/xml/reports.xml"); 
		return reports;
	}

	public String getTeam(String teamID) throws IOException {
		String team = this.getXML("http://217.17.40.90/xml/team-" + teamID + ".xml");  
		return team;
	}

	public String getTransfers() throws IOException {
		String transfers = this.getXML("http://217.17.40.90/xml/transfers.xml"); 
		return transfers;
	}

	public String getVars() throws IOException {
		String vars = this.getXML("http://217.17.40.90/xml/vars.xml"); 
		return vars;
	}

}