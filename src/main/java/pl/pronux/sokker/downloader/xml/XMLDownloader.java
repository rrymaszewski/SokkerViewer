package pl.pronux.sokker.downloader.xml;

import java.io.IOException;

import pl.pronux.sokker.downloader.SokkerAuthentication;

public class XMLDownloader extends SokkerAuthentication {

	private final static String SOKKER_URL = "http://online.sokker.org/xml/";
	
	public XMLDownloader() {
	}

	public String getTrainers() throws IOException {
		return getXML(SOKKER_URL + "trainers.xml"); 
	}

	public String getCountries() throws IOException {
		return getXML(SOKKER_URL + "countries.xml"); 
	}

	public String getCountry(String countryID) throws IOException  {
		return getXML(SOKKER_URL + "country-" + countryID + ".xml");  
	}

	public String getJuniors() throws IOException  {
		return getXML(SOKKER_URL + "juniors.xml"); 
	}

	public String getLeague(String leagueID) throws IOException  {
		return getXML(SOKKER_URL + "league-" + leagueID + ".xml");  
	}

	public String getLeague(String countryID, String division, String number) throws IOException  {
		return getXML(SOKKER_URL + "league-" + countryID + "-" + division + "-" + number + ".xml");    
	}

	public String getMatch(String matchID) throws IOException {
		return getXML(SOKKER_URL + "match-" + matchID + ".xml");  
	}

	public String getLeagueMatches(String leagueID, String round) throws IOException  {
		return getXML(SOKKER_URL + "matches-league-" + leagueID + "-" + round + ".xml");   
	}

	public String getMatchesTeam(String teamID) throws IOException  {
		return getXML(SOKKER_URL + "matches-team-" + teamID + ".xml");  
	}

	public String getPlayer(String playerID) throws IOException {
		return getXML(SOKKER_URL + "player-" + playerID + ".xml");  
	}

	public String getPlayers(String teamID) throws IOException  {
		return getXML(SOKKER_URL + "players-" + teamID + ".xml");  
	}

	public String getRegion(String regionID) throws IOException {
		return getXML(SOKKER_URL + "region-" + regionID + ".xml");  
	}

	public String getRegions(String countryID) throws IOException{
		return getXML(SOKKER_URL + "regions-" + countryID + ".xml");  
	}

	public String getReports() throws IOException {
		return getXML(SOKKER_URL + "reports.xml"); 
	}

	public String getTeam(String teamID) throws IOException {
		return getXML(SOKKER_URL + "team-" + teamID + ".xml");  
	}

	public String getTransfers() throws IOException {
		return getXML(SOKKER_URL + "transfers.xml"); 
	}

	public String getVars() throws IOException {
		return getXML(SOKKER_URL + "vars.xml"); 
	}

}