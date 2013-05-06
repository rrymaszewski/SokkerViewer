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

import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.data.sql.dao.LeagueDao;
import pl.pronux.sokker.downloader.xml.XMLDownloader;
import pl.pronux.sokker.downloader.xml.parsers.MatchesLeagueXmlParser;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.League;
import pl.pronux.sokker.model.LeagueTeam;
import pl.pronux.sokker.model.Match;

public class LeagueMatchesXmlManager extends XmlManager<Match> {

	public LeagueMatchesXmlManager(String destination, XMLDownloader downloader, Date currentDay) {
		super("league_matches", destination, downloader, currentDay); 
	}

	private Map<String, String> leagueMatchesMap = new HashMap<String, String>();

	private List<Match> matches = new ArrayList<Match>();

	@Override
	public void download() throws IOException {
		// TODO Auto-generated method stub
	}

	public void download(List<League> leagues) throws SQLException, IOException {
		for (League league : leagues) {
			boolean check = false;
			
			// checking if list contains our team
			// if not it means that we trying to download our previous league in which we aren't know
			if (league.getLeagueTeams().size() > 0) {
				for (LeagueTeam leagueTeam : league.getLeagueTeams()) {
					if (leagueTeam.getTeamId() == Integer.valueOf(getDownloader().getTeamId())) {
						check = true;
						break;
					}
				}
			}

			
			if (check) {
				if (league.getType() == League.TYPE_LEAGUE && league.getIsCup() == League.NOT_CUP) {
					int numbersOfTeams = league.getLeagueTeams().size();
					LeagueDao leagueDao = new LeagueDao(SQLSession.getConnection());
					int numberOfRoundsInDatabase = leagueDao.getNumberOfRounds(league);
					int numberOfRounds = 0;

					if (league.getIsOfficial() == League.OFFICIAL) {
						
						numberOfRounds = (numbersOfTeams - 1) * 2;

						if (numberOfRoundsInDatabase == numberOfRounds) {
							numberOfRounds = league.getRound();
						}

						List<Integer> completedRounds = leagueDao.getCompletedRounds(league);

						for (int j = 1; j <= numberOfRounds; j++) {
							if (!completedRounds.contains(j)) {
								leagueMatchesMap.put(league.getLeagueId() + "_" + j, getDownloader().getLeagueMatches(String.valueOf(league.getLeagueId()), String.valueOf(j))); 
							}
						}
					} else {
						// max = 16;
						// doesn't download friendly league
					}
				}
			} else {
				// removing teams from league in which we were last season ago
				league.getLeagueTeams().clear();
			}
		}
	}

	@Override
	public void importToSQL() throws SQLException {
		// TODO Auto-generated method stub
	}

	@Override
	public List<Match> parseXML(String xml) throws SAXException {
		MatchesLeagueXmlParser matchesLeagueXMLParser = new MatchesLeagueXmlParser();
		InputSource input = new InputSource(new StringReader(xml));
		try {
			matchesLeagueXMLParser.parseXmlSax(input, null);
		} catch (SAXException ex) {
			input = new InputSource(new StringReader(filterCharacters(xml)));
			matchesLeagueXMLParser.parseXmlSax(input, null);
		}
		return matchesLeagueXMLParser.getMatches();
	}

	public List<Match> parseXML() throws SAXException {
		Set<String> teamsStringSet = leagueMatchesMap.keySet();

		for (String key : teamsStringSet) {
			matches.addAll(parseXML(leagueMatchesMap.get(key)));
		}

		return matches;
	}

	@Override
	public boolean write() throws IOException {

		Set<String> teamsStringSet = leagueMatchesMap.keySet();

		for (String key : teamsStringSet) {
			if (!write(leagueMatchesMap.get(key), key)) {
				// return false;
			}
		}
		return true;
	}

}
