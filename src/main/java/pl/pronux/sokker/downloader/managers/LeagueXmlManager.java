package pl.pronux.sokker.downloader.managers;

import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pl.pronux.sokker.actions.LeaguesManager;
import pl.pronux.sokker.downloader.xml.XMLDownloader;
import pl.pronux.sokker.downloader.xml.parsers.LeagueIDXmlParser;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.League;
import pl.pronux.sokker.model.Match;

public class LeagueXmlManager extends XmlManager<League> {
	
	private LeaguesManager leaguesManager = LeaguesManager.getInstance();
	
	private List<League> leagues = new ArrayList<League>();
	private Map<String, String> leaguesMap = new HashMap<String, String>();
	private Set<String> leagueIdSet = new HashSet<String>();
	public LeagueXmlManager(String destination, XMLDownloader downloader, Date currentDay) {
		super("league", destination, downloader, currentDay); 
	}
	
	public LeagueXmlManager() {
		super(null, null, 0);
	}

	@Override
	public void download() throws IOException {
		// TODO Auto-generated method stub
	}
	
	public void download(List<Match> matches) throws IOException {
		for (Match match : matches) {
			leagueIdSet.add(String.valueOf(match.getLeagueId()));
		}
		
		for (String leagueId : leagueIdSet) {
			leaguesMap.put(leagueId, getDownloader().getLeague(leagueId));
		}
	}
	
	public void download(int leagueId) throws IOException {
		leaguesMap.put(String.valueOf(leagueId), getDownloader().getLeague(String.valueOf(leagueId)));
	}

	@Override
	public void importToSQL() throws SQLException {
		leaguesManager.importLeagues(leagues);
	}
	
	@Override
	public List<League> parseXML(String xml) throws SAXException {
		LeagueIDXmlParser leagueIDXmlParser = new LeagueIDXmlParser();
		InputSource input = new InputSource(new StringReader(xml));
		try {
			leagueIDXmlParser.parseXmlSax(input, null);
		} catch (SAXException ex) {
			input = new InputSource(new StringReader(filterCharacters(xml)));
			leagueIDXmlParser.parseXmlSax(input, null);
		}
		
		List<League> leagues = new ArrayList<League>();
		leagues.add(leagueIDXmlParser.getLeague());
		return leagues;
	}
	
	public List<League> parseXML() throws SAXException {
		Set<String> leaguesSet = leaguesMap.keySet();

		for (String key : leaguesSet) {
			List<League> tempLeagues = parseXML(leaguesMap.get(key));
			if(tempLeagues.get(0).getLeagueId() == 0 && !key.equals("0")) { 
				tempLeagues.get(0).setLeagueId(Integer.valueOf(key));
			}
			leagues.addAll(tempLeagues);
		}
		return leagues;
	}
	
	public List<League> parseXML(List<String> files) throws SAXException {
		for (String xml : files) {
			leagues.addAll(parseXML(xml));
		}
		return leagues;
	}

	@Override
	public boolean write() throws IOException {
		Set<String> leaguesSet = leaguesMap.keySet();
		for (String key : leaguesSet) {
			if (!write(leaguesMap.get(key), key)) {
				// return false;
			}
		}
		return true;
	}
}
