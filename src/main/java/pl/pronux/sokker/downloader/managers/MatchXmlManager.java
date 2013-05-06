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

import pl.pronux.sokker.actions.MatchesManager;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.data.sql.dao.LeagueDao;
import pl.pronux.sokker.downloader.xml.XMLDownloader;
import pl.pronux.sokker.downloader.xml.parsers.MatchXmlParser;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Match;
import pl.pronux.sokker.utils.Log;

public class MatchXmlManager extends XmlManager<Match> {

	private List<Match> matches = new ArrayList<Match>();

	private Map<String, String> matchesMap = new HashMap<String, String>();

	private MatchesManager matchesManager = MatchesManager.getInstance();
	
	public MatchXmlManager(String destination, XMLDownloader downloader, Date currentDay) {
		super("match", destination, downloader, currentDay); 
	}

	public MatchXmlManager() {
		super(null, null, 0);
	}
	
	public void completeMatches(int teamId, int limit) throws SQLException {
		String value = "-3"; 
		List<Match> matches = new ArrayList<Match>();
		LeagueDao leagueDao = new LeagueDao(SQLSession.getConnection());
		
		List<Match> uncompletedMatches = leagueDao.getUncompletedLeague(teamId);

		if (getDownloader().getStatus().equals("OK")) { 
			value = "0"; 
		} else {
			value = getDownloader().getErrorno();
		}

		if (value.equals("0")) { 
			if (uncompletedMatches.size() > 0) {
				Match match = uncompletedMatches.get(0);

				for (Match itrmatch : uncompletedMatches) {
					matchesMap.put(String.valueOf(itrmatch.getMatchId()), ""); 
				}

				int matchId = match.getMatchId() - 1;
				while (limit > 0) {
					if (!matchesMap.containsKey(String.valueOf(matchId))) {
						try {
							download(matchId);
							matches = parseXML(matchesMap.get(String.valueOf(matchId)));
						} catch (Exception e) {
							Log.warning(this.toString(), e);
							continue;
						}
						if (matches.size() > 0 && matches.get(0).getLeagueId() == match.getLeagueId() && matches.get(0).getSeason() == match.getSeason()) {
							matchesManager.importMatches(matches);
							try {
								write(matchesMap.get(String.valueOf(matchId)), String.valueOf(matchId));
							} catch (IOException e) {
							}
						} else {
							if (matchId > match.getMatchId()) {
								limit = 0;
							} else {
								matchId = match.getMatchId();
							}
						}
						limit--;
					}
					if (matchId < match.getMatchId()) {
						matchId -= 1;
					} else {
						matchId += 1;
					}
				}
			}
		}
	}

	/*
	 * empty method
	 */
	@Override
	public void download() throws IOException {
		// TODO Auto-generated method stub
	}

	public void download(List<Match> matches) throws IOException {
		for (Match match : matches) {
			String matchId = String.valueOf(match.getMatchId());
			matchesMap.put(matchId, getDownloader().getMatch(matchId));
		}
	}

	public void download(int matchId) throws IOException {
		matchesMap.put(String.valueOf(matchId), getDownloader().getMatch(String.valueOf(matchId)));
	}

	@Override
	public void importToSQL() throws SQLException {
		matchesManager.importMatches(matches);
	}

	public List<Match> parseXML() throws SAXException {
		Set<String> matchSet = matchesMap.keySet();

		for (String key : matchSet) {
			matches.addAll(parseXML(matchesMap.get(key)));
		}
		return matches;
	}

	public List<Match> parseXML(List<String> files) throws SAXException {
		for (String xml : files) {
			matches.addAll(parseXML(xml));
		}
		return matches;
	}
	
	@Override
	public List<Match> parseXML(String xml) throws SAXException {
		MatchXmlParser matchXMLParser = new MatchXmlParser();
		InputSource input = new InputSource(new StringReader(xml));
		try {
			matchXMLParser.parseXmlSax(input, null);
		} catch (SAXException ex) {
			input = new InputSource(new StringReader(filterCharacters(xml)));
			matchXMLParser.parseXmlSax(input, null);
		}

		List<Match> matches = new ArrayList<Match>();
		Match match = matchXMLParser.getMatch();
		matches.add(match);
		return matches;
	}

	@Override
	public boolean write() throws IOException {
		Set<String> matchSet = matchesMap.keySet();

		for (String key : matchSet) {
			if (!write(matchesMap.get(key), key)) {
				// return false;
			}
		}
		return true;
	}
}