package pl.pronux.sokker.downloader.managers;

import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pl.pronux.sokker.downloader.xml.XMLDownloader;
import pl.pronux.sokker.downloader.xml.parsers.MatchesTeamXmlParser;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Match;

public class MatchesTeamXmlManager extends XmlManager<Match> {

	private List<Match> matchesTeam = new ArrayList<Match>();
	private Map<String, String> matchesTeamMap = new HashMap<String, String>();

	public MatchesTeamXmlManager(String destination, XMLDownloader downloader, Date currentDay) {
		super("matchesTeam", destination, downloader, currentDay);
	}

	@Override
	public void download() throws IOException {
		setContent(downloader.getMatchesTeam(downloader.getTeamId()));
	}
	
	public void download(String teamID) throws IOException {
		matchesTeamMap.put(teamID, downloader.getMatchesTeam(teamID));
	}

	@Override
	public void importToSQL() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Match> parseXML(String xml) throws SAXException {
		MatchesTeamXmlParser matchesTeamXmlParser = new MatchesTeamXmlParser();
		InputSource input = new InputSource(new StringReader(xml));
		try {
			matchesTeamXmlParser.parseXmlSax(input, null);
		} catch (SAXException ex) {
			input = new InputSource(new StringReader(filterCharacters(xml)));
			matchesTeamXmlParser.parseXmlSax(input, null);
		}
		return matchesTeamXmlParser.getMatches();
	}
	
	@Override
	public boolean write() throws IOException {
		write(getContent(), downloader.getTeamId());

		Set<String> matchesTeamStringSet = matchesTeamMap.keySet();

		for (String key : matchesTeamStringSet) {
			if (!write(matchesTeamMap.get(key), key)) {
				// return false;
			}
		}

		return true;
	}
	
	public List<Match> parseXML(int teamId) throws SAXException {
		List<Match> matches = parseXML(matchesTeamMap.get(String.valueOf(teamId)));
		return matches;
	}

	public List<Match> parseXML() throws SAXException {
		List<Match> matches = parseXML(getContent());
		matchesTeam.addAll(matches);

		Set<String> matchesTeamStringSet = matchesTeamMap.keySet();
		for (String key : matchesTeamStringSet) {
			matches.addAll(parseXML(matchesTeamMap.get(key))); 
		}
		return matchesTeam;
	}

}
