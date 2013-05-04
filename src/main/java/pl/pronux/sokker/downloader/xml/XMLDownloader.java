package pl.pronux.sokker.downloader.xml;

import java.io.IOException;

import pl.pronux.sokker.downloader.SokkerAuthentication;

public class XMLDownloader extends SokkerAuthentication {

	public XMLDownloader() {
	}

	public String getTrainers() throws IOException {
		return getXML("http://217.17.40.90/xml/trainers.xml"); 
	}

	public String getCountries() throws IOException {
		return getXML("http://217.17.40.90/xml/countries.xml"); 
	}

	public String getCountry(String countryID) throws IOException  {
		return getXML("http://217.17.40.90/xml/country-" + countryID + ".xml");  
	}

	public String getJuniors() throws IOException  {
		return getXML("http://217.17.40.90/xml/juniors.xml"); 
	}

	public String getLeague(String leagueID) throws IOException  {
		return getXML("http://217.17.40.90/xml/league-" + leagueID + ".xml");  
	}

	public String getLeague(String countryID, String division, String number) throws IOException  {
		return getXML("http://217.17.40.90/xml/league-" + countryID + "-" + division + "-" + number + ".xml");    
	}

	public String getMatch(String matchID) throws IOException {
		return getXML("http://217.17.40.90/xml/match-" + matchID + ".xml");  
	}

	public String getLeagueMatches(String leagueID, String round) throws IOException  {
		return getXML("http://217.17.40.90/xml/matches-league-" + leagueID + "-" + round + ".xml");   
	}

	public String getMatchesTeam(String teamID) throws IOException  {
		return getXML("http://217.17.40.90/xml/matches-team-" + teamID + ".xml");  
	}

	public String getPlayer(String playerID) throws IOException {
		return getXML("http://217.17.40.90/xml/player-" + playerID + ".xml");  
	}

	public String getPlayers(String teamID) throws IOException  {
		return getXML("http://217.17.40.90/xml/players-" + teamID + ".xml");  
	}

	public String getRegion(String regionID) throws IOException {
		return getXML("http://217.17.40.90/xml/region-" + regionID + ".xml");  
	}

	public String getRegions(String countryID) throws IOException{
		return getXML("http://217.17.40.90/xml/regions-" + countryID + ".xml");  
	}

	public String getReports() throws IOException {
		return getXML("http://217.17.40.90/xml/reports.xml"); 
	}

	public String getTeam(String teamID) throws IOException {
		return getXML("http://217.17.40.90/xml/team-" + teamID + ".xml");  
	}

	public String getTransfers() throws IOException {
		return getXML("http://217.17.40.90/xml/transfers.xml"); 
	}

	public String getVars() throws IOException {
		return getXML("http://217.17.40.90/xml/vars.xml"); 
	}

}